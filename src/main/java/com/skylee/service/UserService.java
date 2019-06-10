package com.skylee.service;

import com.skylee.config.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*import User;*/


/**
 * 默认登录控制
 */
@Service
public class UserService implements UserDetailsService {

  @Autowired
  WebSocketConfig webSocketConfig;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String onlinuser = webSocketConfig.onlineUser.toString();
    if (onlinuser.contains(username)||"机器人".equals(username)) {
      throw new UsernameNotFoundException("用户已存在");
    }
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("USER"));
    User user=new User(username,"",authorities);
    return user;
	}

}
