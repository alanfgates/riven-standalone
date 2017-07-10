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
public class NotificationEventRequest implements org.apache.thrift.TBase<NotificationEventRequest, NotificationEventRequest._Fields>, java.io.Serializable, Cloneable, Comparable<NotificationEventRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("NotificationEventRequest");

  private static final org.apache.thrift.protocol.TField LAST_EVENT_FIELD_DESC = new org.apache.thrift.protocol.TField("lastEvent", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField MAX_EVENTS_FIELD_DESC = new org.apache.thrift.protocol.TField("maxEvents", org.apache.thrift.protocol.TType.I32, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new NotificationEventRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new NotificationEventRequestTupleSchemeFactory());
  }

  private long lastEvent; // required
  private int maxEvents; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LAST_EVENT((short)1, "lastEvent"),
    MAX_EVENTS((short)2, "maxEvents");

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
        case 1: // LAST_EVENT
          return LAST_EVENT;
        case 2: // MAX_EVENTS
          return MAX_EVENTS;
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
  private static final int __LASTEVENT_ISSET_ID = 0;
  private static final int __MAXEVENTS_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.MAX_EVENTS};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LAST_EVENT, new org.apache.thrift.meta_data.FieldMetaData("lastEvent", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.MAX_EVENTS, new org.apache.thrift.meta_data.FieldMetaData("maxEvents", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(NotificationEventRequest.class, metaDataMap);
  }

  public NotificationEventRequest() {
  }

  public NotificationEventRequest(
    long lastEvent)
  {
    this();
    this.lastEvent = lastEvent;
    setLastEventIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public NotificationEventRequest(NotificationEventRequest other) {
    __isset_bitfield = other.__isset_bitfield;
    this.lastEvent = other.lastEvent;
    this.maxEvents = other.maxEvents;
  }

  public NotificationEventRequest deepCopy() {
    return new NotificationEventRequest(this);
  }

  @Override
  public void clear() {
    setLastEventIsSet(false);
    this.lastEvent = 0;
    setMaxEventsIsSet(false);
    this.maxEvents = 0;
  }

  public long getLastEvent() {
    return this.lastEvent;
  }

  public void setLastEvent(long lastEvent) {
    this.lastEvent = lastEvent;
    setLastEventIsSet(true);
  }

  public void unsetLastEvent() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __LASTEVENT_ISSET_ID);
  }

  /** Returns true if field lastEvent is set (has been assigned a value) and false otherwise */
  public boolean isSetLastEvent() {
    return EncodingUtils.testBit(__isset_bitfield, __LASTEVENT_ISSET_ID);
  }

  public void setLastEventIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __LASTEVENT_ISSET_ID, value);
  }

  public int getMaxEvents() {
    return this.maxEvents;
  }

  public void setMaxEvents(int maxEvents) {
    this.maxEvents = maxEvents;
    setMaxEventsIsSet(true);
  }

  public void unsetMaxEvents() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MAXEVENTS_ISSET_ID);
  }

  /** Returns true if field maxEvents is set (has been assigned a value) and false otherwise */
  public boolean isSetMaxEvents() {
    return EncodingUtils.testBit(__isset_bitfield, __MAXEVENTS_ISSET_ID);
  }

  public void setMaxEventsIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MAXEVENTS_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case LAST_EVENT:
      if (value == null) {
        unsetLastEvent();
      } else {
        setLastEvent((Long)value);
      }
      break;

    case MAX_EVENTS:
      if (value == null) {
        unsetMaxEvents();
      } else {
        setMaxEvents((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case LAST_EVENT:
      return getLastEvent();

    case MAX_EVENTS:
      return getMaxEvents();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case LAST_EVENT:
      return isSetLastEvent();
    case MAX_EVENTS:
      return isSetMaxEvents();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof NotificationEventRequest)
      return this.equals((NotificationEventRequest)that);
    return false;
  }

  public boolean equals(NotificationEventRequest that) {
    if (that == null)
      return false;

    boolean this_present_lastEvent = true;
    boolean that_present_lastEvent = true;
    if (this_present_lastEvent || that_present_lastEvent) {
      if (!(this_present_lastEvent && that_present_lastEvent))
        return false;
      if (this.lastEvent != that.lastEvent)
        return false;
    }

    boolean this_present_maxEvents = true && this.isSetMaxEvents();
    boolean that_present_maxEvents = true && that.isSetMaxEvents();
    if (this_present_maxEvents || that_present_maxEvents) {
      if (!(this_present_maxEvents && that_present_maxEvents))
        return false;
      if (this.maxEvents != that.maxEvents)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_lastEvent = true;
    list.add(present_lastEvent);
    if (present_lastEvent)
      list.add(lastEvent);

    boolean present_maxEvents = true && (isSetMaxEvents());
    list.add(present_maxEvents);
    if (present_maxEvents)
      list.add(maxEvents);

    return list.hashCode();
  }

  @Override
  public int compareTo(NotificationEventRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetLastEvent()).compareTo(other.isSetLastEvent());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLastEvent()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lastEvent, other.lastEvent);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMaxEvents()).compareTo(other.isSetMaxEvents());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMaxEvents()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.maxEvents, other.maxEvents);
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
    StringBuilder sb = new StringBuilder("NotificationEventRequest(");
    boolean first = true;

    sb.append("lastEvent:");
    sb.append(this.lastEvent);
    first = false;
    if (isSetMaxEvents()) {
      if (!first) sb.append(", ");
      sb.append("maxEvents:");
      sb.append(this.maxEvents);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetLastEvent()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'lastEvent' is unset! Struct:" + toString());
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class NotificationEventRequestStandardSchemeFactory implements SchemeFactory {
    public NotificationEventRequestStandardScheme getScheme() {
      return new NotificationEventRequestStandardScheme();
    }
  }

  private static class NotificationEventRequestStandardScheme extends StandardScheme<NotificationEventRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, NotificationEventRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LAST_EVENT
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.lastEvent = iprot.readI64();
              struct.setLastEventIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // MAX_EVENTS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.maxEvents = iprot.readI32();
              struct.setMaxEventsIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, NotificationEventRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(LAST_EVENT_FIELD_DESC);
      oprot.writeI64(struct.lastEvent);
      oprot.writeFieldEnd();
      if (struct.isSetMaxEvents()) {
        oprot.writeFieldBegin(MAX_EVENTS_FIELD_DESC);
        oprot.writeI32(struct.maxEvents);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class NotificationEventRequestTupleSchemeFactory implements SchemeFactory {
    public NotificationEventRequestTupleScheme getScheme() {
      return new NotificationEventRequestTupleScheme();
    }
  }

  private static class NotificationEventRequestTupleScheme extends TupleScheme<NotificationEventRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, NotificationEventRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.lastEvent);
      BitSet optionals = new BitSet();
      if (struct.isSetMaxEvents()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetMaxEvents()) {
        oprot.writeI32(struct.maxEvents);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, NotificationEventRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.lastEvent = iprot.readI64();
      struct.setLastEventIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.maxEvents = iprot.readI32();
        struct.setMaxEventsIsSet(true);
      }
    }
  }

}

