package com.example.sales.application.dto;

import com.example.common.rest.ExtendedResourceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CustomerDTO {
    String id;
    String token;
    String email;
}
