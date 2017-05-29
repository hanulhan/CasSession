package com.acentic.cloudservices.session.web;

import com.acentic.cloudservices.user.SystemUserMessage.beans.SystemUserMessageBean;
import java.io.Serializable;
import java.util.Date;

public class SERUserMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String message;
	private Date created;
	
	public SERUserMessage() {
		
	}
	
	public SERUserMessage(SystemUserMessageBean msg) {
		super();
		this.id = msg.getId();
		this.message = msg.getMessage();
		this.created = msg.getCreated();
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


}
