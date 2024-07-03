package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.model.UserModel;

public interface UserService {
	public User registerUser(UserModel userModel);
	void sendVerficationTokenForUser(String token,User user);
	String validateVerificationToken(String token);
	VerificationToken generateNewVerificationToken(String oldToken);
	User findUserByEmail(String email);
	void createPasswordResetTokenForUser(User user, String token);
}
