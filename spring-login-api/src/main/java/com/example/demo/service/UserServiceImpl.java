package com.example.demo.service;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.model.UserModel;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public User registerUser(UserModel userModel) {
		 User user=new User();
		 user.setEmail(userModel.getEmail());
		 user.setFirstName(userModel.getFirstName());
		 user.setLastName(userModel.getLastName());
		 user.setRole("USER");
		 user.setPassword(passwordEncoder.encode(userModel.getPassword()));
		 userRepository.save(user);
		 return user;
	}
	@Override
	public void sendVerficationTokenForUser(String token, User user) {
		VerificationToken verificationToken=new VerificationToken(user,token);
		
		verificationTokenRepository.save(verificationToken);
	}
	@Override
	public String validateVerificationToken(String token) {
		VerificationToken verificationToken=verificationTokenRepository.findByToken(token);
		if(verificationToken==null) {
			return "invalid";
		}
		User user=verificationToken.getUser();
		Calendar cal=Calendar.getInstance();
		if((verificationToken.getExpirationTime().getTime()-cal.getTime().getTime())<=0) {
			return "expired";
		}
		user.setEnabled(true);
		userRepository.save(user);
		return "valid";
	}
	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
		VerificationToken verificationToken=verificationTokenRepository.findByToken(oldToken);
		System.out.println(verificationToken);
		verificationToken.setToken(UUID.randomUUID().toString());
		verificationTokenRepository.save(verificationToken);
		return verificationToken;
	}
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}
	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken passwordResetToken=new PasswordResetToken(user,token);
		passwordResetTokenRepository.save(passwordResetToken);
	}

}
