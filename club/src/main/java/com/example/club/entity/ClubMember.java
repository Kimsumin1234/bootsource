package com.example.club.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.club.constant.ClubMemberRole;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ClubMember extends BaseEntity {

    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial; // 소셜가입

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ClubMemberRole> roleSet = new HashSet<>(); // Role 은 동일한 상수가 중복으로 들어가면 안되서 List 말고 Set 으로 담음

    public void addMemberRole(ClubMemberRole memberRole) {
        roleSet.add(memberRole);
    }

}
