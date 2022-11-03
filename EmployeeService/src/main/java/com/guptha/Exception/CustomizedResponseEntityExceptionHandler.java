package com.guptha.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Errordetails> handleAllException(Exception ex, WebRequest request) throws Exception {
		Errordetails err = new Errordetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Errordetails>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public final ResponseEntity<Errordetails> handleEMployeeNotFoundException(Exception ex, WebRequest request)
			throws Exception {
		Errordetails err = new Errordetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity<Errordetails>(err, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders header, HttpStatus status, WebRequest request)  {
		Errordetails err = new Errordetails(LocalDateTime.now(),
				"Total Errors: " + ex.getErrorCount() + " First error : " + ex.getFieldError().getDefaultMessage(),
				request.getDescription(false));
		return new ResponseEntity<Object>(err, HttpStatus.BAD_REQUEST);
	}
	

}
