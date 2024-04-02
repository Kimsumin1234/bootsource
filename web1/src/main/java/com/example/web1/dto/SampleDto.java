package com.example.web1.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SampleDto {
    private Long id;
    private String first;
    private String last;
    private LocalDateTime regTime;
}
