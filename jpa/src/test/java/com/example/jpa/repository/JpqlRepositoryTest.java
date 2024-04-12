package com.example.jpa.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.example.jpa.entity.Address;
import com.example.jpa.entity.Member2;
import com.example.jpa.entity.Order;
import com.example.jpa.entity.Product;
import com.example.jpa.entity.Team2;

@SpringBootTest
public class JpqlRepositoryTest {
    @Autowired
    private Member2Repository member2Repository;
    @Autowired
    private Team2Repository team2Repository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void insertTest() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Team2 team2 = Team2.builder()
                    .name("team" + i)
                    .build();
            team2Repository.save(team2);

            Member2 member2 = Member2.builder()
                    .userName("User" + i)
                    .age(10 + i)
                    .team2(team2)
                    .build();
            member2Repository.save(member2);

            Product product = Product.builder()
                    .name("제품" + i)
                    .price(i * 1000)
                    .stockAmount(i * 5)
                    .build();
            productRepository.save(product);
        });
    }

    @Test
    public void orderInsertTest() {
        Address address = new Address();
        address.setCity("서울시 종로구");
        address.setStreet("151-11");
        address.setZipcode("11017");

        IntStream.rangeClosed(1, 3).forEach(i -> {
            Member2 member2 = Member2.builder().id(1L).build();
            Product product = Product.builder().id(2L).build();

            Order order = Order.builder()
                    .member2(member2)
                    .product(product)
                    .homeAddress(address)
                    .build();
            orderRepository.save(order);
        });
    }

    @Test
    public void findMembersTest() {
        // member2Repository.findByMembers().forEach(member ->
        // System.out.println(member));
        System.out.println(member2Repository.findByMembers(Sort.by("age")));

        // java.lang.Object;@3ccc5fa2 : Object 는 toString 이 자동으로 안되있다
        List<Object[]> list = member2Repository.findByMembers2();
        for (Object[] objects : list) {
            System.out.println(Arrays.toString(objects));
            System.out.printf("username = %s, age = %d\n", objects[0], objects[1]);
        }
    }

    @Test
    public void findByHomeAddressTest() {
        System.out.println(orderRepository.findByHomeAddress());
    }

    @Test
    public void findByOrdersTest() {
        List<Object[]> list = orderRepository.findByOrders();

        for (Object[] objects : list) {
            System.out.println(Arrays.toString(objects));

            // 개별로 조회 가능
            // @Query("select o.member2, o.product, o.id from Order o")
            // select o.member2, o.product, o.id 이 순서대로 [0] [1] [2]
            Member2 member2 = (Member2) objects[0];
            Product product = (Product) objects[1];
            Long id = (Long) objects[2];

            System.out.println(member2);
            System.out.println(product);
            System.out.println(id);
        }
    }

    @Test
    public void findByAgeListTest() {
        System.out.println(member2Repository.findByAgeList(14));
    }

    @Test
    public void findByTeamEqualTest() {
        System.out.println(member2Repository.findByTeamEqual(Team2.builder().id(3L).build()));
    }

    @Test
    public void findByTeamIdEqualTest() {
        System.out.println(member2Repository.findByTeamIdEqual(2L));
    }

    @Test
    public void aggregateTest() {
        List<Object[]> list = member2Repository.aggregate();
        for (Object[] objects : list) {
            System.out.println(Arrays.toString(objects)); // [10, 155, 15.5, 20, 11]
            System.out.println("회원수 " + objects[0]); // 회원수 10
            System.out.println("나이 합 " + objects[1]); // 나이 합 155
            System.out.println("나이 평균 " + objects[2]); // 나이 평균 15.5
            System.out.println("최대 나이 " + objects[3]); // 최대 나이 20
            System.out.println("최소 나이 " + objects[4]); // 최소 나이 11
        }
    }

    @Test
    public void joinTest() {
        // 조인 후 Member2 결과만 가져오기
        System.out.println(member2Repository.findByTeamMember("team1"));

        // 조인 후 Member2 , Team2 결과 가져오기
        List<Object[]> list = member2Repository.findByTeamMember2("team2");
        for (Object[] objects : list) {
            Member2 member2 = (Member2) objects[0];
            Team2 team2 = (Team2) objects[1];
            System.out.println(member2);
            System.out.println(team2);
        }
    }
}
