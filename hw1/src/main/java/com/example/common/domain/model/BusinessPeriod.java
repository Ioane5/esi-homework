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

    public int getNumberOfWorkingDays() {
        int numberOfWorkingDays = 0;
        for (LocalDate date = this.startDate;
             date.isBefore(this.endDate);
             date = date.plusDays(1)) {
            // All the logic to compute the number of working days
            numberOfWorkingDays++;
        }
        return numberOfWorkingDays;
    }
}
