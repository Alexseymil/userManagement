package com.management.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.management.model.UserAccount;
import com.management.repositories.UserCrudRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserCrudRepository userCrudRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, AccessDeniedException {
		UserAccount user = userCrudRepository.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the database"));

		if (!user.isStatus()) {
			throw new AccessDeniedException("User " + username + " was not INACTIVE");
		}
		List<String> roleNames = new ArrayList<>();
		roleNames.add(user.getRole());
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}

		UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(
				user.getUserName(), user.getUserPassword(), grantList);

		return userDetails;
	}

}
