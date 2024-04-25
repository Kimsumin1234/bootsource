package com.example.movie.dto;

import java.time.LocalDateTime;

import com.example.movie.constant.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Setter
@Getter
public class MemberDto {
    private Long mid;

    private String email;

    private String password;

    private String nickname;

    private MemberRole role;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
