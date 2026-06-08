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
    // Only allow relative URLs or absolute URLs with allowed hosts
    if (!uri.isAbsolute() || "yourdomain.com".equalsIgnoreCase(uri.getHost())) {
      return new ModelAndView("redirect:" + url);
    } else {
      // Redirect to a safe default page if validation fails
      return new ModelAndView("redirect:/defaultSafePage");
    }
  } catch (URISyntaxException e) {
    // Redirect to a safe default page if URL is malformed
    return new ModelAndView("redirect:/defaultSafePage");
  }
}
}
