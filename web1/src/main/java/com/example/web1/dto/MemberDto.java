package com.example.web1.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    // 기본메세지 : "(?=^[A-Za-z])(?=.+\d)[A-Za-z\d]{6,12}$"와 일치해야 합니다
    @Pattern(regexp = "(?=^[A-Za-z])(?=.+\\d)[A-Za-z\\d]{6,12}$", message = "아이디는 영대소문자,숫자를 사용해서 6~12자리입니다")
    // @NotEmpty
    private String userid;

    @Pattern(regexp = "(?=^[A-Za-z])(?=.+\\d)(?=.+[!@$%])[A-Za-z\\d!@$%]{8,15}$", message = "비밀번호는 영대소문자,숫자,특수문자(!@$%)를 사용해서 8~15자리입니다")
    // @NotEmpty
    private String password;

    @NotNull(message = "나이는 필수 요소입니다") // 기본메세지 : 널이어서는 안됩니다
    @Min(value = 0) // 최소 (기본메세지 : 0 이상이어야 합니다)
    @Max(value = 120) // 최대 (기본메세지 : 120 이하여야 합니다)
    private Integer age;

    @NotEmpty(message = "이메일은 필수 요소입니다")
    @Email
    private String email;

    @Length(min = 2, max = 6, message = "이름은 2~6자리로 작성해 주세요")
    private String name;

}
