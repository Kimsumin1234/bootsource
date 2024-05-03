package com.example.movie.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.movie.constant.MemberRole;
import com.example.movie.dto.PageRequestDto;
import com.example.movie.entity.Member;
import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.entity.Review;

@SpringBootTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieImageRepository movieImageRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void movieInsertTest() {
        // 영화/영화이미지 샘플 데이터 추가
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie = Movie.builder()
                    .title("Movie" + i)
                    .build();
            movieRepository.save(movie);

            int count = (int) ((Math.random() * 5) + 1);

            for (int j = 0; j < count; j++) {
                MovieImage mImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("img" + j + ".jpg")
                        .build();
                movieImageRepository.save(mImage);
            }
        });
    }

    @Test
    public void memberInsertTest() {
        // 멤버 샘플 데이터 추가
        // IntStream.rangeClosed(1, 100).forEach(i -> {
        // Member member = Member.builder()
        // .email("mem" + i + "@naver.com")
        // .password(passwordEncoder.encode("1111"))
        // .role(MemberRole.MEMBER)
        // .nickname("reviewe" + i)
        // .build();
        // memberRepository.save(member);
        // });
        Member member = Member.builder()
                .email("admin1@naver.com")
                .password(passwordEncoder.encode("1111"))
                .role(MemberRole.ADMIN)
                .nickname("admin1")
                .build();
        memberRepository.save(member);
    }

    @Test
    public void reviewInsertTest() {
        // 리뷰 샘플 데이터 추가
        IntStream.rangeClosed(1, 200).forEach(i -> {
            Long mno = (long) ((Math.random() * 100) + 1);
            Movie movie = Movie.builder().mno(mno).build();

            Long mid = (long) ((Math.random() * 100) + 1);
            Member member = Member.builder().mid(mid).build();

            Review review = Review.builder()
                    .movie(movie)
                    .member(member)
                    .grade((int) (Math.random() * 5) + 1)
                    .text("이 영화에 대한..." + i)
                    .build();
            reviewRepository.save(review);
        });
    }

    @Test
    public void movieListTest() {
        // nativeQuery 로 ORDER BY 를 안햇는데 JPA에서 Sort.by(Sort.Direction.DESC, "mno") 를 하니깐
        // 자동으로 order by mi2.mno desc 를 해버림 그래서 "MI2"."MNO": 부적합한 식별자 에러메세지가 뜸
        // Projection : nativeQuery 를 쓰게되면 [값,값,값,...] 이런 형태를 담기위한 dto를 새로 더 만들어야한다
        // nativeQuery 를 안쓰고 엔티티 형식의 @Query 를 사용하면 [객체,값,...] 엔티티 객체를 가져와서 편하다
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Object[]> list = movieRepository.getListPage(pageRequest);
        for (Object[] objects : list) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void movieImageListTest() {

        // PageRequest pageRequest = PageRequest.of(0, 10);
        // Page<Object[]> list = movieImageRepository.getTotalList(pageRequest);
        // for (Object[] objects : list) {
        // System.out.println(Arrays.toString(objects));
        // }

        // 검색 추가
        PageRequestDto requestDto = PageRequestDto.builder()
                .type("t")
                .keyword("등록")
                .page(1)
                .size(10)
                .build();

        Page<Object[]> list = movieImageRepository.getTotalList(requestDto.getType(), requestDto.getKeyword(),
                requestDto.getPageable(Sort.by("mno").descending()));
        for (Object[] objects : list) {
            System.out.println(Arrays.toString(objects));
        }
    }

    // 특정영화조회 테스트
    @Test
    public void movieRowTest() {
        List<Object[]> list = movieImageRepository.getMovieRow(100L);
        for (Object[] objects : list) {
            System.out.println(Arrays.toString(objects));
        }
    }

    // @Transactional : 테이블이 여러개일 경우 1개의 테이블에 문제가 있을경우 되돌아간다
    // 영화삭제 테스트
    @Transactional // InvalidDataAccessApiUsageException: Executing an update/delete query 오류 해결
    @Test
    public void movieRemoveTest() {
        Movie movie = Movie.builder().mno(99L).build();
        // 이미지 삭제
        movieImageRepository.deleteByMovie(movie);
        // 리뷰 삭제
        reviewRepository.deleteByMovie(movie);
        // 영화 삭제
        movieRepository.delete(movie);
    }

    // movie_mno 를 이용해서 Review 가져오기
    @Transactional
    @Test
    public void findByMovieTest() {
        Movie movie = Movie.builder().mno(94L).build();
        List<Review> reviews = reviewRepository.findByMovie(movie);

        // LazyInitializationException: could not initialize proxy 가 나는 이유
        // => fetch = FetchType.LAZY 때문이다 : select review table 만 실행
        // Test 에서 이작업 실행하기 위해서 @Transactional 을 사용

        reviews.forEach(review -> {
            System.out.println(review);
            System.out.println(review.getMember().getEmail());
            System.out.println(review.getMember().getNickname());
        });
    }

    @Commit // @Transactional 으로 돌아가지 말고 @Commit 도 계속해조
    @Transactional
    @Test
    public void deleteByMemberTest() {
        // 회원탈퇴
        // 1. 리뷰삭제
        Member member = Member.builder().mid(2L).build();
        reviewRepository.deleteByMember(member);
        // 2. 회원삭제
        memberRepository.delete(member);
    }
}
