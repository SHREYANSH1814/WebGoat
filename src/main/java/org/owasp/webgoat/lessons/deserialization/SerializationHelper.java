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
    // REQUIRES IMPORT: java.io.ObjectInputStream
// REQUIRES IMPORT: java.io.ByteArrayInputStream
// REQUIRES IMPORT: java.io.IOException
// REQUIRES IMPORT: java.security.MessageDigest
// REQUIRES IMPORT: java.security.NoSuchAlgorithmException
// REQUIRES IMPORT: javax.crypto.Mac
// REQUIRES IMPORT: javax.crypto.spec.SecretKeySpec

// Example secret key for HMAC verification - in real use, securely manage this key
private static final byte[] HMAC_SECRET_KEY = "replace_with_secure_key".getBytes();

private ObjectInputStream createVerifiedObjectInputStream(byte[] data, byte[] expectedHmac) throws IOException, SecurityException {
    // Verify HMAC before deserialization
    try {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(HMAC_SECRET_KEY, "HmacSHA256");
        mac.init(keySpec);
        byte[] actualHmac = mac.doFinal(data);
        if (!MessageDigest.isEqual(actualHmac, expectedHmac)) {
            throw new SecurityException("Data integrity check failed: HMAC mismatch");
        }
    } catch (NoSuchAlgorithmException | java.security.InvalidKeyException e) {
        throw new IOException("Failed to verify data integrity", e);
    }
    return new ObjectInputStream(new ByteArrayInputStream(data));
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
