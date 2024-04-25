package com.example.movie.dto;

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
public class ReviewDto {
    private Long reviewNo;

    private int grade;

    private String text;

    // Member 관계 (MemberDto 자체를 가져와도 되긴함, 여기는 풀어서 써보는거임)
    // 화면단에 어떤식으로 나타낼건지 먼저 정한후 dto 관계설정을 한다
    // dto 는 화면단과 관련된 객체
    private Long mid;
    private String email;
    private String nickname;

    // Movie 관계
    private Long mno;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
