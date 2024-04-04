package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Test {

    @Id
    private Long id;
    private long id2;
    @Column(columnDefinition = "number(8)") // 컬럼속성을 직접 변경
    private int id3;
    private Integer id4;

    // create table test (
    // id number(19,0) not null, Long 기본은 19자리, 0은 소수점 을 의미
    // id2 number(19,0) not null, @Column(nullable=false) 를 안해도 기본타입은 not null 을
    // 자동으로 해준다
    // id3 number(10,0) not null,
    // id4 number(10,0),
    // primary key (id)
    // )

    // 기본타입 : int, long, boolean, char, float, double - null 대입 불가능
    // 객체타입 : Integer, Long, Boolean ... (대문자로 시작) - null 대입 가능
}
