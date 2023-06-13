package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.AppUserDetails;
import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.entity.Role;
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
import java.util.stream.Collectors;


@Service
public class AppUserServiceImpl implements AppUserService {

    private static final String USER_NOT_FOUND_MSG = "User not found";
    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository userRepository, AppUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public AppUser findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MSG));
    }

    public void updateUserRole(Long id, String newRole) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            user.setRole(Role.valueOf(newRole));
            userRepository.save(user);
        }, () -> {
            throw new IllegalArgumentException(USER_NOT_FOUND_MSG);
        });
    }


    public void registerUser(AppUserDto userDto) {
        if (emailExists(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
        }
        AppUser user = userMapper.mapToUser(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(true);
        user.setRole(Role.USER);
        if (userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }
        if (userDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
        userRepository.save(user);
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
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MSG));

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

        return userRepository.findByActive(true)
                .stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }


    public void updatePersonalDetails(AppUser existingUser, AppUserDto userDto) {
        AppUser user = userMapper.mapToUser(userDto);
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
            throw new UsernameNotFoundException(USER_NOT_FOUND_MSG);
        }
        AppUserDto userDto = userMapper.mapToUserDto(user);
        return new AppUserDetails(userDto);
    }

}
