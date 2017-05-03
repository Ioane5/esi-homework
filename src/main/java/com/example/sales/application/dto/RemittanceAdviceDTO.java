package com.example.sales.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class RemittanceAdviceDTO {
    String id;
    String invoiceId;
    String note;
}
