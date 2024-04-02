package com.example.web1.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @Email(message = "이메일을 확인해 주세요")
    @NotEmpty // @NotEmpty : @NotNull + "" 값 불가
    private String email;

    @Length(min = 2, max = 6) // 길이를 제한을 주면 @NotBlank 이 포함된 의미가 된다 (기본메세지 : 길이가 2에서 6 사이여야 합니다)
    // @NotBlank // @NotBlank : @NotEmpty + "" 값 불가
    private String name;
}
