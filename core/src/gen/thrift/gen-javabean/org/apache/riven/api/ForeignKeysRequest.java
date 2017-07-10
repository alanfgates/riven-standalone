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
public class ForeignKeysRequest implements org.apache.thrift.TBase<ForeignKeysRequest, ForeignKeysRequest._Fields>, java.io.Serializable, Cloneable, Comparable<ForeignKeysRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ForeignKeysRequest");

  private static final org.apache.thrift.protocol.TField PARENT_DB_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("parent_db_name", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PARENT_TBL_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("parent_tbl_name", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField FOREIGN_DB_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("foreign_db_name", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField FOREIGN_TBL_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("foreign_tbl_name", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ForeignKeysRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ForeignKeysRequestTupleSchemeFactory());
  }

  private String parent_db_name; // required
  private String parent_tbl_name; // required
  private String foreign_db_name; // required
  private String foreign_tbl_name; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PARENT_DB_NAME((short)1, "parent_db_name"),
    PARENT_TBL_NAME((short)2, "parent_tbl_name"),
    FOREIGN_DB_NAME((short)3, "foreign_db_name"),
    FOREIGN_TBL_NAME((short)4, "foreign_tbl_name");

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
        case 1: // PARENT_DB_NAME
          return PARENT_DB_NAME;
        case 2: // PARENT_TBL_NAME
          return PARENT_TBL_NAME;
        case 3: // FOREIGN_DB_NAME
          return FOREIGN_DB_NAME;
        case 4: // FOREIGN_TBL_NAME
          return FOREIGN_TBL_NAME;
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
    tmpMap.put(_Fields.PARENT_DB_NAME, new org.apache.thrift.meta_data.FieldMetaData("parent_db_name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARENT_TBL_NAME, new org.apache.thrift.meta_data.FieldMetaData("parent_tbl_name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.FOREIGN_DB_NAME, new org.apache.thrift.meta_data.FieldMetaData("foreign_db_name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.FOREIGN_TBL_NAME, new org.apache.thrift.meta_data.FieldMetaData("foreign_tbl_name", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ForeignKeysRequest.class, metaDataMap);
  }

  public ForeignKeysRequest() {
  }

  public ForeignKeysRequest(
    String parent_db_name,
    String parent_tbl_name,
    String foreign_db_name,
    String foreign_tbl_name)
  {
    this();
    this.parent_db_name = parent_db_name;
    this.parent_tbl_name = parent_tbl_name;
    this.foreign_db_name = foreign_db_name;
    this.foreign_tbl_name = foreign_tbl_name;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ForeignKeysRequest(ForeignKeysRequest other) {
    if (other.isSetParent_db_name()) {
      this.parent_db_name = other.parent_db_name;
    }
    if (other.isSetParent_tbl_name()) {
      this.parent_tbl_name = other.parent_tbl_name;
    }
    if (other.isSetForeign_db_name()) {
      this.foreign_db_name = other.foreign_db_name;
    }
    if (other.isSetForeign_tbl_name()) {
      this.foreign_tbl_name = other.foreign_tbl_name;
    }
  }

  public ForeignKeysRequest deepCopy() {
    return new ForeignKeysRequest(this);
  }

  @Override
  public void clear() {
    this.parent_db_name = null;
    this.parent_tbl_name = null;
    this.foreign_db_name = null;
    this.foreign_tbl_name = null;
  }

  public String getParent_db_name() {
    return this.parent_db_name;
  }

  public void setParent_db_name(String parent_db_name) {
    this.parent_db_name = parent_db_name;
  }

  public void unsetParent_db_name() {
    this.parent_db_name = null;
  }

  /** Returns true if field parent_db_name is set (has been assigned a value) and false otherwise */
  public boolean isSetParent_db_name() {
    return this.parent_db_name != null;
  }

  public void setParent_db_nameIsSet(boolean value) {
    if (!value) {
      this.parent_db_name = null;
    }
  }

  public String getParent_tbl_name() {
    return this.parent_tbl_name;
  }

  public void setParent_tbl_name(String parent_tbl_name) {
    this.parent_tbl_name = parent_tbl_name;
  }

  public void unsetParent_tbl_name() {
    this.parent_tbl_name = null;
  }

  /** Returns true if field parent_tbl_name is set (has been assigned a value) and false otherwise */
  public boolean isSetParent_tbl_name() {
    return this.parent_tbl_name != null;
  }

  public void setParent_tbl_nameIsSet(boolean value) {
    if (!value) {
      this.parent_tbl_name = null;
    }
  }

  public String getForeign_db_name() {
    return this.foreign_db_name;
  }

  public void setForeign_db_name(String foreign_db_name) {
    this.foreign_db_name = foreign_db_name;
  }

  public void unsetForeign_db_name() {
    this.foreign_db_name = null;
  }

  /** Returns true if field foreign_db_name is set (has been assigned a value) and false otherwise */
  public boolean isSetForeign_db_name() {
    return this.foreign_db_name != null;
  }

  public void setForeign_db_nameIsSet(boolean value) {
    if (!value) {
      this.foreign_db_name = null;
    }
  }

  public String getForeign_tbl_name() {
    return this.foreign_tbl_name;
  }

  public void setForeign_tbl_name(String foreign_tbl_name) {
    this.foreign_tbl_name = foreign_tbl_name;
  }

  public void unsetForeign_tbl_name() {
    this.foreign_tbl_name = null;
  }

  /** Returns true if field foreign_tbl_name is set (has been assigned a value) and false otherwise */
  public boolean isSetForeign_tbl_name() {
    return this.foreign_tbl_name != null;
  }

  public void setForeign_tbl_nameIsSet(boolean value) {
    if (!value) {
      this.foreign_tbl_name = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PARENT_DB_NAME:
      if (value == null) {
        unsetParent_db_name();
      } else {
        setParent_db_name((String)value);
      }
      break;

    case PARENT_TBL_NAME:
      if (value == null) {
        unsetParent_tbl_name();
      } else {
        setParent_tbl_name((String)value);
      }
      break;

    case FOREIGN_DB_NAME:
      if (value == null) {
        unsetForeign_db_name();
      } else {
        setForeign_db_name((String)value);
      }
      break;

    case FOREIGN_TBL_NAME:
      if (value == null) {
        unsetForeign_tbl_name();
      } else {
        setForeign_tbl_name((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PARENT_DB_NAME:
      return getParent_db_name();

    case PARENT_TBL_NAME:
      return getParent_tbl_name();

    case FOREIGN_DB_NAME:
      return getForeign_db_name();

    case FOREIGN_TBL_NAME:
      return getForeign_tbl_name();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PARENT_DB_NAME:
      return isSetParent_db_name();
    case PARENT_TBL_NAME:
      return isSetParent_tbl_name();
    case FOREIGN_DB_NAME:
      return isSetForeign_db_name();
    case FOREIGN_TBL_NAME:
      return isSetForeign_tbl_name();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ForeignKeysRequest)
      return this.equals((ForeignKeysRequest)that);
    return false;
  }

  public boolean equals(ForeignKeysRequest that) {
    if (that == null)
      return false;

    boolean this_present_parent_db_name = true && this.isSetParent_db_name();
    boolean that_present_parent_db_name = true && that.isSetParent_db_name();
    if (this_present_parent_db_name || that_present_parent_db_name) {
      if (!(this_present_parent_db_name && that_present_parent_db_name))
        return false;
      if (!this.parent_db_name.equals(that.parent_db_name))
        return false;
    }

    boolean this_present_parent_tbl_name = true && this.isSetParent_tbl_name();
    boolean that_present_parent_tbl_name = true && that.isSetParent_tbl_name();
    if (this_present_parent_tbl_name || that_present_parent_tbl_name) {
      if (!(this_present_parent_tbl_name && that_present_parent_tbl_name))
        return false;
      if (!this.parent_tbl_name.equals(that.parent_tbl_name))
        return false;
    }

    boolean this_present_foreign_db_name = true && this.isSetForeign_db_name();
    boolean that_present_foreign_db_name = true && that.isSetForeign_db_name();
    if (this_present_foreign_db_name || that_present_foreign_db_name) {
      if (!(this_present_foreign_db_name && that_present_foreign_db_name))
        return false;
      if (!this.foreign_db_name.equals(that.foreign_db_name))
        return false;
    }

    boolean this_present_foreign_tbl_name = true && this.isSetForeign_tbl_name();
    boolean that_present_foreign_tbl_name = true && that.isSetForeign_tbl_name();
    if (this_present_foreign_tbl_name || that_present_foreign_tbl_name) {
      if (!(this_present_foreign_tbl_name && that_present_foreign_tbl_name))
        return false;
      if (!this.foreign_tbl_name.equals(that.foreign_tbl_name))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_parent_db_name = true && (isSetParent_db_name());
    list.add(present_parent_db_name);
    if (present_parent_db_name)
      list.add(parent_db_name);

    boolean present_parent_tbl_name = true && (isSetParent_tbl_name());
    list.add(present_parent_tbl_name);
    if (present_parent_tbl_name)
      list.add(parent_tbl_name);

    boolean present_foreign_db_name = true && (isSetForeign_db_name());
    list.add(present_foreign_db_name);
    if (present_foreign_db_name)
      list.add(foreign_db_name);

    boolean present_foreign_tbl_name = true && (isSetForeign_tbl_name());
    list.add(present_foreign_tbl_name);
    if (present_foreign_tbl_name)
      list.add(foreign_tbl_name);

    return list.hashCode();
  }

  @Override
  public int compareTo(ForeignKeysRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetParent_db_name()).compareTo(other.isSetParent_db_name());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParent_db_name()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.parent_db_name, other.parent_db_name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetParent_tbl_name()).compareTo(other.isSetParent_tbl_name());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParent_tbl_name()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.parent_tbl_name, other.parent_tbl_name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetForeign_db_name()).compareTo(other.isSetForeign_db_name());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetForeign_db_name()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.foreign_db_name, other.foreign_db_name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetForeign_tbl_name()).compareTo(other.isSetForeign_tbl_name());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetForeign_tbl_name()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.foreign_tbl_name, other.foreign_tbl_name);
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
    StringBuilder sb = new StringBuilder("ForeignKeysRequest(");
    boolean first = true;

    sb.append("parent_db_name:");
    if (this.parent_db_name == null) {
      sb.append("null");
    } else {
      sb.append(this.parent_db_name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("parent_tbl_name:");
    if (this.parent_tbl_name == null) {
      sb.append("null");
    } else {
      sb.append(this.parent_tbl_name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("foreign_db_name:");
    if (this.foreign_db_name == null) {
      sb.append("null");
    } else {
      sb.append(this.foreign_db_name);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("foreign_tbl_name:");
    if (this.foreign_tbl_name == null) {
      sb.append("null");
    } else {
      sb.append(this.foreign_tbl_name);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
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

  private static class ForeignKeysRequestStandardSchemeFactory implements SchemeFactory {
    public ForeignKeysRequestStandardScheme getScheme() {
      return new ForeignKeysRequestStandardScheme();
    }
  }

  private static class ForeignKeysRequestStandardScheme extends StandardScheme<ForeignKeysRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ForeignKeysRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PARENT_DB_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.parent_db_name = iprot.readString();
              struct.setParent_db_nameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PARENT_TBL_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.parent_tbl_name = iprot.readString();
              struct.setParent_tbl_nameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // FOREIGN_DB_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.foreign_db_name = iprot.readString();
              struct.setForeign_db_nameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // FOREIGN_TBL_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.foreign_tbl_name = iprot.readString();
              struct.setForeign_tbl_nameIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ForeignKeysRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.parent_db_name != null) {
        oprot.writeFieldBegin(PARENT_DB_NAME_FIELD_DESC);
        oprot.writeString(struct.parent_db_name);
        oprot.writeFieldEnd();
      }
      if (struct.parent_tbl_name != null) {
        oprot.writeFieldBegin(PARENT_TBL_NAME_FIELD_DESC);
        oprot.writeString(struct.parent_tbl_name);
        oprot.writeFieldEnd();
      }
      if (struct.foreign_db_name != null) {
        oprot.writeFieldBegin(FOREIGN_DB_NAME_FIELD_DESC);
        oprot.writeString(struct.foreign_db_name);
        oprot.writeFieldEnd();
      }
      if (struct.foreign_tbl_name != null) {
        oprot.writeFieldBegin(FOREIGN_TBL_NAME_FIELD_DESC);
        oprot.writeString(struct.foreign_tbl_name);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ForeignKeysRequestTupleSchemeFactory implements SchemeFactory {
    public ForeignKeysRequestTupleScheme getScheme() {
      return new ForeignKeysRequestTupleScheme();
    }
  }

  private static class ForeignKeysRequestTupleScheme extends TupleScheme<ForeignKeysRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ForeignKeysRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetParent_db_name()) {
        optionals.set(0);
      }
      if (struct.isSetParent_tbl_name()) {
        optionals.set(1);
      }
      if (struct.isSetForeign_db_name()) {
        optionals.set(2);
      }
      if (struct.isSetForeign_tbl_name()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetParent_db_name()) {
        oprot.writeString(struct.parent_db_name);
      }
      if (struct.isSetParent_tbl_name()) {
        oprot.writeString(struct.parent_tbl_name);
      }
      if (struct.isSetForeign_db_name()) {
        oprot.writeString(struct.foreign_db_name);
      }
      if (struct.isSetForeign_tbl_name()) {
        oprot.writeString(struct.foreign_tbl_name);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ForeignKeysRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.parent_db_name = iprot.readString();
        struct.setParent_db_nameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.parent_tbl_name = iprot.readString();
        struct.setParent_tbl_nameIsSet(true);
      }
      if (incoming.get(2)) {
        struct.foreign_db_name = iprot.readString();
        struct.setForeign_db_nameIsSet(true);
      }
      if (incoming.get(3)) {
        struct.foreign_tbl_name = iprot.readString();
        struct.setForeign_tbl_nameIsSet(true);
      }
    }
  }

}

