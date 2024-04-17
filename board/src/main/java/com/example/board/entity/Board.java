package com.example.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "writer")
@Setter
@Getter
@Entity
public class Board extends BaseEntity {

    @SequenceGenerator(name = "board_seq_gen", sequenceName = "board_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gen")
    @Id
    private Long bno;

    private String title;

    private String content;

    // @ManyToOne - 기본 fetch 방식이 즉시로딩(FetchType.EAGER)
    // FetchType.LAZY 로 바꿔서 필요할때만 정보를 가져오게 설정
    // FetchType.EAGER : left join 방식으로 데이터 처리
    // FetchType.LAZY : join x , select 구문을 두번 실행
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;
}
