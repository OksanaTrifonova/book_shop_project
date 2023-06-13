package com.oksanatrifonova.bookshop.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "original_file_name")
    private String originalFileName;
    @Column(name = "size")
    private Long size;
    @Column(name = "content_type")
    private String contentType;
    @Lob
    private byte[] bytes;
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Book book;
    public Image(String name, String originalFileName, String contentType, long size, byte[] bytes) {
        this.name = name;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.size = size;
        this.bytes = bytes;
    }

}
