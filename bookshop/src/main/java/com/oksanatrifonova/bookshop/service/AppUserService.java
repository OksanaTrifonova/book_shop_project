package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {
    void registerUser(AppUserDto userDto);

    AppUser findUserByEmail(String email);

    void addUser(AppUserDto userDto);

    boolean emailExists(String email);

    void updateUserRole(Long id, String newRole);

    void deleteUser(Long id);

    List<AppUserDto> findActiveUsersDto();

    void updatePersonalDetails(AppUser existingUser, AppUserDto userDto);
}
