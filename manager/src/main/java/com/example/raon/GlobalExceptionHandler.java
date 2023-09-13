package com.example.raon;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {


	// DIVEエーらが発生した時に_errorhandlerに移動
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

		return "/error/_errorhandler_DIVE";
	}
	
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/access-denied"); 
        mav.addObject("error", "Access Denied");
        mav.addObject("message", "You do not have permission to access this resource.");
        return mav;
    }

}