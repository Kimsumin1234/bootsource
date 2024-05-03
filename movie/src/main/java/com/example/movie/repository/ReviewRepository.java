package com.example.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

import com.example.movie.entity.Member;
import com.example.movie.entity.Movie;
import com.example.movie.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 영화삭제(자식먼저 삭제해야한다, 이미지 와 리뷰 를 먼저 삭제)
    // DELETE FROM REVIEW r WHERE movie_mno=1; ==> 이경우는 메소드를 따로 작성해야함
    // delete(), deleteById() ==> REVIEW 의 @Id 가 기준 이라서 movie_mno=1 에는 적용못한다
    @Modifying
    @Query("delete from Review r where r.movie = ?1")
    void deleteByMovie(Movie movie);

    // movie_mno 를 이용해서 리뷰 가져오기
    // 리뷰 100개에 해당하는 select 구문을 100번 돌려서 가져오기는 비효율적이다
    // @EntityGraph 를 사용해서 member table 과 join 에서 처리한다(이구문 할때만 LAZY 로 안하고 같이가져옴)
    // 기본은 LAZY 가 맞으나, 한 화면에 리뷰정보와 멤버 정보가 같이 필요하기 때문
    // @EntityGraph 를 사용안하면 select 를 여러번 하지만
    // @EntityGraph 를 사용하면 join 구문 한번으로 처리가 가능하다
    @EntityGraph(attributePaths = { "member" }, type = EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // @Query("delete from review r where r.member = ?1", nativeQuery=true) sql 쿼리문
    // @Query를 안하면 delete from review where review_no=? 이 구문이 여러번 실행(비효율)
    // member_mno 를 이용하면 한번에 삭제 가능
    @Modifying
    @Query("delete from Review r where r.member = ?1")
    void deleteByMember(Member member);
}
