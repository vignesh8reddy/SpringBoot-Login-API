package com.example.demo.entity.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import com.example.demo.entity.User;
import com.example.demo.event.RegistrationCompleteEvent;
import com.example.demo.service.UserService;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
	
	@Autowired
	private UserService userService;

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User user=event.getUser();
		String token=UUID.randomUUID().toString();
		userService.sendVerficationTokenForUser(token,user);
	}
	
	
}
