package com.annieryannel.recommendationsapp.service;

import com.annieryannel.recommendationsapp.DTO.UserDto;
import com.annieryannel.recommendationsapp.mappers.UserMapper;
import com.annieryannel.recommendationsapp.models.Review;
import com.annieryannel.recommendationsapp.models.User;
import com.annieryannel.recommendationsapp.repositories.ReviewRepository;
import com.annieryannel.recommendationsapp.repositories.RoleRepository;
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
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    final UserRepository userRepository;

    final UserMapper userMapper;

    final RoleRepository roleRepository;

    final ReviewRepository reviewRepository;

    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, ReviewRepository reviewRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.reviewRepository = reviewRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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
        user.addRole(roleRepository.findByRole("ROLE_USER"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }

    public String getUsernameById(Long userId) {
        return userRepository.getById(userId).getUsername();
    }

    public UserDto getUserDtoByUsername(String username) {
        return userMapper.toDto(userRepository.findByUsername(username));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDto> loadAllUsers() {
        return allUsersToDto(userRepository.findAll());
    }

    public List<UserDto> allUsersToDto(List<User> users) {
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }
}
