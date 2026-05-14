package org.felipejaber.n2teste.repository;

import org.felipejaber.n2teste.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
