package com.example.jpa.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
// uniqueConstraints = {@UniqueConstraint(name = "제약조건이름", columnNames = {제약조건
// 컬럼명}) })
@Table(name = "membertbl", uniqueConstraints = {
        @UniqueConstraint(name = "NAME_AGE_UNIQUE", columnNames = { "name", "age" }) })
@Entity
public class Member {
    // id,name,age,roleType(ADMIN,USER),created_date,last_modified_date,description
    // roleType(ADMIN,USER) : 입력값에 제한을 두고 싶은 상황, enum 클래스를 활용한다

    @Id
    private String id;

    @Column(name = "name") // 컬럼 이름 변경
    private String userName;

    private Integer age;

    @Enumerated(EnumType.STRING) // enum 클래스 활용
    private RoleType roleType;

    @CreatedDate // insert 시 시간 자동으로 저장
    private LocalDateTime createdDate;

    @LastModifiedDate // 마지막으로 변경된 시간 자동 저장
    private LocalDateTime lastModifiedDate;

    @Lob // 대용량 데이터 담는 어노테이션 (내용은 적을게 많아서)
    private String description;
}
