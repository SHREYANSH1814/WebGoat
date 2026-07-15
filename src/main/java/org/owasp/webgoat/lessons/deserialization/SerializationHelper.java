/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializationHelper {

  private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static Object fromString(String s) throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(s);
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data)) {
      @Override
      protected Class<?> resolveClass(java.io.ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        // Allow only safe classes to be deserialized
        if (className.equals("java.lang.String") ||
            className.equals("java.lang.Integer") ||
            className.equals("java.lang.Long") ||
            className.equals("java.lang.Boolean") ||
            className.equals("java.util.ArrayList") ||
            className.equals("java.util.HashMap") ||
            className.equals("java.util.LinkedHashMap") ||
            className.equals("java.util.HashSet") ||
            className.equals("java.util.LinkedHashSet") ||
            className.equals("java.util.Date")) {
          return super.resolveClass(desc);
        } else {
          throw new InvalidClassException("Unauthorized deserialization attempt", className);
        }
      }
    };
    Object o = ois.readObject();
    ois.close();
    return o;
  }

  public static String toString(Serializable o) throws IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(o);
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  public static String show() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeLong(-8699352886133051976L);
    dos.close();
    byte[] longBytes = baos.toByteArray();
    return bytesToHex(longBytes);
  }

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }
}
