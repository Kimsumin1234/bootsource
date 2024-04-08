package com.example.todo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@Builder
public class TodoDto {
    private Long id;

    private Boolean completed;

    private Boolean important;

    private String title;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

}
