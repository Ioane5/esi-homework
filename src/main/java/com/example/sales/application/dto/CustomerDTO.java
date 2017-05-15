package com.example.sales.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class CustomerDTO {
    String id;
    String token;
    String email;
}
