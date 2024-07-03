package com.example.demo.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.demo.entity.User;
import com.example.demo.event.RegistrationCompleteEvent;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

	@Autowired
	private UserService userService;
	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		User user=event.getUser();
		String token=UUID.randomUUID().toString();
		userService.sendVerficationTokenForUser(token, user);
		String url=event.getApplicationUrl()+"/verifyRegistration?token="+token;
		
		log.info("Click the link to verify the account:{}",url);
		
	}
	
	
}
