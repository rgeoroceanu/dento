package com.company.dento.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.dento.model.business.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
	
	public User findByUsername(String username);
	
}
