package com.tinecommerce.core.security;

import com.tinecommerce.core.customer.model.Customer;
import com.tinecommerce.core.customer.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service("customerUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Resource
    private CustomerRepository userRepository;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Customer user = userRepository.findByUsernameOrEmail(username, username).get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new User(username, user.getPassword(), grantedAuthorities);
    }
}