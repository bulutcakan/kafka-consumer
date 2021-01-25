package com.kafka.event.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.event.consumer.entity.LibraryEvent;
import com.kafka.event.consumer.repository.LibraryEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by Bulut Cakan (179997) on
 * Hour :23:01
 * Day: Sunday
 * Month:January
 * Year:2021
 */
@Service
@Slf4j
public class LibraryEventsService {

    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    LibraryEventRepository libraryEventRepository;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        LibraryEvent libraryEvent = objectMapper.readValue(consumerRecord.value(), LibraryEvent.class);
        log.info("library event : {}  ", libraryEvent);


        if (libraryEvent.getLibraryEventId() != null && libraryEvent.getLibraryEventId() == 1000) {
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        switch (libraryEvent.getLibraryEventType()) {
            case NEW:
                save(libraryEvent);
                break;
            case UPDATE:
                validate(libraryEvent);
                save(libraryEvent);
            default:
                log.info("invalid library event type");
        }


    }

    private void validate(LibraryEvent libraryEvent) {
        if (Objects.isNull(libraryEvent.getLibraryEventId()))
            throw new IllegalArgumentException("Library Event id is missing");

        Optional<LibraryEvent> libraryEventOptional = libraryEventRepository.findById(libraryEvent.getLibraryEventId());
        if (!libraryEventOptional.isPresent())
            throw new IllegalArgumentException("Not valid library event");

        log.info("Validation is successful for the library event : {} ", libraryEvent);


    }

    private void update(LibraryEvent libraryEvent) {

    }

    private void save(LibraryEvent libraryEvent) {
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventRepository.save(libraryEvent);
        log.info("Sucessfuly persited {}", libraryEvent);
    }

    public void handleRecovery(ConsumerRecord<Integer, String> record) {

        Integer key = record.key();
        String message = record.value();

        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, message, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, message, result);
            }
        });
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
