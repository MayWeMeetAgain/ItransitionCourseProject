package com.annieryannel.recommendationsapp.service;

import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.repositories.ReviewRepository;
import com.annieryannel.recommendationsapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {return false;}
        userRepository.save(setRegistrationParams(user));
        return true;
    }

    private User setRegistrationParams(User user) {
//        user.addRole(roleRepository.findByRole(Roles.ROLE_USER.getAuthority()));
//        user.addRole(roleRepository.findByRole(Roles.ROLE_ACTIVE.getAuthority()));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //user.setRegistrationDate(new Date());
        return user;
    }

    public String getUsernameById(Long userId) {
        return userRepository.getById(userId).getUsername();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser(){
        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();
        return getUserByUsername(username);
    }

    public Long countLikes(User user) {
        long count = 0;
        List<Review> reviews = reviewRepository.findAllByAuthorId(user.getId());
        for (Review review : reviews)
            count += review.getLikes().size();
        return count;
    }
}
