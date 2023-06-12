package com.oksanatrifonova.bookshop.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="book")
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name="description",columnDefinition = "text")
    private String description;
    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER,
    mappedBy = "book")
    private Images images;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<BookAuthor> authors = new HashSet<>();
    private boolean active;

    public void addAuthors(Set<BookAuthor> authors) {
        this.authors.addAll(authors);
        authors.forEach(author -> author.getBooks().add(this));
    }

    public Book(String title, Set<BookAuthor> authors, BigDecimal price,String description, Category category) {
        this.title = title;
        this.authors = authors;
        this.price = price;
        this.description=description;
        this.category=category;
    }
    public void addImageToBook(Images image) {
        if (images != null) {
            images.setBook(null);
        }
        if (image != null) {
            image.setBook(this);
        }
        images = image;
    }
}
