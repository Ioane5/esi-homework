package com.example.sales.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledPaymentNotifier {

    @Autowired
    private InvoiceService invoiceService;

    @Scheduled(cron = "0 0 12 * * *")
    public void sendPaymentReminders() {
        System.out.println("Sending payment reminders");
        invoiceService.sendPaymentReminders();
    }
}
