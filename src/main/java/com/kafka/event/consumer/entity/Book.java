package com.kafka.event.consumer.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

/**
 * Created by Bulut Cakan (179997) on
 * Hour :16:28
 * Day: Sunday
 * Month:January
 * Year:2021
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Book {

    @Id
    private Integer bookId;

    @NotBlank
    private String bookName;

    @NotBlank
    private String bookAuthor;

    @OneToOne
    @JoinColumn(name = "libraryEvent_Id")
    private LibraryEvent libraryEvent;

}
