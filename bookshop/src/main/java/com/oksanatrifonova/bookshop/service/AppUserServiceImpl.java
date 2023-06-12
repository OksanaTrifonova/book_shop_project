package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.entity.Role;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.dto.AppUserDetails;
import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.mapper.AppUserMapper;
import com.oksanatrifonova.bookshop.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository userRepository, AppUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public AppUser findUserById(Long id){
       return userRepository.findById(id).orElseThrow(null);
    }

    public void updateUserRole(Long id,String newRole){
        Optional<AppUser> user = userRepository.findById(id);
        if (user.isPresent()){
            AppUser appUser = user.get();
            appUser.setRole(Role.valueOf(newRole));
            userRepository.save(appUser);
        }else{
            throw  new IllegalArgumentException("User not found");
        }
    }


    @Override
    public AppUser registerUser(AppUserDto userDto) {
        if (emailExists(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }
        AppUser user = userMapper.mapToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(true);
        user.setRole(Role.USER);
        if (userDto.getAddress() != null && !userDto.getAddress().isEmpty()) {
            user.setAddress(userDto.getAddress());
        }
        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
        userRepository.save(user);
        return user;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void addUser(AppUserDto userDto) {
        if (emailExists(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }
        AppUser user = userMapper.mapToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(true);
        user.setRole(userDto.getRole());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getRole().equals(Role.ADMIN)) {
            long adminCount = userRepository.countByRoleAndActive(Role.ADMIN, true);
            if (adminCount <= 1) {
                throw new IllegalArgumentException("Cannot delete the last active Admin user");
            }
        }
        user.setActive(false);
        userRepository.save(user);
    }

    public List<AppUserDto> findActiveUsersDto() {
        List<AppUser> activeUsers = userRepository.findByActive(true);
        return userMapper.mapToUserDtoList(activeUsers);
    }


    public void updatePersonalDetails(AppUser existingUser, AppUserDto userDto) {
        AppUser user =userMapper.mapToUser(userDto);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());

    }

    public void saveUser(AppUser user) {
        userRepository.save(user);
    }

    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        AppUserDto userDto = userMapper.mapToUserDto(user);
        return new AppUserDetails(userDto);
    }

}
