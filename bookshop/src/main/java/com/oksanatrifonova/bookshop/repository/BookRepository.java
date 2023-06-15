package com.oksanatrifonova.bookshop.repository;

import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.entity.Author;
import com.oksanatrifonova.bookshop.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByActive(boolean active, Pageable pageable);

    Page<Book> findBooksByCategoryAndActive(Category category, boolean active, Pageable pageable);

    Page<Book> findBooksByAuthorsAndActive(Author author, boolean active, Pageable pageable);
    List<Book> findAllByAuthors(Author author);

}
