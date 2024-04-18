package com.example.book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.book.dto.BookDto;
import com.example.book.dto.PageRequestDto;
import com.example.book.dto.PageResultDto;
import com.example.book.entity.Book;
import com.example.book.entity.Category;
import com.example.book.entity.Publisher;
import com.example.book.repository.BookRepository;
import com.example.book.repository.CategoryRepository;
import com.example.book.repository.PublisherRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    // 페이지 나누기 전
    // @Override
    // public List<BookDto> getList() {
    // List<Book> books = bookRepository.findAll(Sort.by("id").descending());
    // // List<BookDto> bookDtos = new ArrayList<>();
    // // books.forEach(book -> bookDtos.add(entityToDto(book)));
    // List<BookDto> bookDtos = books.stream().map(book ->
    // entityToDto(book)).collect(Collectors.toList());
    // return bookDtos;
    // }

    // 페이지 나누기 후
    @Override
    public PageResultDto<BookDto, Book> getList(PageRequestDto requestDto) {
        // Test 에서 페이지정보를 입력하기 때문에 여기 메소드에서는 정보를 받고 정렬을 해준다
        Pageable pageable = requestDto.getPageable(Sort.by("id").descending());

        // Page 객체 : 페이지 나누기에 필요한 메소드 제공 (websource 모델2 만들때 PageDto 역할)
        Page<Book> result = bookRepository
                .findAll(bookRepository.makePredicate(requestDto.getType(), requestDto.getKeyword()), pageable);

        Function<Book, BookDto> fn = (entity -> entityToDto(entity));
        return new PageResultDto<>(result, fn);
    }

    @Override
    public Long bookCreate(BookDto dto) {
        // Category 와 Publisher 는 dto에 이름만 담겨있음
        // dto 에서 Entity 로 가려면 Category 와 Publisher 객채를 만들어야함
        // CategoryRepository 와 PublisherRepository 에서 가서 이름으로 아이디를 찾을수있는 메소드 작성
        Category category = categoryRepository.findByName(dto.getCategoryName()).get();
        Publisher publisher = publisherRepository.findByName(dto.getPublisherName()).get();

        Book book = dtoToEntity(dto);
        book.setCategory(category);
        book.setPublisher(publisher);

        Book newBook = bookRepository.save(book);

        return newBook.getId();
    }

    @Override
    public List<String> categoryNameList() {
        List<Category> list = categoryRepository.findAll();
        // Category(id=1, name=컴퓨터)
        // 이름만 담고 싶을경우
        // List<String> cateList = new ArrayList<>();
        // list.forEach(category->cateList.add(category.getName()));
        List<String> cateList = list.stream().map(entity -> entity.getName()).collect(Collectors.toList());
        return cateList;
    }

    @Override
    public BookDto getRow(Long id) {
        Book book = bookRepository.findById(id).get();
        return entityToDto(book);
    }

    @Override
    public Long bookUpdate(BookDto updateDto) {
        // 수정할 대상 찾기
        Book book = bookRepository.findById(updateDto.getId()).get();
        // 수정할 값 세팅
        book.setPrice(updateDto.getPrice());
        book.setSalePrice(updateDto.getSalePrice());
        Book newBook = bookRepository.save(book);
        return newBook.getId();
    }

    @Override
    public void bookDelete(Long id) {
        bookRepository.deleteById(id);
    }

}
