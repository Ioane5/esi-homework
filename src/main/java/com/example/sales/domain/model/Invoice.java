package com.example.sales.domain.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Invoice {
    @Id
    String id;

    @OneToOne
    PurchaseOrder order;

    @OneToOne(cascade = CascadeType.ALL)
    RemittanceAdvice remittanceAdvice;

    public static Invoice of(String id, PurchaseOrder order) {
        Invoice invoice = new Invoice();
        invoice.id = id;
        invoice.order = order;

        return invoice;
    }

    public Invoice close(RemittanceAdvice remittanceAdvice) {
        this.remittanceAdvice = remittanceAdvice;
        return this;
    }
}
