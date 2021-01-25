package com.kafka.event.consumer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.event.consumer.service.LibraryEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Created by Bulut Cakan (179997) on
 * Hour :21:39
 * Day: Sunday
 * Month:January
 * Year:2021
 */
@Component
@Slf4j
public class LibraryEventsConsumer {

    @Autowired
    LibraryEventsService libraryEventsService;

    @KafkaListener(topics = {"library-events"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("Consumer record is {} ", consumerRecord);
        libraryEventsService.processLibraryEvent(consumerRecord);
    }
}
