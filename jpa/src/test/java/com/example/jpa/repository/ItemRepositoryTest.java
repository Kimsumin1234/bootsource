package com.example.jpa.repository;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Item;
import com.example.jpa.entity.ItemSellStatus;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void createTest() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Item item = Item.builder()
                    .itemNm("운동화" + i)
                    .price(95000 * i)
                    .stockNumber(30)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .build();
            itemRepository.save(item);
        });
    }

    @Test
    public void readTest() {
        // 한개조회
        System.out.println(itemRepository.findById(2L));
        System.out.println("-----------------------------");
        // 전체조회
        itemRepository.findAll().forEach(item -> System.out.println(item));
    }

    @Test
    public void updateTest() {
        // Optional<Item> result = itemRepository.findById(7L);
        // Item item = result.get();
        // item.setItemNm("바지");
        // item.setPrice(77777);
        Item item = itemRepository.findById(7L).get();
        item.setItemNm("바지");
        item.setPrice(77777);
        System.out.println(itemRepository.save(item));
    }

    @Test
    public void deleteTest() {
        Optional<Item> result = itemRepository.findById(6L);
        itemRepository.delete(result.get());
        System.out.println(itemRepository.findById(6L));
    }
}
