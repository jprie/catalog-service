package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExists() {
        var bookIsbn = "1234567890";
        var book = Book.of(bookIsbn, "Title", "Author", 12.90, "Publisher");
        jdbcAggregateTemplate.insert(book);

        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(bookIsbn);
    }

    @Test
    void whenCreateBookNotAuthenticatedThenNoAuditMetadata() {

        var bookIsbn = "1234567890";
        var book = Book.of(bookIsbn, "Title", "Author", 12.90, "Publisher");

        var createdBook = bookRepository.save(book);
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.createdBy()).isNull();
    }

    @Test
    @WithMockUser("john")
    void whenCreateBookAuthenticatedThenAuditMetadata() {

        var bookIsbn = "1234567890";
        var book = Book.of(bookIsbn, "Title", "Author", 12.90, "Publisher");

        var createdBook = bookRepository.save(book);
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.createdBy()).isEqualTo("john");
    }


}
