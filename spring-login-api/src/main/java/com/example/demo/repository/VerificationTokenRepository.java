package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken , Long> {
		public	VerificationToken findByToken(String token);
}
