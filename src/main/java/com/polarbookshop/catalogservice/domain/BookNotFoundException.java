package com.polarbookshop.catalogservice.domain;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("Book with ISBN " + isbn + " was not found.");
    }
}
