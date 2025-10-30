package com.jhj.home.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.home.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);
}
