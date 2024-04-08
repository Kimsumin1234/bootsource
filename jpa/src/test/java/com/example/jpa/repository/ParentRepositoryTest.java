package com.example.jpa.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Child;
import com.example.jpa.entity.Parent;

@SpringBootTest
public class ParentRepositoryTest {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ChildRepository childRepository;

    @Test
    public void insertTest() {
        // 부모 한명에 자식 2명 만들기
        Parent parent = Parent.builder()
                .name("parent1").build();
        parentRepository.save(parent);

        IntStream.rangeClosed(1, 2).forEach(i -> {
            Child child = Child.builder().name("child" + i).parent(parent).build();
            childRepository.save(child);
        });

    }

    @Test
    public void insertCascadeTest() {
        // 부모 한명에 자식 2명 만들기
        Parent parent = Parent.builder()
                .name("parent3").build();

        IntStream.rangeClosed(1, 2).forEach(i -> {
            Child child = Child.builder().name("child" + i).parent(parent).build();
            parent.getChild().add(child);

            // 에러는 안뜨지만 메소드를 구현해 놓지않아서 List<Child> 에 안담긴다
            // @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) 부모에 cascade 를 추가
            // 하니깐 됨
            parentRepository.save(parent);

            // InvalidDataAccessApiUsageException : 부모부터 저장이 되야 자식이 저장이된다 자식부터 저장하려고하니깐 에러뜸
            // childRepository.save(child);
        });

    }

    @Test
    public void deleteTest() {
        // 1번방법 : 부모가 자식을 가지고 있는 경우, 삭제시 자식의 부모 아이디를 변경후 부모삭제
        Parent parent = Parent.builder()
                .id(1L).build();

        // 부모를 null 로 지정하고 부모 삭제하는 방법
        // IntStream.rangeClosed(1, 2).forEach(i -> {
        // Child child = Child.builder().id((long) i).parent(null).build();
        // childRepository.save(child);
        // parentRepository.delete(parent);
        // });

        // 자식먼저 삭제후 부모삭제 하는 방법
        Child c1 = Child.builder().id(1L).build();
        childRepository.delete(c1);
        Child c2 = Child.builder().id(2L).build();
        childRepository.delete(c2);

        parentRepository.delete(parent);

    }

    @Test
    public void deleteCascadeTest() {
        // OneToMany 일때 cascade 를 이용해서 같이 삭제하는 방법
        Parent parent = Parent.builder()
                .id(52L).build();

        Child c1 = Child.builder().id(102L).build();
        parent.getChild().add(c1);
        Child c2 = Child.builder().id(103L).build();
        parent.getChild().add(c2);

        parentRepository.delete(parent);

    }

    // @Transactional
    @Test
    public void deleteOrphanTest() {
        // 기존 자식 여부 확인하기 위해서 먼저 부모 찾아오기
        Parent parent = parentRepository.findById(102L).get();
        System.out.println("기존자식" + parent.getChild());

        // .delete(c1) 으로 제거할수 있는데
        // ArrayList 에 0 번에 있는 자식을 제거 하면 이것도 제거할수 있을까?
        // 객체개념에서 인덱스를 제거 => List<Child> 에서 제거 => Child 엔티티 제거
        // OneToMany 일경우 : ToString exclude + fetch = FetchType.EAGER, cascade =
        // CascadeType.ALL, orphanRemoval = true
        // 하니깐 삭제가 된다
        // 그렇다면 add() 도 가능하다
        parent.getChild().remove(0);
        parentRepository.save(parent);

        // FetchType 이 LAZY 인 경우 오류발생
        // LazyInitializationException proxy - no Session : Parent 가
        // @OneToMany-FetchType.LAZY 여서 Child 를 가져오지 못했다
        // 해결방법 : @Transactional (springframework꺼) 추가하기?

    }
}
