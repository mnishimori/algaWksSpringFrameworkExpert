package br.com.digitalzyon.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.digitalzyon.service.exception.ValidacaoException;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<String> handleValidacaoException(ValidacaoException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
}
