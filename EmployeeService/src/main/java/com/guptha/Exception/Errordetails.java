package com.guptha.Exception;

import java.time.LocalDateTime;

public class Errordetails {

	private LocalDateTime timesatmp;
	private String message;
	private String details;

	public Errordetails() {
		super();
	}

	public Errordetails(LocalDateTime timesatmp, String message, String details) {
		super();
		this.timesatmp = timesatmp;
		this.message = message;
		this.details = details;
	}

	public LocalDateTime getTimesatmp() {
		return timesatmp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public void setTimesatmp(LocalDateTime timesatmp) {
		this.timesatmp = timesatmp;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
