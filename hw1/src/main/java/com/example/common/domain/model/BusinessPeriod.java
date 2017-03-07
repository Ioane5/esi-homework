package com.example.common.domain.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDate;

/**
 * Created by ioane5 on 2/20/17.
 */
@Embeddable
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class BusinessPeriod {
    LocalDate startDate;
    LocalDate endDate;
}
