package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import org.springframework.stereotype.Component;


@Component
public class AppUserMapper {

    public AppUserDto mapToUserDto(AppUser user) {
        AppUserDto userDto = new AppUserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setAddress(user.getAddress());
        return userDto;
    }

    public AppUser mapToUser(AppUserDto userDto) {
        AppUser user = new AppUser();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        return user;
    }
}
