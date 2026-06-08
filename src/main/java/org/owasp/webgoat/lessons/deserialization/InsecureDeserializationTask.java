/*
 * SPDX-FileCopyrightText: Copyright © 2014 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.deserialization;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.Base64;
import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AssignmentHints;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AssignmentHints({
  "insecure-deserialization.hints.1",
  "insecure-deserialization.hints.2",
  "insecure-deserialization.hints.3"
})
public class InsecureDeserializationTask implements AssignmentEndpoint {

  @PostMapping("/InsecureDeserialization/task")
  @ResponseBody
  public AttackResult completed(@RequestParam String token) throws IOException {
    String b64token;
    long before;
    long after;
    int delay;

    b64token = token.replace('-', '+').replace('_', '/');

         // REQUIRES IMPORT: javax.crypto.Mac
// REQUIRES IMPORT: javax.crypto.spec.SecretKeySpec
// REQUIRES IMPORT: java.util.Arrays
// REQUIRES IMPORT: java.nio.charset.StandardCharsets

String secretKey = "replace_with_secure_key"; // Use a securely stored key
byte[] decodedBytes = Base64.getDecoder().decode(b64token);

// Separate the HMAC from the data (assuming last 32 bytes are HMAC for HMAC-SHA256)
int hmacLength = 32;
if (decodedBytes.length < hmacLength) {
    throw new SecurityException("Invalid token length");
}
byte[] data = Arrays.copyOfRange(decodedBytes, 0, decodedBytes.length - hmacLength);
byte[] receivedHmac = Arrays.copyOfRange(decodedBytes, decodedBytes.length - hmacLength, decodedBytes.length);

Mac mac = Mac.getInstance("HmacSHA256");
SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
mac.init(keySpec);
byte[] computedHmac = mac.doFinal(data);

if (!Arrays.equals(receivedHmac, computedHmac)) {
    throw new SecurityException("Invalid HMAC - data may have been tampered with");
}

try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
      before = System.currentTimeMillis();
      Object o = ois.readObject();
      if (!(o instanceof VulnerableTaskHolder)) {
        if (o instanceof String) {
          return failed(this).feedback("insecure-deserialization.stringobject").build();
        }
        return failed(this).feedback("insecure-deserialization.wrongobject").build();
      }
      after = System.currentTimeMillis();
    } catch (InvalidClassException e) {
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    } catch (IllegalArgumentException e) {
      return failed(this).feedback("insecure-deserialization.expired").build();
    } catch (Exception e) {
      return failed(this).feedback("insecure-deserialization.invalidversion").build();
    }

    delay = (int) (after - before);
    if (delay > 7000) {
      return failed(this).build();
    }
    if (delay < 3000) {
      return failed(this).build();
    }
    return success(this).build();
  }
}
