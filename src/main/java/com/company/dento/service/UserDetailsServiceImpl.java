package com.company.dento.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.dento.model.business.User;
import com.company.dento.model.type.Role;
import com.company.dento.service.exception.DataDoesNotExistException;

//@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
	@Autowired
    private DataService dataService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user;
		try {
			user = dataService.getUser(username);
		} catch (DataDoesNotExistException e) {
			throw new UsernameNotFoundException("Username not found");
		}

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
