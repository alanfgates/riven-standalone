/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.riven.api;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)")
public class PartitionsStatsRequest implements org.apache.thrift.TBase<PartitionsStatsRequest, PartitionsStatsRequest._Fields>, java.io.Serializable, Cloneable, Comparable<PartitionsStatsRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("PartitionsStatsRequest");

  private static final org.apache.thrift.protocol.TField DB_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("dbName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField TBL_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("tblName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField COL_NAMES_FIELD_DESC = new org.apache.thrift.protocol.TField("colNames", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField PART_NAMES_FIELD_DESC = new org.apache.thrift.protocol.TField("partNames", org.apache.thrift.protocol.TType.LIST, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new PartitionsStatsRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new PartitionsStatsRequestTupleSchemeFactory());
  }

  private String dbName; // required
  private String tblName; // required
  private List<String> colNames; // required
  private List<String> partNames; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    DB_NAME((short)1, "dbName"),
    TBL_NAME((short)2, "tblName"),
    COL_NAMES((short)3, "colNames"),
    PART_NAMES((short)4, "partNames");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // DB_NAME
          return DB_NAME;
        case 2: // TBL_NAME
          return TBL_NAME;
        case 3: // COL_NAMES
          return COL_NAMES;
        case 4: // PART_NAMES
          return PART_NAMES;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.DB_NAME, new org.apache.thrift.meta_data.FieldMetaData("dbName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TBL_NAME, new org.apache.thrift.meta_data.FieldMetaData("tblName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.COL_NAMES, new org.apache.thrift.meta_data.FieldMetaData("colNames", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.PART_NAMES, new org.apache.thrift.meta_data.FieldMetaData("partNames", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(PartitionsStatsRequest.class, metaDataMap);
  }

  public PartitionsStatsRequest() {
  }

  public PartitionsStatsRequest(
    String dbName,
    String tblName,
    List<String> colNames,
    List<String> partNames)
  {
    this();
    this.dbName = dbName;
    this.tblName = tblName;
    this.colNames = colNames;
    this.partNames = partNames;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public PartitionsStatsRequest(PartitionsStatsRequest other) {
    if (other.isSetDbName()) {
      this.dbName = other.dbName;
    }
    if (other.isSetTblName()) {
      this.tblName = other.tblName;
    }
    if (other.isSetColNames()) {
      List<String> __this__colNames = new ArrayList<String>(other.colNames);
      this.colNames = __this__colNames;
    }
    if (other.isSetPartNames()) {
      List<String> __this__partNames = new ArrayList<String>(other.partNames);
      this.partNames = __this__partNames;
    }
  }

  public PartitionsStatsRequest deepCopy() {
    return new PartitionsStatsRequest(this);
  }

  @Override
  public void clear() {
    this.dbName = null;
    this.tblName = null;
    this.colNames = null;
    this.partNames = null;
  }

  public String getDbName() {
    return this.dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public void unsetDbName() {
    this.dbName = null;
  }

  /** Returns true if field dbName is set (has been assigned a value) and false otherwise */
  public boolean isSetDbName() {
    return this.dbName != null;
  }

  public void setDbNameIsSet(boolean value) {
    if (!value) {
      this.dbName = null;
    }
  }

  public String getTblName() {
    return this.tblName;
  }

  public void setTblName(String tblName) {
    this.tblName = tblName;
  }

  public void unsetTblName() {
    this.tblName = null;
  }

  /** Returns true if field tblName is set (has been assigned a value) and false otherwise */
  public boolean isSetTblName() {
    return this.tblName != null;
  }

  public void setTblNameIsSet(boolean value) {
    if (!value) {
      this.tblName = null;
    }
  }

  public int getColNamesSize() {
    return (this.colNames == null) ? 0 : this.colNames.size();
  }

  public java.util.Iterator<String> getColNamesIterator() {
    return (this.colNames == null) ? null : this.colNames.iterator();
  }

  public void addToColNames(String elem) {
    if (this.colNames == null) {
      this.colNames = new ArrayList<String>();
    }
    this.colNames.add(elem);
  }

  public List<String> getColNames() {
    return this.colNames;
  }

  public void setColNames(List<String> colNames) {
    this.colNames = colNames;
  }

  public void unsetColNames() {
    this.colNames = null;
  }

  /** Returns true if field colNames is set (has been assigned a value) and false otherwise */
  public boolean isSetColNames() {
    return this.colNames != null;
  }

  public void setColNamesIsSet(boolean value) {
    if (!value) {
      this.colNames = null;
    }
  }

  public int getPartNamesSize() {
    return (this.partNames == null) ? 0 : this.partNames.size();
  }

  public java.util.Iterator<String> getPartNamesIterator() {
    return (this.partNames == null) ? null : this.partNames.iterator();
  }

  public void addToPartNames(String elem) {
    if (this.partNames == null) {
      this.partNames = new ArrayList<String>();
    }
    this.partNames.add(elem);
  }

  public List<String> getPartNames() {
    return this.partNames;
  }

  public void setPartNames(List<String> partNames) {
    this.partNames = partNames;
  }

  public void unsetPartNames() {
    this.partNames = null;
  }

  /** Returns true if field partNames is set (has been assigned a value) and false otherwise */
  public boolean isSetPartNames() {
    return this.partNames != null;
  }

  public void setPartNamesIsSet(boolean value) {
    if (!value) {
      this.partNames = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case DB_NAME:
      if (value == null) {
        unsetDbName();
      } else {
        setDbName((String)value);
      }
      break;

    case TBL_NAME:
      if (value == null) {
        unsetTblName();
      } else {
        setTblName((String)value);
      }
      break;

    case COL_NAMES:
      if (value == null) {
        unsetColNames();
      } else {
        setColNames((List<String>)value);
      }
      break;

    case PART_NAMES:
      if (value == null) {
        unsetPartNames();
      } else {
        setPartNames((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case DB_NAME:
      return getDbName();

    case TBL_NAME:
      return getTblName();

    case COL_NAMES:
      return getColNames();

    case PART_NAMES:
      return getPartNames();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case DB_NAME:
      return isSetDbName();
    case TBL_NAME:
      return isSetTblName();
    case COL_NAMES:
      return isSetColNames();
    case PART_NAMES:
      return isSetPartNames();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof PartitionsStatsRequest)
      return this.equals((PartitionsStatsRequest)that);
    return false;
  }

  public boolean equals(PartitionsStatsRequest that) {
    if (that == null)
      return false;

    boolean this_present_dbName = true && this.isSetDbName();
    boolean that_present_dbName = true && that.isSetDbName();
    if (this_present_dbName || that_present_dbName) {
      if (!(this_present_dbName && that_present_dbName))
        return false;
      if (!this.dbName.equals(that.dbName))
        return false;
    }

    boolean this_present_tblName = true && this.isSetTblName();
    boolean that_present_tblName = true && that.isSetTblName();
    if (this_present_tblName || that_present_tblName) {
      if (!(this_present_tblName && that_present_tblName))
        return false;
      if (!this.tblName.equals(that.tblName))
        return false;
    }

    boolean this_present_colNames = true && this.isSetColNames();
    boolean that_present_colNames = true && that.isSetColNames();
    if (this_present_colNames || that_present_colNames) {
      if (!(this_present_colNames && that_present_colNames))
        return false;
      if (!this.colNames.equals(that.colNames))
        return false;
    }

    boolean this_present_partNames = true && this.isSetPartNames();
    boolean that_present_partNames = true && that.isSetPartNames();
    if (this_present_partNames || that_present_partNames) {
      if (!(this_present_partNames && that_present_partNames))
        return false;
      if (!this.partNames.equals(that.partNames))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_dbName = true && (isSetDbName());
    list.add(present_dbName);
    if (present_dbName)
      list.add(dbName);

    boolean present_tblName = true && (isSetTblName());
    list.add(present_tblName);
    if (present_tblName)
      list.add(tblName);

    boolean present_colNames = true && (isSetColNames());
    list.add(present_colNames);
    if (present_colNames)
      list.add(colNames);

    boolean present_partNames = true && (isSetPartNames());
    list.add(present_partNames);
    if (present_partNames)
      list.add(partNames);

    return list.hashCode();
  }

  @Override
  public int compareTo(PartitionsStatsRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetDbName()).compareTo(other.isSetDbName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDbName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.dbName, other.dbName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTblName()).compareTo(other.isSetTblName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTblName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tblName, other.tblName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetColNames()).compareTo(other.isSetColNames());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetColNames()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.colNames, other.colNames);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPartNames()).compareTo(other.isSetPartNames());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartNames()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partNames, other.partNames);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("PartitionsStatsRequest(");
    boolean first = true;

    sb.append("dbName:");
    if (this.dbName == null) {
      sb.append("null");
    } else {
      sb.append(this.dbName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("tblName:");
    if (this.tblName == null) {
      sb.append("null");
    } else {
      sb.append(this.tblName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("colNames:");
    if (this.colNames == null) {
      sb.append("null");
    } else {
      sb.append(this.colNames);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("partNames:");
    if (this.partNames == null) {
      sb.append("null");
    } else {
      sb.append(this.partNames);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetDbName()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'dbName' is unset! Struct:" + toString());
    }

    if (!isSetTblName()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'tblName' is unset! Struct:" + toString());
    }

    if (!isSetColNames()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'colNames' is unset! Struct:" + toString());
    }

    if (!isSetPartNames()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'partNames' is unset! Struct:" + toString());
    }

    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class PartitionsStatsRequestStandardSchemeFactory implements SchemeFactory {
    public PartitionsStatsRequestStandardScheme getScheme() {
      return new PartitionsStatsRequestStandardScheme();
    }
  }

  private static class PartitionsStatsRequestStandardScheme extends StandardScheme<PartitionsStatsRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, PartitionsStatsRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // DB_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.dbName = iprot.readString();
              struct.setDbNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TBL_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.tblName = iprot.readString();
              struct.setTblNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // COL_NAMES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list428 = iprot.readListBegin();
                struct.colNames = new ArrayList<String>(_list428.size);
                String _elem429;
                for (int _i430 = 0; _i430 < _list428.size; ++_i430)
                {
                  _elem429 = iprot.readString();
                  struct.colNames.add(_elem429);
                }
                iprot.readListEnd();
              }
              struct.setColNamesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // PART_NAMES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list431 = iprot.readListBegin();
                struct.partNames = new ArrayList<String>(_list431.size);
                String _elem432;
                for (int _i433 = 0; _i433 < _list431.size; ++_i433)
                {
                  _elem432 = iprot.readString();
                  struct.partNames.add(_elem432);
                }
                iprot.readListEnd();
              }
              struct.setPartNamesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, PartitionsStatsRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.dbName != null) {
        oprot.writeFieldBegin(DB_NAME_FIELD_DESC);
        oprot.writeString(struct.dbName);
        oprot.writeFieldEnd();
      }
      if (struct.tblName != null) {
        oprot.writeFieldBegin(TBL_NAME_FIELD_DESC);
        oprot.writeString(struct.tblName);
        oprot.writeFieldEnd();
      }
      if (struct.colNames != null) {
        oprot.writeFieldBegin(COL_NAMES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.colNames.size()));
          for (String _iter434 : struct.colNames)
          {
            oprot.writeString(_iter434);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.partNames != null) {
        oprot.writeFieldBegin(PART_NAMES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.partNames.size()));
          for (String _iter435 : struct.partNames)
          {
            oprot.writeString(_iter435);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class PartitionsStatsRequestTupleSchemeFactory implements SchemeFactory {
    public PartitionsStatsRequestTupleScheme getScheme() {
      return new PartitionsStatsRequestTupleScheme();
    }
  }

  private static class PartitionsStatsRequestTupleScheme extends TupleScheme<PartitionsStatsRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, PartitionsStatsRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.dbName);
      oprot.writeString(struct.tblName);
      {
        oprot.writeI32(struct.colNames.size());
        for (String _iter436 : struct.colNames)
        {
          oprot.writeString(_iter436);
        }
      }
      {
        oprot.writeI32(struct.partNames.size());
        for (String _iter437 : struct.partNames)
        {
          oprot.writeString(_iter437);
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, PartitionsStatsRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.dbName = iprot.readString();
      struct.setDbNameIsSet(true);
      struct.tblName = iprot.readString();
      struct.setTblNameIsSet(true);
      {
        org.apache.thrift.protocol.TList _list438 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
        struct.colNames = new ArrayList<String>(_list438.size);
        String _elem439;
        for (int _i440 = 0; _i440 < _list438.size; ++_i440)
        {
          _elem439 = iprot.readString();
          struct.colNames.add(_elem439);
        }
      }
      struct.setColNamesIsSet(true);
      {
        org.apache.thrift.protocol.TList _list441 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
        struct.partNames = new ArrayList<String>(_list441.size);
        String _elem442;
        for (int _i443 = 0; _i443 < _list441.size; ++_i443)
        {
          _elem442 = iprot.readString();
          struct.partNames.add(_elem442);
        }
      }
      struct.setPartNamesIsSet(true);
    }
  }

}

