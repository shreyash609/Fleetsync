//package com.fleetsync.auth_service.service;
//
//import com.fleetsync.auth_service.dto.RegisterRequest;
//
//public class Test {
//
//    public void test(){
//
//        RegisterRequest request=new RegisterRequest();
//
//    }
//
//}

package com.fleetsync.auth_service.service;

import com.fleetsync.auth_service.dto.*;
import com.fleetsync.auth_service.entity.User;
import com.fleetsync.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Test {

    @Autowired
    private UserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken("dummy-access")
                .refreshToken("dummy-refresh")
                .tokenType("Bearer")
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
