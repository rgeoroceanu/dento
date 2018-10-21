package com.company.dento.dao;

import com.company.dento.model.business.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends PageableRepository<User, Long> {
	
	Optional<User> findByUsername(final String username);
	
}
