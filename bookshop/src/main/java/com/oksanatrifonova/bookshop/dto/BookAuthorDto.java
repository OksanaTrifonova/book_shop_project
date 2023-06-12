package com.oksanatrifonova.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthorDto {
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer birthYear;
    private Integer deathYear;
    private boolean active;
}
