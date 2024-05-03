package com.example.movie.dto;

import java.time.LocalDateTime;

import com.example.movie.constant.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    // Member 가 @Column(nullable = false) 이런거면
    // Member 엔티티에 맞춰서 @NotBlank 같은걸 주는거다
    // null 이 가능한데 무조건 주는거는 아니다

    @Email(message = "알맞은 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    private MemberRole role;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
