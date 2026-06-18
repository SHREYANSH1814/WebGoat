/*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializationHelper {

  private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

  public static Object fromString(String s) throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(s);
    /*
 * SPDX-FileCopyrightText: Copyright © 2019 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SerializationHelper {

  private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

  import java.security.MessageDigest;
  import java.security.NoSuchAlgorithmException;
  import javax.crypto.Mac;
  import javax.crypto.spec.SecretKeySpec;

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final byte[] HMAC_KEY = "a-very-secret-key".getBytes();

    public static Object fromString(String s) throws IOException, ClassNotFoundException {
      // The input string is expected to be in the format: base64(hmac) + ":" + base64(serializedObject)
      String[] parts = s.split(":", 2);
      if (parts.length != 2) {
        throw new IOException("Invalid input format");
      }
      byte[] hmacReceived = Base64.getDecoder().decode(parts[0]);
      byte[] data = Base64.getDecoder().decode(parts[1]);

      // Verify HMAC
      Mac mac;
      try {
        mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(HMAC_KEY, HMAC_ALGO));
      } catch (NoSuchAlgorithmException | java.security.InvalidKeyException e) {
        throw new IOException("Failed to initialize HMAC", e);
      }
      byte[] hmacCalculated = mac.doFinal(data);
      if (!MessageDigest.isEqual(hmacReceived, hmacCalculated)) {
        throw new IOException("Data integrity check failed");
      }

      try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
        return ois.readObject();
      }
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
