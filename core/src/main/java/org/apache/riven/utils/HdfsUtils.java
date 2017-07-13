/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.riven.utils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.fs.Trash;
import org.apache.hadoop.fs.permission.AclEntry;
import org.apache.hadoop.fs.permission.AclEntryScope;
import org.apache.hadoop.fs.permission.AclEntryType;
import org.apache.hadoop.fs.permission.AclStatus;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.client.HdfsAdmin;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * Utilities for accessing HDFS.  Each method put in here is assumed to work against both Hadoop
 * 2 and 3.  Hadoop 1 is not supported.
 */
public class HdfsUtils {
  private static Logger LOG = LoggerFactory.getLogger(HdfsUtils.class);

  public static final PathFilter HIDDEN_FILES_PATH_FILTER = new PathFilter() {
    @Override
    public boolean accept(Path p) {
      String name = p.getName();
      return !name.startsWith("_") && !name.startsWith(".");
    }
  };

  /**
   * Check the permissions on a file.
   * @param fs Filesystem the file is contained in
   * @param stat Stat info for the file
   * @param action action to be performed
   * @throws IOException If thrown by Hadoop
   * @throws AccessControlException if the file cannot be accessed
   */
  public static void checkFileAccess(FileSystem fs, FileStatus stat, FsAction action)
      throws IOException, LoginException {
    checkFileAccess(fs, stat, action, SecurityUtils.getUGI());
  }

  /**
   * Check the permissions on a file
   * @param fs Filesystem the file is contained in
   * @param stat Stat info for the file
   * @param action action to be performed
   * @param ugi user group info for the current user.  This is passed in so that tests can pass
   *            in mock ones.
   * @throws IOException If thrown by Hadoop
   * @throws AccessControlException if the file cannot be accessed
   */
  @VisibleForTesting
  static void checkFileAccess(FileSystem fs, FileStatus stat, FsAction action,
                              UserGroupInformation ugi) throws IOException {

    String user = ugi.getShortUserName();
    String[] groups = ugi.getGroupNames();

    if (groups != null) {
      String superGroupName = fs.getConf().get("dfs.permissions.supergroup", "");
      if (arrayContains(groups, superGroupName)) {
        LOG.debug("User \"" + user + "\" belongs to super-group \"" + superGroupName + "\". " +
            "Permission granted for action: " + action + ".");
        return;
      }
    }

    FsPermission dirPerms = stat.getPermission();

    if (user.equals(stat.getOwner())) {
      if (dirPerms.getUserAction().implies(action)) {
        return;
      }
    } else if (arrayContains(groups, stat.getGroup())) {
      if (dirPerms.getGroupAction().implies(action)) {
        return;
      }
    } else if (dirPerms.getOtherAction().implies(action)) {
      return;
    }
    throw new AccessControlException("action " + action + " not permitted on path "
        + stat.getPath() + " for user " + user);
  }

  private static boolean arrayContains(String[] array, String value) {
    if (array == null) return false;
    for (String element : array) {
      if (element.equals(value)) return true;
    }
    return false;
  }

  public static boolean rename(FileSystem fs, Path sourcePath, Path destPath) throws IOException {
    LOG.debug("Renaming " + sourcePath + " to " + destPath);

    // If destPath directory exists, rename call will move the sourcePath
    // into destPath without failing. So check it before renaming.
    if (fs.exists(destPath)) {
      throw new IOException("Cannot rename the source path. The destination "
          + "path already exists.");
    }
    return fs.rename(sourcePath, destPath);
  }

  /**
   * Escapes a path name.
   * @param path The path to escape.
   * @return An escaped path name.
   */
  public static String escapePathName(String path) {
    return escapePathName(path, null);
  }

