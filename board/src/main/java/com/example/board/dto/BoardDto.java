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
public class BoardDto {
    private Long bno;

    private String title;

    private String content;

    private String writerEmail; // 작성자 아이디(email)

    private String writerName; // 작성자 이름

    private Long replyCount; // 해당 게시물의 댓글 수

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
