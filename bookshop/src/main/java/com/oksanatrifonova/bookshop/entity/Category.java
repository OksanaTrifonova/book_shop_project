package com.oksanatrifonova.bookshop.entity;

import static org.thymeleaf.util.StringUtils.capitalize;

public enum Category {
    DETECTIVE,
    CLASSICS,
    FANTASY,
    ROMANCE,
    THRILLER,
    CHILDREN;

    @Override
    public String toString() {
        return capitalize(name().toLowerCase());
    }
}
