package com.oksanatrifonova.bookshop.repository;

import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.entity.BookAuthor;
import com.oksanatrifonova.bookshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findByActive(boolean active);
    List<Book> findBooksByCategoryAndActive(Category category, boolean active);
    List<Book> findBooksByAuthorsAndActive(BookAuthor author, boolean active);

}
