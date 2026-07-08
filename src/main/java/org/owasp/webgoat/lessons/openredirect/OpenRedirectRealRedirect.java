/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides a real 302 redirect for experimentation separate from assignment scoring.
 */
@Controller
public class OpenRedirectRealRedirect {

  @GetMapping("/OpenRedirect/realRedirect")
  public ModelAndView real(@RequestParam("url") String url) {
    try {
      URI uri = new URI(url);
      // Only allow relative URLs or absolute URLs with allowed hosts (example: localhost or same domain)
      if (!uri.isAbsolute() || "localhost".equals(uri.getHost()) || "127.0.0.1".equals(uri.getHost())) {
        return new ModelAndView("redirect:" + url);
      }
    } catch (URISyntaxException e) {
      // Invalid URL syntax, fall through to safe redirect
    }
    // Fallback to a safe default redirect
    return new ModelAndView("redirect:/");
  }
}
