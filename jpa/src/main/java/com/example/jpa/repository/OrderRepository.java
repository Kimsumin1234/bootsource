package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jpa.entity.Address;
import com.example.jpa.entity.Order;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주소 조회
    // o.homeAddress 는 1개 조회기 때문에 Address 에 담는다
    @Query("select o.homeAddress from Order o")
    List<Address> findByHomeAddress();

    // 1개 이상일때 Object[] 를 사용한다
    @Query("select o.member2, o.product, o.id from Order o")
    List<Object[]> findByOrders();
}
