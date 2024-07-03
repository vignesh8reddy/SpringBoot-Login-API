package com.example.demo.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.event.RegistrationCompleteEvent;
import com.example.demo.model.PasswordModel;
import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@PostMapping("/register")
	public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest httpServletRequest) {
		User user = userService.registerUser(userModel);
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(httpServletRequest)));
		return "Success";
	}

	@GetMapping("/register/verifyRegistration")
	public String verifyRegistration(@RequestParam("token") String token) {
		String result = userService.validateVerificationToken(token);
		if (result.equalsIgnoreCase("valid")) {
			return "User Verified Successfully";
		}
		return "Bad User";
	}

	@GetMapping("/resendVerifyToken")
	public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
		VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
		User user = verificationToken.getUser();
		resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
		return "Verification Link Sent";
	}
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody PasswordModel passwordModel,HttpServletRequest request) {
		User user=userService.findUserByEmail(passwordModel.getEmail());
		String url="";
		if(user!=null) {
			String token=UUID.randomUUID().toString();
			userService.createPasswordResetTokenForUser(user,token);
			url=passwordResetTokenMail(user,applicationUrl(request),token);
		}
		return url;
	}
	private String passwordResetTokenMail(User user, String applicationUrl,String token) {
		String url = applicationUrl + "/savePassword?token=" + token;

		log.info("Click the link to Reset your password:{}", url);
		return url;
	}

	private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
		String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();

		log.info("Click the link to verify the account:{}", url);

	}

	private String applicationUrl(HttpServletRequest httpServletRequest) {
		return "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort()
				+ httpServletRequest.getServletPath();
	}

}
