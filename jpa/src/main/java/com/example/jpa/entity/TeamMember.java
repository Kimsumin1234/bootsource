package com.example.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter
@Getter
@Entity
public class TeamMember {

    @Column(name = "member_id")
    @Id
    private String id;

    private String userName;

    // sql 코드 외래키 제약조건
    // @Many(팀멤버,다쪽에 설정해준다)
    @ManyToOne // 관계를 알려주는 어노테이션 종류는 ppt 에 있다
    private Team team; // 객체와 객체 관계 이기 때문에 id 말고 객체를 사용한다

}
