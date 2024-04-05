package com.example.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.jpa.entity.Team;
import com.example.jpa.entity.TeamMember;

@SpringBootTest
public class TeamRepositoryTest {

    @Autowired // test 는 final 사용불가
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Test
    public void insertTest() {
        // 외래키 제약조건 순서 팀 삽입 -> 팀맴버 삽입
        // 팀 정보 삽입
        Team team1 = teamRepository.save(Team.builder().id("team1").name("팀1").build());
        Team team2 = teamRepository.save(Team.builder().id("team2").name("팀2").build());
        Team team3 = teamRepository.save(Team.builder().id("team3").name("팀3").build());

        // 회원 정보 삽입
        // 제약조건 때문에 팀정보를 넣어준다
        teamMemberRepository.save(TeamMember.builder().id("member1").userName("홍길동").team(team1).build());
        teamMemberRepository.save(TeamMember.builder().id("member2").userName("성춘향").team(team2).build());
        teamMemberRepository.save(TeamMember.builder().id("member3").userName("이순신").team(team2).build());
        teamMemberRepository.save(TeamMember.builder().id("member4").userName("정우성").team(team3).build());
    }

    // 연관관계 가 있는 데이터 조회
    // 1. 다대일(멤버:팀) 관계에서는 기본적으로 1(팀)에 해당하는 엔티티 정보를 가지고 나옴 => FetchType 개념
    // ==> FetchType.EAGER 가 기본설정(변경가능)
    // ==> 멤버를 조회시 팀 정보도 같이 조회가됨 (객체 그래프 탐색으로 접근 할수있다)
    // ==> 객체지향 쿼리를 직접 작성해서 원하는걸 조회하는것도 가능하다

    // 2. 일대다(팀:멤버) 관계에서는 다(멤버)에 해당하는 엔티티를 안가지고 나옴 => FetchType 개념
    // ==> FetchType.LAZY 가 기본설정(변경가능)

    // FetchType : 연결관계에서 상대 정보를 같이 가지고 나올건지 말건지 여부
    // FetchType.EAGER : 가지고 나옴
    // FetchType.LAZY : 안가지고 나옴
    @Test
    public void getRowTest() {
        // select
        // tm1_0.member_id,
        // t1_0.team_id,
        // t1_0.team_name,
        // tm1_0.user_name
        //
        // from
        // team_member tm1_0
        //
        // left join
        // team t1_0
        // on t1_0.team_id=tm1_0.team_team_id
        //
        // where
        // tm1_0.member_id=?

        // team_member(N) : team(1) => N:1 의 관계, 외래키 제약조건
        // member를 찾을때 : N:1 관계에서는 1에 해당하는 정보도 기본으로 가지고 옴
        TeamMember teamMember = teamMemberRepository.findById("member1").get();

        // member1 에 해당하는 team 에 해당하는 정보도 알아서 가져왔다
        // => join 을 한경우 이렇게 가져온다, 외래키를 설정해놓으면 스프링부트가 알아서 join 해준다
        System.out.println(teamMember); // TeamMember(id=member1, userName=홍길동, team=Team(id=team1, name=팀1))

        // 객체 그래프 탐색 : 객체끼리 연관되있으면 탐색이 가능하다?
        // team 정보만 뽑을수있다
        System.out.println(teamMember.getTeam()); // Team(id=team1, name=팀1)
        // team 이름도 이렇게 뽑을수있다
        System.out.println(teamMember.getTeam().getName()); // 팀1

        // 회원 조회 시 나와 같은 팀에 소속된 회원 과 팀 조회
        teamMemberRepository.findByMemberEqualTeam("팀2").forEach(member -> {
            System.out.println(member);
        });
    }

    @Test
    public void updateTest() {
        // 멤버의 팀 변경
        // 수정할 회원을 먼저 조회
        TeamMember member = teamMemberRepository.findById("member3").get();
        Team team = Team.builder().id("team3").build();
        member.setTeam(team);

        System.out.println("수정 후" + teamMemberRepository.save(member));
    }

    @Test
    public void deleteTest() {
        // 팀원삭제 부터 먼저 해야한다 (자식 레코드 부터 먼저 삭제) or 팀원의 팀을 null 로 설정
        TeamMember member = teamMemberRepository.findById("member1").get();
        member.setTeam(null);
        teamMemberRepository.save(member);
        // 팀삭제
        teamRepository.deleteById("team1");

    }

    // OneToMany 설정 기준으로
    // 팀을 기준으로 회원 조회가 가능할까?
    @Transactional
    @Test
    public void getMemberList() {
        // select t1_0.team_id, t1_0.team_name from team t1_0 where t1_0.team_id=?
        Team team = teamRepository.findById("team3").get();
        // 처음에 LazyInitializationException 가 발생한 이유 : ToString() 에서 members 변수를 출력하려고해서
        // @ToString(exclude = { "members" }) 를 사용해서 해결
        System.out.println(team);

        // org.hibernate.LazyInitializationException (FetchType.LAZY 가 기본설정 이여서 에러가 뜸)
        // 1. @Transactional 을 사용해서 해결 : 한꺼번에 작업을 처리해줘 라는 어노테이션
        // 2. FetchType.LAZY 변경으로 해결 : @OneToMany(mappedBy = "team", fetch =
        // FetchType.EAGER)

        // select 를 두번 사용했다
        // select m1_0.team_team_id, m1_0.member_id, m1_0.user_name from team_member
        // m1_0 where m1_0.team_team_id=?
        team.getMembers().forEach(m -> System.out.println(m));
    }
}
