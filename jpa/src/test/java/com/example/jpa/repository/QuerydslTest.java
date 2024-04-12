package com.example.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import com.example.jpa.entity.Member2;
import com.example.jpa.entity.Product;
import com.example.jpa.entity.QMember2;
import com.example.jpa.entity.QProduct;
import com.querydsl.core.BooleanBuilder;

@SpringBootTest
public class QuerydslTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Member2Repository member2Repository;

    @Test
    public void testQueryDsl() {
        // QProduct 가져오기
        // Iter~ 가 붙은 객체는 대부분의 언어에서 for 구문을 사용하면 하나씩 요소를 가져올수있음
        QProduct product = QProduct.product;
        // 제품명이 제품1 인걸 조회
        Iterable<Product> products = productRepository.findAll(product.name.eq("제품1"));

        // 제품명이 제품6 이고 가격이 5000 초과인 제품 조회
        products = productRepository.findAll(product.name.eq("제품6").and(product.price.gt(5000)));

        // 제품명이 제품5 이고 가격이 5000 이상인 제품 조회
        // products =
        // productRepository.findAll(product.name.eq("제품5").and(product.price.goe(5000)));

        // 메소드가 길어질 경우 BooleanBuilder 에 한번 담아서 가독성을 올릴수있다
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(product.name.eq("제품1"));
        builder.and(product.price.goe(5000));
        products = productRepository.findAll(builder);

        // 제품명이 '제품' 글자가 포함된 제품 조회
        products = productRepository.findAll(product.name.contains("제품"));

        // 제품명이 '제품' 글자로 시작하는 제품 조회
        products = productRepository.findAll(product.name.startsWith("제품"));

        // 제품명이 '제품' 글자로 끝나는 제품 조회
        products = productRepository.findAll(product.name.endsWith("제품"));
        for (Product pro : products) {
            System.out.println(pro);
        }
    }

    @Test
    public void testQueryDsl2() {
        QMember2 member2 = QMember2.member2;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(member2.userName.eq("User1"));

        Iterable<Member2> list = member2Repository.findAll(builder, Sort.by("id").descending());
        for (Member2 mem : list) {
            System.out.println(mem);
        }
    }

}
