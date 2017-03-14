package com.example.sales.domain.validation;


import com.example.common.domain.model.BusinessPeriod;
import com.example.common.domain.validation.BusinessPeriodValidator;
import com.example.inventory.domain.model.PlantReservation;
import com.example.sales.domain.model.POStatus;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PurchaseOrderValidator implements Validator {

    @Autowired
    private BusinessPeriodValidator periodValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return PurchaseOrder.class.equals(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {
        PurchaseOrder po = (PurchaseOrder) object;
        validateMandatoryFields(po, errors);
        errors.pushNestedPath("reservation");
        validateReservation(po, errors);
        errors.popNestedPath();
        errors.pushNestedPath("rentalPeriod");
        validateRentalPeriod(po.getRentalPeriod(), errors);
        errors.popNestedPath();
        validateTotalCost(po, errors);
    }

    private void validateReservation(PurchaseOrder po, Errors errors) {
        if (po.getStatus().equals(POStatus.PENDING)) return;
        PlantReservation reservation = po.getReservation();

        if (reservation.getId() == null) {
            errors.rejectValue("id", "Reservation cannot have null id");
        }

        if (!reservation.getSchedule().equals(po.getRentalPeriod())) {
            errors.rejectValue("schedule", "Rental period and plant reservation schedule mismatch");
        }
    }

    private void validateMandatoryFields(PurchaseOrder po, Errors errors) {
        if (po.getId() == null) {
            errors.rejectValue("id", "Purchase Order id cannot be null");
        }

        if (po.getPlant() == null) {
            errors.rejectValue("plant", "Plant cannot be null");
        } else if (po.getPlant().getId() == null) {
            errors.rejectValue("plantId", "Plant id cannot be null");
        }

        if (po.getIssueDate() == null) {
            errors.rejectValue("issueDate", "Issue date cannot be null");
        }
    }

    private void validateRentalPeriod(BusinessPeriod rentalPeriod, Errors errors) {
        ValidationUtils.invokeValidator(periodValidator, rentalPeriod, errors);
        try {
            if (LocalDate.now().isAfter(rentalPeriod.getStartDate())) {
                errors.rejectValue("StartDate", "Start date cannot be in past");
            }
        } catch (NullPointerException ignored) { }

        try {
            if (LocalDate.now().isAfter(rentalPeriod.getEndDate())) {
                errors.rejectValue("EndDate", "End date cannot be in past");
            }
        } catch (NullPointerException ignored) { }
    }

    private void validateTotalCost(PurchaseOrder po, Errors errors) {
        if (po.getStatus().equals(POStatus.PENDING)) return;
        if (po.getTotal().compareTo(BigDecimal.ZERO) != 1) {
            errors.rejectValue("total", "Total should be positive");
        }
    }
}