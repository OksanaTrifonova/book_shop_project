package com.oksanatrifonova.bookshop.entity;
import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "app_user")
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private String address;
    private String phoneNumber;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean active;
}
