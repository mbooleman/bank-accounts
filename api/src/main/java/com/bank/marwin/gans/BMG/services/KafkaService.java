package com.bank.marwin.gans.BMG.services;

import com.bank.marwin.gans.avro.model.TransactionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, TransactionMessage> kafkatransactionTemplate;

    public void sendMessage(String msg) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("bank-marwin-gans", msg);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println(
                        "Sent message=[" + msg + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + msg + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendTransactionMessage(TransactionMessage msg) {
        CompletableFuture<SendResult<String, TransactionMessage>> future = kafkatransactionTemplate.send("transactions",
                msg);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + msg.toString() + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + msg.toString() + "] due to : " + ex.getMessage());
                throw new KafkaException("Unable to send message=[" + msg + "] due to : " + ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "bank-marwin-gans", groupId = "bmg-spring")
    public void listenBMGSpring(String message) {
        System.out.println("Received Message in group bmg-spring: " + message);
    }

    @KafkaListener(topics = "transactions", groupId = "bmg-spring-transaction", containerFactory = "kafkaListenerTransactionContainerFactory")
    public void listenTransactions(TransactionMessage message) {
        System.out.println("Received Message in group bmg-spring-transaction: " + message);
        System.out.println("THAT IS THIS ONE");
    }

}
