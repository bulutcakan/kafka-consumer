package com.kafka.event.consumer.repository;

import com.kafka.event.consumer.entity.LibraryEvent;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Bulut Cakan (179997) on
 * Hour :23:11
 * Day: Sunday
 * Month:January
 * Year:2021
 */
public interface LibraryEventRepository extends CrudRepository<LibraryEvent, Integer> {
}
