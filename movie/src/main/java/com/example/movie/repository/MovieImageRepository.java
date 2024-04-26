package com.example.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.repository.total.MovieImageReviewRepository;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long>, MovieImageReviewRepository {

    // 영화삭제(자식먼저 삭제해야한다, 이미지 와 리뷰 를 먼저 삭제)
    // DELETE FROM MOVIE_IMAGE mi WHERE movie_mno=1; ==> 이경우는 메소드를 따로 작성해야함
    // delete(), deleteById() ==> MovieImage 의 @Id 가 기준 이라서 movie_mno=1 에는 적용못한다
    @Modifying
    @Query("delete from MovieImage mi where mi.movie = ?1")
    void deleteByMovie(Movie movie);
}