  /**
   * Escapes a path name.
   * @param path The path to escape.
   * @param defaultPath
   *          The default name for the path, if the given path is empty or null.
   * @return An escaped path name.
   */
  public static String escapePathName(String path, String defaultPath) {

    // __HIVE_DEFAULT_NULL__ is the system default value for null and empty string.
    // TODO: we should allow user to specify default partition or HDFS file location.
    if (path == null || path.length() == 0) {
      if (defaultPath == null) {
        //previously, when path is empty or null and no default path is specified,
        // __HIVE_DEFAULT_PARTITION__ was the return value for escapePathName
        return "__HIVE_DEFAULT_PARTITION__";
      } else {
        return defaultPath;
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (needsEscaping(c)) {
        sb.append('%');
        sb.append(String.format("%1$02X", (int) c));
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  // NOTE: This is for generating the internal path name for partitions. Users
  // should always use the MetaStore API to get the path name for a partition.
  // Users should not directly take partition values and turn it into a path
  // name by themselves, because the logic below may change in the future.
  //
  // In the future, it's OK to add new chars to the escape list, and old data
  // won't be corrupt, because the full path name in metastore is stored.
  // In that case, Hive will continue to read the old data, but when it creates
  // new partitions, it will use new names.
  // edit : There are some use cases for which adding new chars does not seem
  // to be backward compatible - Eg. if partition was created with name having
  // a special char that you want to start escaping, and then you try dropping
  // the partition with a hive version that now escapes the special char using
  // the list below, then the drop partition fails to work.

  static BitSet charToEscape = new BitSet(128);
  static {
    for (char c = 0; c < ' '; c++) {
      charToEscape.set(c);
    }

    /**
     * ASCII 01-1F are HTTP control characters that need to be escaped.
     * \u000A and \u000D are \n and \r, respectively.
     */
    char[] clist = new char[] {'\u0001', '\u0002', '\u0003', '\u0004',
                               '\u0005', '\u0006', '\u0007', '\u0008', '\u0009', '\n', '\u000B',
                               '\u000C', '\r', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012',
                               '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019',
                               '\u001A', '\u001B', '\u001C', '\u001D', '\u001E', '\u001F',
                               '"', '#', '%', '\'', '*', '/', ':', '=', '?', '\\', '\u007F', '{',
                               '[', ']', '^'};

    for (char c : clist) {
      charToEscape.set(c);
    }
  }

  private static boolean needsEscaping(char c) {
    return c >= 0 && c < charToEscape.size() && charToEscape.get(c);
  }

  public static String unescapePathName(String path) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.length(); i++) {
      char c = path.charAt(i);
      if (c == '%' && i + 2 < path.length()) {
        int code = -1;
        try {
          code = Integer.parseInt(path.substring(i + 1, i + 3), 16);
        } catch (Exception e) {
          code = -1;
        }
        if (code >= 0) {
          sb.append((char) code);
          i += 2;
          continue;
        }
      }
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Get all file status from a root path and recursively go deep into certain levels.
   *
   * @param path
   *          the root path
   * @param level
   *          the depth of directory to explore
   * @param fs
   *          the file system
   * @return array of FileStatus
   * @throws IOException
   */
  public static FileStatus[] getFileStatusRecurse(Path path, int level, FileSystem fs)
      throws IOException {

    // if level is <0, the return all files/directories under the specified path
    if ( level < 0) {
      List<FileStatus> result = new ArrayList<FileStatus>();
      try {
        FileStatus fileStatus = fs.getFileStatus(path);
        listStatusRecursively(fs, fileStatus, result);
      } catch (IOException e) {
        // globStatus() API returns empty FileStatus[] when the specified path
        // does not exist. But getFileStatus() throw IOException. To mimic the
        // similar behavior we will return empty array on exception. For external
        // tables, the path of the table will not exists during table creation
        return new FileStatus[0];
      }
      return result.toArray(new FileStatus[result.size()]);
    }

    // construct a path pattern (e.g., /*/*) to find all dynamically generated paths
    StringBuilder sb = new StringBuilder(path.toUri().getPath());
    for (int i = 0; i < level; i++) {
      sb.append(Path.SEPARATOR).append("*");
    }
    Path pathPattern = new Path(path, sb.toString());
    return fs.globStatus(pathPattern, HIDDEN_FILES_PATH_FILTER);
  }

  /**
   * Recursively lists status for all files starting from a particular directory (or individual file
   * as base case).
   *
   * @param fs
   *          file system
   *
   * @param fileStatus
   *          starting point in file system
   *
   * @param results
   *          receives enumeration of all files found
   */
  public static void listStatusRecursively(FileSystem fs, FileStatus fileStatus,
                                           List<FileStatus> results) throws IOException {

    if (fileStatus.isDir()) {
      for (FileStatus stat : fs.listStatus(fileStatus.getPath(), HIDDEN_FILES_PATH_FILTER)) {
        listStatusRecursively(fs, stat, results);
      }
    } else {
      results.add(fileStatus);
    }
  }

  public static String makePartName(List<String> partCols, List<String> vals) {
    return makePartName(partCols, vals, null);
  }

  /**
   * Makes a valid partition name.
   * @param partCols The partition keys' names
   * @param vals The partition values
   * @param defaultStr
   *         The default name given to a partition value if the respective value is empty or null.
   * @return An escaped, valid partition name.
   */
  public static String makePartName(List<String> partCols, List<String> vals,
                                    String defaultStr) {
    StringBuilder name = new StringBuilder();
    for (int i = 0; i < partCols.size(); i++) {
      if (i > 0) {
        name.append(Path.SEPARATOR);
      }
      name.append(escapePathName((partCols.get(i)).toLowerCase(), defaultStr));
      name.append('=');
      name.append(escapePathName(vals.get(i), defaultStr));
    }
    return name.toString();
  }

  /**
   * Copy the permissions, group, and ACLs from a source {@link HadoopFileStatus} to a target {@link Path}. This method
   * will only log a warning if permissions cannot be set, no exception will be thrown.
   *
   * @param conf the {@link Configuration} used when setting permissions and ACLs
   * @param sourceStatus the source {@link HadoopFileStatus} to copy permissions and ACLs from
   * @param fs the {@link FileSystem} that contains the target {@link Path}
   * @param target the {@link Path} to copy permissions, group, and ACLs to
   * @param recursion recursively set permissions and ACLs on the target {@link Path}
   */
  public static void setFullFileStatus(Configuration conf, HdfsUtils.HadoopFileStatus sourceStatus,
                                       FileSystem fs, Path target, boolean recursion) {
    setFullFileStatus(conf, sourceStatus, null, fs, target, recursion);
  }

  /**
   * Copy the permissions, group, and ACLs from a source {@link HadoopFileStatus} to a target {@link Path}. This method
   * will only log a warning if permissions cannot be set, no exception will be thrown.
   *
   * @param conf the {@link Configuration} used when setting permissions and ACLs
   * @param sourceStatus the source {@link HadoopFileStatus} to copy permissions and ACLs from
   * @param targetGroup the group of the target {@link Path}, if this is set and it is equal to the source group, an
   *                    extra set group operation is avoided
   * @param fs the {@link FileSystem} that contains the target {@link Path}
   * @param target the {@link Path} to copy permissions, group, and ACLs to
   * @param recursion recursively set permissions and ACLs on the target {@link Path}
   */
  public static void setFullFileStatus(Configuration conf, HdfsUtils.HadoopFileStatus sourceStatus,
                                       String targetGroup, FileSystem fs, Path target, boolean recursion) {
    setFullFileStatus(conf, sourceStatus, targetGroup, fs, target, recursion, recursion ? new FsShell() : null);
  }

  @VisibleForTesting
  static void setFullFileStatus(Configuration conf, HdfsUtils.HadoopFileStatus sourceStatus,
                                String targetGroup, FileSystem fs, Path target, boolean recursion, FsShell fsShell) {
    try {
      FileStatus fStatus = sourceStatus.getFileStatus();
      String group = fStatus.getGroup();
      boolean aclEnabled = Objects.equal(conf.get("dfs.namenode.acls.enabled"), "true");
      FsPermission sourcePerm = fStatus.getPermission();
      List<AclEntry> aclEntries = null;
      if (aclEnabled) {
        if (sourceStatus.getAclEntries() != null) {
          LOG.trace(sourceStatus.getAclStatus().toString());
          aclEntries = new ArrayList<>(sourceStatus.getAclEntries());
          removeBaseAclEntries(aclEntries);

          //the ACL api's also expect the tradition user/group/other permission in the form of ACL
          aclEntries.add(newAclEntry(AclEntryScope.ACCESS, AclEntryType.USER, sourcePerm.getUserAction()));
          aclEntries.add(newAclEntry(AclEntryScope.ACCESS, AclEntryType.GROUP, sourcePerm.getGroupAction()));
          aclEntries.add(newAclEntry(AclEntryScope.ACCESS, AclEntryType.OTHER, sourcePerm.getOtherAction()));
        }
      }

      if (recursion) {
        //use FsShell to change group, permissions, and extended ACL's recursively
        fsShell.setConf(conf);
        //If there is no group of a file, no need to call chgrp
        if (group != null && !group.isEmpty()) {
          run(fsShell, new String[]{"-chgrp", "-R", group, target.toString()});
        }
        if (aclEnabled) {
          if (null != aclEntries) {
            //Attempt extended Acl operations only if its enabled, 8791but don't fail the operation regardless.
            try {
              //construct the -setfacl command
              String aclEntry = Joiner.on(",").join(aclEntries);
              run(fsShell, new String[]{"-setfacl", "-R", "--set", aclEntry, target.toString()});

            } catch (Exception e) {
              LOG.info("Skipping ACL inheritance: File system for path " + target + " " +
                  "does not support ACLs but dfs.namenode.acls.enabled is set to true. ");
              LOG.debug("The details are: " + e, e);
            }
          }
        } else {
          String permission = Integer.toString(sourcePerm.toShort(), 8);
          run(fsShell, new String[]{"-chmod", "-R", permission, target.toString()});
        }
      } else {
        if (group != null && !group.isEmpty()) {
          if (targetGroup == null ||
              !group.equals(targetGroup)) {
            fs.setOwner(target, null, group);
          }
        }
        if (aclEnabled) {
          if (null != aclEntries) {
            fs.setAcl(target, aclEntries);
          }
        } else {
          fs.setPermission(target, sourcePerm);
        }
      }
    } catch (Exception e) {
      LOG.warn(
          "Unable to inherit permissions for file " + target + " from file " + sourceStatus.getFileStatus().getPath(),
          e.getMessage());
      LOG.debug("Exception while inheriting permissions", e);
    }
  }

  /**
   * Create a new AclEntry with scope, type and permission (no name).
   *
   * @param scope
   *          AclEntryScope scope of the ACL entry
   * @param type
   *          AclEntryType ACL entry type
   * @param permission
   *          FsAction set of permissions in the ACL entry
   * @return AclEntry new AclEntry
   */
  private static AclEntry newAclEntry(AclEntryScope scope, AclEntryType type,
                                      FsAction permission) {
    return new AclEntry.Builder().setScope(scope).setType(type)
        .setPermission(permission).build();
  }
  /**
   * Removes basic permission acls (unamed acls) from the list of acl entries
   * @param entries acl entries to remove from.
   */
  private static void removeBaseAclEntries(List<AclEntry> entries) {
    Iterables.removeIf(entries, new Predicate<AclEntry>() {
      @Override
      public boolean apply(AclEntry input) {
        if (input.getName() == null) {
          return true;
        }
        return false;
      }
    });
  }

  private static void run(FsShell shell, String[] command) throws Exception {
    LOG.debug(ArrayUtils.toString(command));
    int retval = shell.run(command);
    LOG.debug("Return value is :" + retval);
  }

  public static class HadoopFileStatus {

    private final FileStatus fileStatus;
    private final AclStatus aclStatus;

    public HadoopFileStatus(Configuration conf, FileSystem fs, Path file) throws IOException {

      FileStatus fileStatus = fs.getFileStatus(file);
      AclStatus aclStatus = null;
      if (Objects.equal(conf.get("dfs.namenode.acls.enabled"), "true")) {
        //Attempt extended Acl operations only if its enabled, but don't fail the operation regardless.
        try {
          aclStatus = fs.getAclStatus(file);
        } catch (Exception e) {
          LOG.info("Skipping ACL inheritance: File system for path " + file + " " +
              "does not support ACLs but dfs.namenode.acls.enabled is set to true. ");
          LOG.debug("The details are: " + e, e);
        }
      }this.fileStatus = fileStatus;
      this.aclStatus = aclStatus;
    }

    public FileStatus getFileStatus() {
      return fileStatus;
    }

    public List<AclEntry> getAclEntries() {
      return aclStatus == null ? null : Collections.unmodifiableList(aclStatus.getEntries());
    }

    @VisibleForTesting
    AclStatus getAclStatus() {
      return this.aclStatus;
    }
  }

  public static boolean isPathEncrypted(Configuration conf, URI fsUri, Path path) throws
      IOException {
    Path fullPath;
    if (path.isAbsolute()) {
      fullPath = path;
    } else {
      fullPath = path.getFileSystem(conf).makeQualified(path);
    }
    if(!"hdfs".equalsIgnoreCase(path.toUri().getScheme())) {
      return false;
    }
    try {
      HdfsAdmin hdfsAdmin = new HdfsAdmin(fsUri, conf);
      return (hdfsAdmin.getEncryptionZoneForPath(fullPath) != null);
    } catch (FileNotFoundException fnfe) {
      LOG.debug("Failed to get EZ for non-existent path: "+ fullPath, fnfe);
      return false;
    }
  }

  /**
   * Move a particular file or directory to the trash.
   * @param fs FileSystem to use
   * @param f path of file or directory to move to trash.
   * @param conf
   * @return true if move successful
   * @throws IOException
   */
  public static boolean moveToTrash(FileSystem fs, Path f, Configuration conf, boolean purge)
      throws IOException {
    LOG.debug("deleting  " + f);
    boolean result = false;
    try {
      if(purge) {
        LOG.debug("purge is set to true. Not moving to Trash " + f);
      } else {
        result = Trash.moveToAppropriateTrash(fs, f, conf);
        if (result) {
          LOG.trace("Moved to trash: " + f);
          return true;
        }
      }
    } catch (IOException ioe) {
      // for whatever failure reason including that trash has lower encryption zone
      // retry with force delete
      LOG.warn(ioe.getMessage() + "; Force to delete it.");
    }

    result = fs.delete(f, true);
    if (!result) {
      LOG.error("Failed to delete " + f);
    }
    return result;
  }

  /**
   * Determine if two file systems are the same
   * @param fs1 1st file system
   * @param fs2 2nd file system
   * @return return true if both file system arguments point to same file system
   */
  public static boolean equalsFileSystem(FileSystem fs1, FileSystem fs2) {
    //When file system cache is disabled, you get different FileSystem objects
    // for same file system, so '==' can't be used in such cases
    //FileSystem api doesn't have a .equals() function implemented, so using
    //the uri for comparison. FileSystem already uses uri+Configuration for
    //equality in its CACHE .
    //Once equality has been added in HDFS-9159, we should make use of it
    return fs1.getUri().equals(fs2.getUri());
  }

}
