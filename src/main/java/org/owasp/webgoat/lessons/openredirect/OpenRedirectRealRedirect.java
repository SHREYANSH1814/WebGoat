/*
 * SPDX-FileCopyrightText: Copyright © 2025 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.openredirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Provides a real 302 redirect for experimentation separate from assignment scoring.
 */
@Controller
public class OpenRedirectRealRedirect {

  // REQUIRES IMPORT: java.net.URI
// REQUIRES IMPORT: java.net.URISyntaxException
@GetMapping("/OpenRedirect/realRedirect")
public ModelAndView real(@RequestParam("url") String url) {
  try {
    URI uri = new URI(url);
    // Allow only relative URLs or absolute URLs with allowed hosts (example: same host)
    if (uri.isAbsolute()) {
      // Reject absolute URLs to prevent open redirect
      throw new IllegalArgumentException("Absolute URLs are not allowed");
    }
    return new ModelAndView("redirect:" + url);
  } catch (URISyntaxException | IllegalArgumentException e) {
    // Handle invalid or disallowed URLs by redirecting to a safe default page
    return new ModelAndView("redirect:/defaultSafePage");
  }
}
}
