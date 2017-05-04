package com.example.sales.domain.repository;


import com.example.sales.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String>, CustomInvoiceRepository {
}
