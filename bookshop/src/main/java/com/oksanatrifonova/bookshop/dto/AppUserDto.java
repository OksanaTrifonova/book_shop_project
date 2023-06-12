package com.oksanatrifonova.bookshop.dto;

import com.oksanatrifonova.bookshop.entity.Role;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String email;
    @Nullable
    private String address;
    @Nullable
    private String phoneNumber;
    @NotNull
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean active;

}
