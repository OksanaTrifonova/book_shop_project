package com.oksanatrifonova.bookshop.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String title;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER,
            mappedBy = "book")
    private Image images;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
    private boolean active;


    public Book(String title, Set<Author> authors, BigDecimal price, String description, Category category) {
        this.title = title;
        this.authors = authors;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public void addImageToBook(Image image) {
        if (images != null) {
            images.setBook(null);
        }
        if (image != null) {
            image.setBook(this);
        }
        images = image;
    }
}
