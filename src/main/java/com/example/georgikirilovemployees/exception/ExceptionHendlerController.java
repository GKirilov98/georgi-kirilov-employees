package com.example.georgikirilovemployees.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Project: georgi-kirilov-employees
 * Created by: GKirilov
 * On: 12/5/2021
 */
@RestControllerAdvice
public class ExceptionHendlerController {
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ModelAndView getSuperheroesUnavailable(Exception ex) {
        return new ModelAndView("index", "error", ex.getMessage());
    }
}
