package com.example.sales.integration;

import com.example.sales.application.dto.RemittanceAdviceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.integration.dsl.support.Transformers;

@Configuration
public class InvoicingFlow {

    @Value("${gmail.username}")
    String gmailUsername;
    @Value("${gmail.password}")
    String gmailPassword;

    @Bean
    IntegrationFlow sendInvoiceFlow() {
        return IntegrationFlows.from("sendInvoiceChannel")
                .handle(Mail.outboundAdapter("smtp.gmail.com")
                        .port(465)
                        .protocol("smtps")
                        .credentials(gmailUsername, gmailPassword)
                        .javaMailProperties(p -> p.put("mail.debug", "false")))
                .get();
    }

    @Bean
    IntegrationFlow inboundMail() {
        return IntegrationFlows.from(Mail.imapInboundAdapter(
                String.format("imaps://%s:%s@imap.gmail.com:993/INBOX", gmailUsername, gmailPassword)
                ).selectorExpression("subject matches '.*Invoice.*'"),
                e -> e.autoStartup(true)
                        .poller(Pollers.fixedDelay(40000))
        ).transform("@remittanceAdviceProcessor.extractRemittanceAdvice(payload)")
                .channel("remittance-processing-channel")
                .get();
    }

    @Bean
    IntegrationFlow inboundHttpGateway() {
        return IntegrationFlows.from(
                Http.inboundChannelAdapter("/api/invoicing/remittances")
                        .requestPayloadType(String.class)
        )
                .channel("remittance-processing-channel")
                .get();
    }

    @Bean
    IntegrationFlow processRemittanceAdvices() {
        return IntegrationFlows.from("remittance-processing-channel")
                .transform(Transformers.fromJson(RemittanceAdviceDTO.class))
                .handle("invoiceService", "closeInvoice")
                .get();
    }
}
