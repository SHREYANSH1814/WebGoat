/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.cryptography;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Random;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncodingAssignment implements AssignmentEndpoint {

  public static String getBasicAuth(String username, String password) {
    return Base64.getEncoder().encodeToString(username.concat(":").concat(password).getBytes());
  }

  @GetMapping(path = "/crypto/encoding/basic", produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public String getBasicAuth(HttpServletRequest request) {

    String basicAuth = (String) request.getSession().getAttribute("basicAuth");
    String username = request.getUserPrincipal().getName();
    if (basicAuth == null) {
      String password =
          HashingAssignment.SECRETS[new Random().nextInt(HashingAssignment.SECRETS.length)];
      basicAuth = getBasicAuth(username, password);
      request.getSession().setAttribute("basicAuth", sanitizeBasicAuth(basicAuth));

private String sanitizeBasicAuth(String basicAuth) {
    if (basicAuth == null) {
        return null;
    }
    // Basic validation: ensure the string matches expected Basic Auth format "Basic base64credentials"
    if (!basicAuth.startsWith("Basic ")) {
        throw new IllegalArgumentException("Invalid basicAuth format");
    }
    String base64Credentials = basicAuth.substring(6);
    // Validate base64 encoding
    try {
        java.util.Base64.getDecoder().decode(base64Credentials);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid base64 encoding in basicAuth");
    }
    return basicAuth;
}
    }
    return "Authorization: Basic ".concat(basicAuth);
  }

  @PostMapping("/crypto/encoding/basic-auth")
  @ResponseBody
  public AttackResult completed(
      HttpServletRequest request,
      @RequestParam String answer_user,
      @RequestParam String answer_pwd) {
    String basicAuth = (String) request.getSession().getAttribute("basicAuth");
    if (basicAuth != null
        && answer_user != null
        && answer_pwd != null
        && basicAuth.equals(getBasicAuth(answer_user, answer_pwd))) {
      return success(this).feedback("crypto-encoding.success").build();
    } else {
      return failed(this).feedback("crypto-encoding.empty").build();
    }
  }
}
