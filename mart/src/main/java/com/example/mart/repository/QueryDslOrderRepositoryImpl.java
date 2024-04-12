package com.example.mart.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.mart.entity.Item;
import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.QItem;
import com.example.mart.entity.QMember;
import com.example.mart.entity.QOrder;
import com.example.mart.entity.QOrderItem;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;

public class QueryDslOrderRepositoryImpl extends QuerydslRepositorySupport implements QueryDslOrderRepository {

    public QueryDslOrderRepositoryImpl() {
        // 기준이 되는 클래스를 적어둔다
        super(Order.class);
    }

    @Override
    public List<Object[]> joinList() {
        // Q객체 가져오기
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        QOrderItem orderItem = QOrderItem.orderItem;

        // 쿼리문 생성 1. JPAQuery 2. JPQLQuery
        // sql : select * from order o join member m on o.member_id = m.member_id
        JPQLQuery<Order> query = from(order);

        // join : innerJoin, leftJoin, rightJoin, fullJoin 지원
        // join(조인대상, 별칭으로 사용할 쿼리타입)
        // 내부조인 : join() , innerJoin()
        // query.innerJoin(order.member, member); // on o.member_id = m.member_id
        query.join(order.member, member) // == query.innerJoin(order.member, member)
                .leftJoin(order.orderItems, orderItem); // 이렇게 하면 총 3개 테이블 조인

        JPQLQuery<Tuple> tuple = query.select(order, member, orderItem); // select m,t,i
        List<Tuple> result = tuple.fetch();
        System.out.println("결과");
        System.out.println(result);

        // List<Tuple> ==> List<Object[]>
        List<Object[]> list = result.stream().map(t -> t.toArray()).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Member> members() {

        QMember member = QMember.member;

        // select * from member where name = 'user1' order by name desc;
        JPQLQuery<Member> query = from(member); // from member
        query.where(member.name.eq("user1")).orderBy(member.name.desc()); // where name = 'user1' order by name desc
        JPQLQuery<Member> tuple = query.select(member);
        List<Member> list = tuple.fetch();
        return list;
    }

    @Override
    public List<Item> items() {
        // select * from item where name='바지'and price > 20000
        QItem item = QItem.item;
        JPQLQuery<Item> query = from(item);
        query.where(item.name.eq("바지").and(item.price.gt(20000)));
        JPQLQuery<Item> tuple = query.select(item);
        List<Item> list = tuple.fetch();
        return list;
    }

}
