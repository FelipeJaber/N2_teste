package org.felipejaber.n2teste.service;

import org.felipejaber.n2teste.model.Book;
import org.felipejaber.n2teste.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void deveSalvarLivroComSucesso() {
        Book book = new Book("Clean Code", "Robert C. Martin", 89.90, "Tecnologia");

        when(bookRepository.save(book)).thenReturn(book);

        Book resultado = bookService.save(book);

        assertNotNull(resultado);
        assertEquals("Clean Code", resultado.getTitle());
        assertEquals("Robert C. Martin", resultado.getAuthor());
        assertEquals(89.90, resultado.getPrice());
        assertEquals("Tecnologia", resultado.getGenre());

        verify(bookRepository, times(1)).save(book);
    }
}