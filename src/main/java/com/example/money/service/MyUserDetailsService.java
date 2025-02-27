package com.example.money.service;

import com.example.money.entity.UserPrincipal;
import com.example.money.entity.Users;
import com.example.money.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user1 = userRepository.findByEmail(email);

        if(user1 == null){
            throw new UsernameNotFoundException("User not Found!!");
        }

        return new UserPrincipal(user1);
    }
}
