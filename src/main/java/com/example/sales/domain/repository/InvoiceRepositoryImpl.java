package com.example.sales.domain.repository;

import com.example.sales.domain.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class InvoiceRepositoryImpl implements CustomInvoiceRepository {

    @Autowired
    private EntityManager em;

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<Invoice> findUnpaidInvoices() {
        return em.createQuery("SELECT i from Invoice i where i.remittanceAdvice is NULL", Invoice.class).getResultList();
    }
}
