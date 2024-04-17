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
@ToString(exclude = "board")
@Setter
@Getter
@Entity
public class Reply extends BaseEntity {

    @SequenceGenerator(name = "reply_seq_gen", sequenceName = "reply_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_seq_gen")
    @Id
    private Long rno;

    private String text; // 댓글내용

    private String replyer; // 댓글작성자

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
