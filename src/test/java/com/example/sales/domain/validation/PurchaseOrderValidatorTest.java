package com.example.sales.domain.validation;

import com.example.MainApplication;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.sales.domain.model.PurchaseOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class PurchaseOrderValidatorTest {

    @Autowired
    private PurchaseOrderValidator validatorUnderTest;

    private PurchaseOrder order;
    private String validId = "id";
    private LocalDate validIssueDate;
    private PlantInventoryEntry validPlant;
    private BusinessPeriod validRentalPeriod;
    private PlantReservation validReservation, invalidReservation;

    @Before
    public void setup() {
        validIssueDate = LocalDate.now();
        validPlant = PlantInventoryEntry.of("plantID", "name", "description", BigDecimal.TEN);
        validRentalPeriod = BusinessPeriod.of(validIssueDate.plusMonths(1), validIssueDate.plusMonths(2));
        order = PurchaseOrder.of(validId, null, validPlant, validIssueDate, validRentalPeriod);
        validReservation = PlantReservation.of("resId", order.getRentalPeriod(), null);
        invalidReservation = PlantReservation.of("resId",
                BusinessPeriod.of(LocalDate.now().minusYears(2), LocalDate.now().minusYears(1)), null);
    }

    @Test
    public void testValidateOnValidOrder() throws Exception {
        assertFalse(getValidationErrors(order).hasErrors());
    }

    @Test
    public void testValidateOnNullId() throws Exception {
        PurchaseOrder order = PurchaseOrder.of(null, null, validPlant, validIssueDate, validRentalPeriod);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnNullPlantInventoryEntry() throws Exception {
        PurchaseOrder order = PurchaseOrder.of(validId, null, null, validIssueDate, validRentalPeriod);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnNullIssueDate() throws Exception {
        PurchaseOrder order = PurchaseOrder.of(validId, null, validPlant, null, validRentalPeriod);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnNullRentalPeriodStart() throws Exception {
        BusinessPeriod rentalPeriod = BusinessPeriod.of(null, LocalDate.now());
        PurchaseOrder order = PurchaseOrder.of(validId, null, validPlant, validIssueDate, rentalPeriod);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnNullRentalPeriodEnd() throws Exception {
        BusinessPeriod rentalPeriod = BusinessPeriod.of(LocalDate.now(), null);
        PurchaseOrder order = PurchaseOrder.of(validId, null, validPlant, validIssueDate, rentalPeriod);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnPastRentalPeriodStart() throws Exception {
        BusinessPeriod rentalPeriod =
                BusinessPeriod.of(LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1));
        PurchaseOrder order = PurchaseOrder.of(validId, null, validPlant, validIssueDate, rentalPeriod);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnPastRentalPeriodEnd() throws Exception {
        BusinessPeriod rentalPeriod =
                BusinessPeriod.of(LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1));
        PurchaseOrder order = PurchaseOrder.of(validId, null, validPlant, validIssueDate, rentalPeriod);
        assertEquals(2, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnValidOpenPO() throws Exception {
        order.addReservationAndAcceptPO(validReservation);
        assertFalse(getValidationErrors(order).hasErrors());
    }

    @Test
    public void testValidateOnValidClosedPO() throws Exception {
        order.addReservationAndAcceptPO(validReservation).cancel();
        assertFalse(getValidationErrors(order).hasErrors());
    }

    @Test
    public void testValidateOnInvalidOpenPO() throws Exception {
        order.addReservationAndAcceptPO(invalidReservation);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateOnInvalidClosedPO() throws Exception {
        order.addReservationAndAcceptPO(invalidReservation).cancel();
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateTotalCostOfOpenPO() throws Exception {
        order.addReservationAndAcceptPO(validReservation);
        order.updateTotalCost(BigDecimal.ZERO);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    @Test
    public void testValidateTotalCostOfClosedPO() throws Exception {
        order.addReservationAndAcceptPO(validReservation).cancel();
        order.updateTotalCost(BigDecimal.ZERO);
        assertEquals(1, getValidationErrors(order).getErrorCount());
    }

    private Errors getValidationErrors(PurchaseOrder order) {
        Errors errors = new BeanPropertyBindingResult(order, "order");
        validatorUnderTest.validate(order, errors);
        return errors;
    }

}