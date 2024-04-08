package com.example.todo.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

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

@DynamicInsert // @ColumnDefault(" ") 가 동작을 하기 위해서 사용
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@Table(name = "TODOTBL")
@Entity
public class Todo extends BaseEntity {

    @SequenceGenerator(name = "todo_seq_gen", sequenceName = "todo_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_seq_gen")
    @Column(name = "TODO_ID")
    @Id
    private Long id;

    // @Column(nullable = false) @DynamicInsert 를 적용하기 위해서 not null 제거
    @ColumnDefault("0") // sql 구문에서 default 값을 설정하는 것과 같음
    private Boolean completed;

    // @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean important;

    @Column(nullable = false)
    private String title;

}
// sql 에서 default 값을 삽입한다는 의미 : 아무것도 입력이 되지 않으면 default 값으로 입력해주라는 의미
// jpa 에서는 default 값을 자동으로 삽입하려면 @DynamicInsert 가 필요하다
// @DynamicInsert : 데이터가 존재하는 필드만으로 insert sql(?개수) 문 생성 => not null 이 아닌 필드만 해줌
