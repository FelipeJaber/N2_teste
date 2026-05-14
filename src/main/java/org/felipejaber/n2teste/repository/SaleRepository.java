package org.felipejaber.n2teste.repository;

import org.felipejaber.n2teste.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
