package com.example.raon;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

	// DIVEエーらが発生した時に_errorhandlerに移動
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		
		//社員 - ラオンユーザー間のデータ整合性のため
		return "_errorhandler_DIVE";
	}


}