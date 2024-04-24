package com.example.board.dto;

import java.time.LocalDateTime;

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
public class ReplyDto {
    private Long rno;

    private String text; // 댓글내용

    // private String replyer; // 댓글작성자 (익명)

    // Member 와 관계
    private String writerEmail; // 작성자 아이디(email)
    private String writerName; // 작성자 이름

    private Long bno; // 부모글 번호

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

}
