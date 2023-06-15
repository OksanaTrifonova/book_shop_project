package com.oksanatrifonova.bookshop.repository;

import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(AppUser user);
}
