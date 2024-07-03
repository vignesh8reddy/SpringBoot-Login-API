package com.example.demo.event;

import org.springframework.context.ApplicationEvent;

import com.example.demo.entity.User;

import lombok.Data;

@Data
public class RegistrationCompleteEvent extends ApplicationEvent {
	
	private User user;
	private String applicationUrl;
	public RegistrationCompleteEvent(User user,String applicationUrl) {
		super(user);
		this.user=user;
		this.applicationUrl=applicationUrl;
	}

}
