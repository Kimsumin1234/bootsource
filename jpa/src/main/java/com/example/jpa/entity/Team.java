package com.example.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
// 관계맺은 속성은 기본적으로 ToString 에서 뺀다
@ToString(exclude = { "members" }) // ToString 생성시 클래스 내 모든 속성이 기본설정, exclude 를 사용하면 지정속성을 뺄수있다
@Setter
@Getter
@Entity
public class Team {

    @Column(name = "team_id")
    @Id
    private String id;

    @Column(name = "team_name")
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER) // 다쪽이 해당하는곳이 주인, mappedBy = "주인" 을 써준다
    private List<TeamMember> members = new ArrayList<>();
}
