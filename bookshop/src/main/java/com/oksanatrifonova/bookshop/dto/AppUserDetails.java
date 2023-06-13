package com.oksanatrifonova.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
public class AppUserDetails implements UserDetails {

    private transient AppUserDto userDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRole = userDto.getRole().name();
        String role = switch (userRole) {
            case "USER" -> "ROLE_USER";
            case "ADMIN" -> "ROLE_ADMIN";
            case "MANAGER" -> "ROLE_MANAGER";
            default -> null;
        };

        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
