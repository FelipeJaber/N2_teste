package org.felipejaber.n2teste.service;

import org.felipejaber.n2teste.model.Book;
import org.felipejaber.n2teste.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book update(Long id, Book bookDetails) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book existingBook = bookOpt.get();
            if (bookDetails.getTitle() != null) {
                existingBook.setTitle(bookDetails.getTitle());
            }
            if (bookDetails.getAuthor() != null) {
                existingBook.setAuthor(bookDetails.getAuthor());
            }
            if (bookDetails.getGenre() != null) {
                existingBook.setGenre(bookDetails.getGenre());
            }
            if (bookDetails.getPrice() != null) {
                existingBook.setPrice(bookDetails.getPrice());
            }
            if (bookDetails.getQuantity() != null) {
                existingBook.setQuantity(bookDetails.getQuantity());
            }
            return bookRepository.save(existingBook);
        }
        throw new RuntimeException("Book not found with id: " + id);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
