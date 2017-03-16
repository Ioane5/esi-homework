package com.example.common.domain.validation;

import com.example.common.domain.model.BusinessPeriod;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BusinessPeriodValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return BusinessPeriod.class.equals(clazz);
    }

    public void validate(Object object, Errors errors) {
        BusinessPeriod period = (BusinessPeriod) object;
        if (period.getStartDate() != null && period.getEndDate() != null) {
            if (period.getStartDate().isAfter(period.getEndDate())) {
                errors.reject("Start date should be before end date");
            }
        }
        if (period.getStartDate() == null) {
            errors.rejectValue("StartDate", "Start date cannot be null");
        }

        if (period.getEndDate() == null) {
            errors.rejectValue("EndDate", "End date cannot be null");
        }
    }
}
