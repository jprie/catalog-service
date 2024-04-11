package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws IOException {
        var now = Instant.now();
        var book = new Book(5L, "1234567890", "Title", "Author", 9.90, "Publisher", now, now, "isabelle", "isabelle", 0);
        var jsonContent = json.write(book);


        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(book.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.price());
        assertThat(jsonContent).extractingJsonPathStringValue("@.publisher").isEqualTo(book.publisher());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate").isEqualTo(book.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate").isEqualTo(book.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdBy").isEqualTo(book.createdBy());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedBy").isEqualTo(book.lastModifiedBy());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version").isEqualTo(book.version());
    }

    @Test
    void testDeserialize() throws IOException {
        var instant = Instant.parse("2021-09-07T22:50:37.135029Z");
        var content = """
                {
                  "id" : 5,
                  "isbn" : "1234567890",
                  "title" : "Title",
                  "author" : "Author",
                  "price" : 9.90,
                  "publisher" : "Publisher",
                  "createdDate": "2021-09-07T22:50:37.135029Z",
                  "lastModifiedDate": "2021-09-07T22:50:37.135029Z",
                  "createdBy": "isabelle",
                  "lastModifiedBy": "isabelle",
                  "version" : 0
                }
                """;

        assertThat(json.parse(content)).usingRecursiveComparison()
                .isEqualTo(new Book(5L, "1234567890", "Title", "Author", 9.90, "Publisher", instant, instant, "isabelle", "isabelle", 0));
    }
}
