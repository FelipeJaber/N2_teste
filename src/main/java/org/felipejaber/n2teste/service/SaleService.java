package org.felipejaber.n2teste.service;

import org.felipejaber.n2teste.model.Book;
import org.felipejaber.n2teste.model.Sale;
import org.felipejaber.n2teste.repository.BookRepository;
import org.felipejaber.n2teste.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Sale save(Sale sale) {
        // Validate if book exists and has sufficient quantity
        Optional<Book> bookOpt = bookRepository.findById(sale.getBookId());
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Book not found: " + sale.getBookId());
        }

        Book book = bookOpt.get();
        if (book.getQuantity() == null || book.getQuantity() < sale.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " +
                (book.getQuantity() != null ? book.getQuantity() : 0));
        }

        // Decrease book quantity
        book.setQuantity(book.getQuantity() - sale.getQuantity());
        bookRepository.save(book);

        return saleRepository.save(sale);
    }

    public Sale update(Long id, Sale saleDetails) {
        Optional<Sale> saleOpt = saleRepository.findById(id);
        if (saleOpt.isEmpty()) {
            throw new RuntimeException("Sale not found with id: " + id);
        }

        Sale existingSale = saleOpt.get();
        Integer quantityDifference = 0;

        // Check if quantity changed
        if (saleDetails.getQuantity() != null && !saleDetails.getQuantity().equals(existingSale.getQuantity())) {
            quantityDifference = saleDetails.getQuantity() - existingSale.getQuantity();
        }

        // Validate book and stock if quantity changed
        Optional<Book> bookOpt = bookRepository.findById(existingSale.getBookId());
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("Book not found: " + existingSale.getBookId());
        }

        Book book = bookOpt.get();
        if (quantityDifference > 0) {
            // Increasing quantity - check if we have enough stock to add
            if (book.getQuantity() < quantityDifference) {
                throw new RuntimeException("Insufficient stock to increase quantity. Available: " + book.getQuantity());
            }
            book.setQuantity(book.getQuantity() - quantityDifference);
        } else if (quantityDifference < 0) {
            // Decreasing quantity - return stock
            book.setQuantity(book.getQuantity() - quantityDifference);
        }

        // Update sale details
        if (saleDetails.getQuantity() != null) {
            existingSale.setQuantity(saleDetails.getQuantity());
        }
        if (saleDetails.getUserId() != null) {
            existingSale.setUserId(saleDetails.getUserId());
        }

        bookRepository.save(book);
        return saleRepository.save(existingSale);
    }


    public void deleteById(Long id) {
        Optional<Sale> saleOpt = saleRepository.findById(id);
        if (saleOpt.isPresent()) {
            Sale sale = saleOpt.get();
            // Restore book quantity when sale is deleted
            Optional<Book> bookOpt = bookRepository.findById(sale.getBookId());
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                book.setQuantity(book.getQuantity() + sale.getQuantity());
                bookRepository.save(book);
            }
        }
        saleRepository.deleteById(id);
    }
}
