package com.example.mart.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mart.entity.Item;
import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.OrderItem;
import com.example.mart.entity.OrderStatus;

@SpringBootTest
public class MartRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void insertTest() {
        // 멤버 3명 추가
        memberRepository.save(Member.builder().name("홍길동").zipcode("15102").city("서울").street("해방촌").build());
        memberRepository.save(Member.builder().name("김길동").zipcode("15103").city("서울").street("이태원").build());
        memberRepository.save(Member.builder().name("홍길동").zipcode("15104").city("인천").street("부평").build());

        // 아이템 3개 추가
        itemRepository.save(Item.builder().name("모자").price(10000).stockQuantity(10).build());
        itemRepository.save(Item.builder().name("티셔츠").price(20000).stockQuantity(15).build());
        itemRepository.save(Item.builder().name("신발").price(10000).stockQuantity(20).build());
    }

    // 주문
    @Test
    public void orderInsertTest() {
        // 1.누가 주문하느냐? (id가 1번인 사람이 주문을한다면)
        // 멤버가 이미 있을경우 이런식으로 가능
        Member member = Member.builder().id(1L).build();
        // 어떤 아이템? (id가 1번인 상품을 주문)
        Item item = Item.builder().id(1L).build();
        // 주문 + 주문상품
        // 주문, 주문상품은 없으니깐 만들어준다
        // 부모키를 먼저 save 하고 자식키를 save 한다
        Order order = Order.builder().member(member).orderDate(LocalDateTime.now()).orderStatus(OrderStatus.ORDER)
                .build();
        orderRepository.save(order);
        OrderItem orderItem = OrderItem.builder().item(item).order(order).orderPrice(10000).count(3).build();
        orderItemRepository.save(orderItem);
    }

    @Test
    public void readTest() {
        // 전체 회원 조회
        memberRepository.findAll().forEach(member -> System.out.println(member));

        // 전체 아이템 조회
        itemRepository.findAll().forEach(item -> System.out.println(item));

        // 주문아이템 조회 시 주문 정보 출력 (OrderItem(N):Order(1))
        // OrderItem(id=1, item=Item(id=1, name=모자, price=10000, stockQuantity=10),
        // order=Order(id=1, member=Member(id=1, name=홍길동, zipcode=15102, city=서울,
        // street=해방촌), orderDate=2024-04-05T15:04:32.230741,orderStatus=ORDER),
        // orderPrice=10000, count=3)
        orderItemRepository.findAll().forEach(entity -> {
            System.out.println(entity);

            // @ManyToOne 으로 연결된거 끼리 가져올수있다
            // 객체 그래프 탐색 기능이 가능한 이유 : N:1 관계에서 FetchType.EAGER 이기 때문
            System.out.println("상품정보" + entity.getItem());
            System.out.println("구매자" + entity.getOrder().getMember().getName());
        });
    }

    @Test
    public void readTest2() {
        // @OneToMany 를 이용해 조회
        // @OneToMany 는 관련있는 엔티티를 처음부터 안 가지고 옴
        // Order : OrderItem
        // Order 를 기준으로 OrderItem 조회
        Order order = orderRepository.findById(2L).get();
        System.out.println(order); // 에러발생해결 => @ToString(exclude = "orderItems")

        // Order 를 기준으로 OrderItem 조회
        // 에러해결방법 : 1.@Transactional 2.FetchType.EAGER
        System.out.println(order.getOrderItems());

    }

    @Transactional
    @Test
    public void readTest3() {
        // @OneToMany 를 이용해 조회
        // Member : Order
        // 회원의 주문내역 조회 (@Transactional 방식으로 해보기)
        Member member = memberRepository.findById(1L).get();
        System.out.println(member);

        member.getOrders().forEach(m -> System.out.println(m));

    }

    @Test
    public void updateTest() {
        // 멤버 주소 수정
        Member member = memberRepository.findById(1L).get();
        member.setCity("부산");
        memberRepository.save(member);

        // 아이템 가격 수정
        Item item = itemRepository.findById(3L).get();
        item.setPrice(50000);
        itemRepository.save(item);

        // 주문상태 수정 CANCEL
        Order order = orderRepository.findById(1L).get();
        order.setOrderStatus(OrderStatus.CANCEL);
        orderRepository.save(order);
    }

    @Test
    public void deleteTest() {
        // 주문 제거 시 주문아이템도 한꺼번에 제거 가능?
        // 주문 조회 시 주문아이템도 조회 가능?

        // 주문 아이템 제거후 주문제거
        orderItemRepository.deleteById(1L);

        orderRepository.deleteById(1L);
    }
}
