package com.kafka.event.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by Bulut Cakan (179997) on
 * Hour :16:00
 * Day: Sunday
 * Month:January
 * Year:2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class LibraryEvent {

    @Id
    @GeneratedValue
    private Integer libraryEventId;

    @Enumerated(EnumType.STRING)
    private LibraryEventType libraryEventType;

    @ToString.Exclude
    @OneToOne(mappedBy = "libraryEvent", cascade = {CascadeType.ALL})
    private Book book;

}
