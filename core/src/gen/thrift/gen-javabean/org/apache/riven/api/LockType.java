/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.riven.api;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum LockType implements org.apache.thrift.TEnum {
  SHARED_READ(1),
  SHARED_WRITE(2),
  EXCLUSIVE(3);

  private final int value;

  private LockType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static LockType findByValue(int value) { 
    switch (value) {
      case 1:
        return SHARED_READ;
      case 2:
        return SHARED_WRITE;
      case 3:
        return EXCLUSIVE;
      default:
        return null;
    }
  }
}
