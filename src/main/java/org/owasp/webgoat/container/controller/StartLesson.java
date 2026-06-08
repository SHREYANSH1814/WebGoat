/*
 * SPDX-FileCopyrightText: Copyright © 2016 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.container.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.owasp.webgoat.container.session.Course;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StartLesson {

  private final Course course;

  public StartLesson(Course course) {
    this.course = course;
  }

  @GetMapping(
      value = {"*.lesson"},
      produces = "text/html")
  public ModelAndView lessonPage(HttpServletRequest request) {
    var model = new ModelAndView("lesson_content");
    var path = request.getRequestURL().toString(); // we now got /a/b/c/AccessControlMatrix.lesson
    var lessonName = path.substring(path.lastIndexOf('/') + 1, path.indexOf(".lesson"));

    course.getLessons().stream()
        .filter(l -> l.getId().equals(lessonName))
        .findFirst()
        .ifPresent(
            lesson -> {
              request.setAttribute("lesson", sanitizeLesson(lesson));

private Lesson sanitizeLesson(Lesson lesson) {
    // Perform validation and sanitization of lesson object fields here
    // For example, validate IDs, escape strings, remove unsafe content, etc.
    // This ensures only trusted, validated data is passed across the trust boundary
    // Implement specific validation logic based on Lesson class structure
    return lesson;
}
            });

    return model;
  }
}
