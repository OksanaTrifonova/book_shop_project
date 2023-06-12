package com.oksanatrifonova.bookshop.repository;

import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmail(String email);

    List<AppUser> findByActive(boolean active);

    long countByRoleAndActive(Role role, boolean active);


}
