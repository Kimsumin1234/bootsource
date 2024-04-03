package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "memotbl") // 작업하는 테이블 이름변경
@Entity // dto 랑 구별하기 위해서 어노테이션 사용, 데이터베이스에서 데이터로 관리하는 대상
public class Memo {
    // IDENTITY : mno number(19,0) generated as identity, (기본키 생성은 데이터베이스에 위임)
    // AUTO (SEQUENCE를 자동으로 실행) : create sequence memotbl_seq start with 1 increment
    // by 50 50단위로 시퀀스 생성
    // SEQUENCE : create sequence memotbl_seq start with 1 increment by 50
    // (오라클 기준, db가 달라지면 다를수있음)
    @SequenceGenerator(name = "memo_seq_gen", sequenceName = "memo_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memo_seq_gen")
    @Id // DB 에서 primary key 랑 같은의미
    private Long mno;

    // 자바에서 변수명은 _ 사용 X => memo_text (데이터베이스 컬럼명)
    @Column(nullable = false, length = 300) // DB 에서 not null, length 로 길이수정가능
    private String memoText;
}
