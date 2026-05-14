package org.felipejaber.n2teste.service;

import org.felipejaber.n2teste.model.Book;
import org.felipejaber.n2teste.model.Sale;
import org.felipejaber.n2teste.repository.BookRepository;
import org.felipejaber.n2teste.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    @Transactional
    public Sale save(Sale sale) {
        // Validate if book exists and has sufficient quantity
        Optional<Book> bookOpt = bookRepository.findById(sale.getBookId());
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Book not found: " + sale.getBookId());
        }

        Book book = bookOpt.get();
        int currentQuantity = book.getQuantity() != null ? book.getQuantity() : 0;
        if (currentQuantity < sale.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + currentQuantity);
        }

        // Decrease book quantity
        book.setQuantity(currentQuantity - sale.getQuantity());
        bookRepository.save(book);

        return saleRepository.save(sale);
    }

    @Transactional
    public Sale update(Long id, Sale saleDetails) {
        Optional<Sale> saleOpt = saleRepository.findById(id);
        if (saleOpt.isEmpty()) {
            throw new RuntimeException("Sale not found with id: " + id);
        }

        Sale existingSale = saleOpt.get();
        Long oldBookId = existingSale.getBookId();
        Integer oldQuantity = existingSale.getQuantity();

        // Update sale details
        if (saleDetails.getQuantity() != null) {
            existingSale.setQuantity(saleDetails.getQuantity());
        }
        if (saleDetails.getUserId() != null) {
            existingSale.setUserId(saleDetails.getUserId());
        }
        if (saleDetails.getBookId() != null) {
            existingSale.setBookId(saleDetails.getBookId());
        }

        // Adjust stock if book or quantity changed
        if (!oldBookId.equals(existingSale.getBookId()) || !oldQuantity.equals(existingSale.getQuantity())) {
            // Restore stock to old book
            Optional<Book> oldBookOpt = bookRepository.findById(oldBookId);
            if (oldBookOpt.isPresent()) {
                Book oldBook = oldBookOpt.get();
                int oldCurrent = oldBook.getQuantity() != null ? oldBook.getQuantity() : 0;
                oldBook.setQuantity(oldCurrent + oldQuantity);
                bookRepository.save(oldBook);
            }

            // Decrease stock from new book
            Optional<Book> newBookOpt = bookRepository.findById(existingSale.getBookId());
            if (newBookOpt.isPresent()) {
                Book newBook = newBookOpt.get();
                int newCurrent = newBook.getQuantity() != null ? newBook.getQuantity() : 0;
                if (newCurrent < existingSale.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for updated sale. Available: " + newCurrent);
                }
                newBook.setQuantity(newCurrent - existingSale.getQuantity());
                bookRepository.save(newBook);
            } else {
                throw new RuntimeException("New book not found: " + existingSale.getBookId());
            }
        }

        return saleRepository.save(existingSale);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Sale> saleOpt = saleRepository.findById(id);
        if (saleOpt.isPresent()) {
            Sale sale = saleOpt.get();
            // Restore book quantity when sale is deleted
            Optional<Book> bookOpt = bookRepository.findById(sale.getBookId());
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                int currentQuantity = book.getQuantity() != null ? book.getQuantity() : 0;
                book.setQuantity(currentQuantity + sale.getQuantity());
                bookRepository.save(book);
            }
        }
        saleRepository.deleteById(id);
    }
}
