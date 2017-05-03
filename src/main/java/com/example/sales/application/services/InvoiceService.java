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

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class InvoiceService {

    @Value("${gmail.username}")
    String gmailUsername;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    InvoicingGateway invoicingGateway;

    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper mapper;

    public Invoice createInvoice(PurchaseOrder order) {
        Invoice invoice = Invoice.of(IdentifierFactory.nextId(), order);
        invoiceRepository.save(invoice);
        return invoice;
    }

    public void sendInvoice(Invoice invoice, String email) throws Exception {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        String json = mapper.writeValueAsString(
                InvoiceDTO.of(invoice.getId(), invoice.getOrder().getId(), invoice.getOrder().getTotal(), invoice.getOrder().getPaymentSchedule()));

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(rootMessage, true);
        helper.setFrom(gmailUsername + "@gmail.com");
        helper.setTo(email);
        helper.setSubject("Invoice Purchase Order " + invoice.getOrder().getId());
        helper.setText("Dear customer,\n\nPlease find attached the Invoice corresponding to your Purchase Order " +
                invoice.getOrder().getId() + ".\n\nKindly yours,\n\nESI 13 RentIt Team!");

        helper.addAttachment("invoice.json", new ByteArrayDataSource(json, "application/json"));

        invoicingGateway.sendInvoice(rootMessage);
    }

    public Invoice closeInvoice(RemittanceAdviceDTO remittanceAdviceDTO) {
        Invoice invoice = invoiceRepository.findOne(remittanceAdviceDTO.getInvoiceId());

        invoice.closeInvoice(RemittanceAdvice.of(remittanceAdviceDTO.getId(), remittanceAdviceDTO.getNote()));
        invoiceRepository.save(invoice);

        return invoice;
    }
}
