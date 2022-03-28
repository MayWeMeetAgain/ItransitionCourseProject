package com.annieryannel.recommendationsapp.controllers;

import com.force.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodNotAllowedException.class)
    public String handleError405(HttpServletRequest request, Exception e, Model model) {
        model.addAttribute("exception", "Method Not Allowed");
        model.addAttribute("errorcode", "405");
        return "error";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleError404(HttpServletRequest request, Exception e, Model model) {
        model.addAttribute("exception", "No such review");
        model.addAttribute("errorcode", "404");
        return "error";
    }

}