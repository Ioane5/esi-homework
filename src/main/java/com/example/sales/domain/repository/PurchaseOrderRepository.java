package com.example.sales.domain.repository;

import com.example.sales.domain.model.Customer;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String>, CustomPurchaseOrderRepository {

    PurchaseOrder findByIdAndCustomer(String id, Customer customer);

    List<PurchaseOrder> findAllByCustomer(Customer customer);
}
