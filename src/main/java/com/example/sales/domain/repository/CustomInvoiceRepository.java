package com.example.sales.domain.repository;

import com.example.sales.domain.model.Invoice;

import java.util.List;

public interface CustomInvoiceRepository {
    List<Invoice> findUnpaidInvoices();
}
