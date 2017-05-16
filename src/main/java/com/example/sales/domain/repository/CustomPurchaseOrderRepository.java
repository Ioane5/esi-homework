package com.example.sales.domain.repository;


import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CustomPurchaseOrderRepository {
    @Query("SELECT po FROM PurchaseOrder po WHERE po.rentalPeriod.startDate = ?1 and po.status = 'ACCEPTED'")
    List<PurchaseOrder> findDispatches(LocalDate date);
}
