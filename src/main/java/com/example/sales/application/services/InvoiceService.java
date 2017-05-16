package com.example.sales.application.services;

import com.example.common.infrastructure.IdentifierFactory;
import com.example.sales.application.dto.InvoiceDTO;
import com.example.sales.application.dto.RemittanceAdviceDTO;
import com.example.sales.domain.model.Invoice;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.model.RemittanceAdvice;
import com.example.sales.domain.repository.InvoiceRepository;
import com.example.sales.integration.InvoicingGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.List;

@Service
public class InvoiceService {

    @Value("${gmail.username}")
    private String gmailUsername;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoicingGateway invoicingGateway;

    @Autowired
    @Qualifier("objectMapper")
    private ObjectMapper mapper;

    public Invoice createInvoice(PurchaseOrder order) {
        Invoice invoice = Invoice.of(IdentifierFactory.nextId(), order);
        invoiceRepository.save(invoice);
        return invoice;
    }

    public void sendInvoice(Invoice invoice, String email) throws IOException, MessagingException {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        String json = mapper.writeValueAsString(
                InvoiceDTO.of(invoice.getId(), invoice.getOrder().getId(), invoice.getOrder().getTotal(), invoice.getOrder().getPaymentSchedule()));

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(rootMessage, true);
        helper.setFrom(gmailUsername + "@gmail.com");
        helper.setTo(email);
        helper.setSubject("Invoice Purchase Order " + invoice.getOrder().getId());
        helper.setText("Dear customer,\n\nPlease find attached the Invoice corresponding to your Purchase Order " +
                invoice.getOrder().getId() + ".\n\nKindly yours,\n\nESI 11 RentIt Team!");

        helper.addAttachment("invoice.json", new ByteArrayDataSource(json, "application/json"));

        invoicingGateway.sendInvoice(rootMessage);
    }

    public void closeInvoice(RemittanceAdviceDTO remittanceAdviceDTO) {
        RemittanceAdvice advice = RemittanceAdvice.of(remittanceAdviceDTO.getId(), remittanceAdviceDTO.getNote());
        Invoice invoice = invoiceRepository.findOne(remittanceAdviceDTO.getInvoiceId()).close(advice);
        invoiceRepository.save(invoice);
    }

    public void sendPaymentReminders() {
        List<Invoice> unpaidInvoices = invoiceRepository.findUnpaidInvoices();
        unpaidInvoices.forEach(invoice -> {
            try {
                sendInvoice(invoice, invoice.getOrder().getCustomer().getEmail());
            } catch (IOException | MessagingException ex1) {
                throw new RuntimeException(ex1);
            }
        });
    }
}
