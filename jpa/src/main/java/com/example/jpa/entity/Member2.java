package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "team2")
@Setter
@Getter
// uniqueConstraints = {@UniqueConstraint(name = "제약조건이름", columnNames = {제약조건
// 컬럼명}) })
@Table(name = "jpql_member")
@Entity
public class Member2 {
    @SequenceGenerator(name = "jpql_member_seq_gen", sequenceName = "jpql_member_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpql_member_seq_gen")
    @Id
    private Long id;

    @Column(name = "name")
    private String userName;

    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team2 team2;

}
