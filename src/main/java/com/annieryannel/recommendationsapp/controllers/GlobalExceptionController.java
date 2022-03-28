package com.annieryannel.recommendationsapp.controllers;

import com.force.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.ErrorMessage;
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

@ControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView auth(ModelMap model) {
        return new ModelAndView("redirect:/login", model);
    }


//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleError404(HttpServletRequest request, Exception e) {
//        ModelAndView mav = new ModelAndView("/error");
//        mav.addObject("exception", e);
//        mav.addObject("errorcode", "404");
//        return mav;
//    }

}