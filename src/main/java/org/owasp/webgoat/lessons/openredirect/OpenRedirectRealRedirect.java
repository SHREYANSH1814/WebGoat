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
      // Only allow relative URLs or absolute URLs with allowed hosts (example: localhost)
      if (!uri.isAbsolute() || "localhost".equalsIgnoreCase(uri.getHost())) {
        return new ModelAndView("redirect:" + url);
      }
    } catch (URISyntaxException e) {
      // Invalid URL, fall through to safe redirect
    }
    // Fallback safe redirect
    return new ModelAndView("redirect:/");
  }
}
