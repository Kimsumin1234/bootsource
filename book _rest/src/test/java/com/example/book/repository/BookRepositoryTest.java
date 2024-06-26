package com.example.book.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.example.book.entity.Book;
import com.example.book.entity.Category;
import com.example.book.entity.Publisher;

import jakarta.transaction.Transactional;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    public void categoryInsertTest() {
        Category category = Category.builder().name("컴퓨터").build();
        categoryRepository.save(category);
        Category category2 = Category.builder().name("경제/경영").build();
        categoryRepository.save(category2);
        Category category3 = Category.builder().name("인문").build();
        categoryRepository.save(category3);
        Category category4 = Category.builder().name("소설").build();
        categoryRepository.save(category4);
        Category category5 = Category.builder().name("자기계발").build();
        categoryRepository.save(category5);
    }

    @Test
    public void publisherInsertTest() {
        Publisher publisher = Publisher.builder().name("로드북").build();
        publisherRepository.save(publisher);
        Publisher publisher2 = Publisher.builder().name("다산").build();
        publisherRepository.save(publisher2);
        Publisher publisher3 = Publisher.builder().name("웅진지식하우스").build();
        publisherRepository.save(publisher3);
        Publisher publisher4 = Publisher.builder().name("비룡소").build();
        publisherRepository.save(publisher4);
        Publisher publisher5 = Publisher.builder().name("을유문화사").build();
        publisherRepository.save(publisher5);
    }

    @Test
    public void bookInsertTest() {
        LongStream.rangeClosed(1, 20).forEach(i -> {
            Book book = Book.builder().title("book title " + i)
                    .writer("book writer" + i)
                    .price(25000)
                    .salePrice(22500)
                    .category(Category.builder().id((i % 5) + 1).build())
                    .publisher(Publisher.builder().id((i % 5) + 1).build())
                    .build();
            bookRepository.save(book);
        });

    }

    @Transactional
    @Test
    public void testBookList() {
        List<Book> books = bookRepository.findAll();

        books.forEach(book -> {
            System.out.println(book);
            System.out.println("출판사 " + book.getPublisher().getName());
            System.out.println("분야 " + book.getCategory().getName());
        });
    }

    @Test
    public void testCateNameList() {
        List<Category> list = categoryRepository.findAll();

        list.forEach(cate -> System.out.println(cate));

        // Category(id=1, name=컴퓨터)
        // 이름만 담고 싶을경우
        // List<String> cateList = new ArrayList<>();
        // list.forEach(category->cateList.add(category.getName()));
        List<String> cateList = list.stream().map(entity -> entity.getName()).collect(Collectors.toList());

    }

    @Test
    public void testRead() {
        Book book = bookRepository.findById(2L).get();
        System.out.println(book);

    }

    @Test
    public void testModify() {
        Book book = bookRepository.findById(22L).get();
    }

    @Test
    public void testSearchList() {

        // page 번호는 0 부터 시작
        // Pageable pageable = PageRequest.of(0, 10);
        // Pageable pageable = PageRequest.of(0, 10, Direction.DESC);
        // Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "id");

        // 1페이지 에 10개의 게시글을 보여주고 id 를 기준으로 내림차순 정렬해줘
        // controller 에서 이작업을 해준다 , 원하는 페이지 입력, 게시글수 조절
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        // Page 객체 : 페이지 나누기에 필요한 메소드 제공 (websource 모델2 만들때 PageDto 역할)
        Page<Book> result = bookRepository
                .findAll(bookRepository.makePredicate("t", "book"), pageable);

        System.out.println("전체 행 수 " + result.getTotalElements());
        System.out.println("필요한 페이지 수 " + result.getTotalPages());
        result.getContent().forEach(content -> System.out.println(content));
    }
}
