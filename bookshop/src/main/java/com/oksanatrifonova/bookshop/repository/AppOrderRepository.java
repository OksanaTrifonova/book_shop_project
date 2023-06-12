package com.oksanatrifonova.bookshop.repository;

import com.oksanatrifonova.bookshop.entity.BookOrder;
import com.oksanatrifonova.bookshop.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppOrderRepository extends JpaRepository<BookOrder, Long> {
    List<BookOrder> findByUser(AppUser user);
}
