package com.oksanatrifonova.bookshop.repository;

import com.oksanatrifonova.bookshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
