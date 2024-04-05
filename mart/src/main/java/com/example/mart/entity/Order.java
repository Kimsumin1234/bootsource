package com.example.mart.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Builder
@ToString(exclude = "orderItems")
@Getter
@Setter
@Table(name = "orders") // 테이블명은 order 사용불가 sql 구문중에 order by 이런게 있어서
@Entity
public class Order {

    @SequenceGenerator(name = "mart_order_seq_gen", sequenceName = "mart_order_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mart_order_seq_gen")
    @Id
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    private Member member;

    @CreatedDate
    private LocalDateTime orderDate;

    // 주문상태 - ORDER, CANCEL (이런 고정값 입력이라면 enum을 사용해 오타를 막을수있다)
    // EnumType.STRING : ORDER, CANCEL 이러한 상수를 STRING 타입으로 해줘
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // 관계를 형성하게 되면 JPA 에서 관계형성 테이블을 생성하게 된다
    // ex) ORDERS_ORDER_ITEMS 이런식으로 내가 만들지 않았는데 JPA 에서 그냥 테이블을 생성하는 경우가있음
    @Builder.Default // Builder 를 하게 되면 new ArrayList<>() 를 안해줘서 그걸 하게하는 어노테이션
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER) // 1관계 쪽은 무조건 List 타입이다
    private List<OrderItem> orderItems = new ArrayList<>();
}
