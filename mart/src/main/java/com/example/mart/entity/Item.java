package com.example.mart.entity;

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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "mart_item")
@Entity
public class Item extends BaseEntity {

    @SequenceGenerator(name = "mart_item_seq_gen", sequenceName = "mart_item_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mart_item_seq_gen")
    @Column(name = "item_id")
    @Id
    private Long id;

    private String name;

    // int 기본타입을 사용하면 not null 이 붙는다 , Integer 객체타입을 쓰면 null 가능
    private int price;

    private int stockQuantity;
}
