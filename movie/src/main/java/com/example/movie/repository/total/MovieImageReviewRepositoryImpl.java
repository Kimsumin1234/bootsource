package com.example.movie.repository.total;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.movie.entity.MovieImage;
import com.example.movie.entity.QMovie;
import com.example.movie.entity.QMovieImage;
import com.example.movie.entity.QReview;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MovieImageReviewRepositoryImpl extends QuerydslRepositorySupport implements MovieImageReviewRepository {

    public MovieImageReviewRepositoryImpl() {
        // extends 할 클래스를 써준다
        super(MovieImage.class);
    }

    @Override
    public Page<Object[]> getTotalList(Pageable pageable) {
        log.info("==== querydsl getTotalList ====");
        // 1. 일단 Q 클래스 가져오기
        QMovie movie = QMovie.movie;
        QReview review = QReview.review;
        QMovieImage movieImage = QMovieImage.movieImage;

        // 2024-04-25 디비버 참고해서 작성
        // 2. 기준이 되는 클래스 는 From 절
        JPQLQuery<MovieImage> query = from(movieImage);
        // movieImage.movie.eq(movie) : MovieImage(엔티티) 에 Movie 랑 Movie (엔티티) 같으면
        // leftJoin(movie,movieImage.movie) 이렇게 해도 된다
        query.leftJoin(movie).on(movieImage.movie.eq(movie));

        // JPAExpressions : 서브 쿼리는 from 절에서 사용을 못하고 select 절이나 where 절에서 사용
        // m.CREATED_DATE , m.TITLE , m.mno : movie 로 퉁친다 (movieImage 도 동일)
        JPQLQuery<Tuple> tuple = query.select(movie, movieImage,
                JPAExpressions.select(review.countDistinct()).from(review).where(review.movie.eq(movieImage.movie)),
                JPAExpressions.select(review.grade.avg().round()).from(review)
                        .where(review.movie.eq(movieImage.movie)))
                .where(movieImage.inum
                        .in(JPAExpressions.select(movieImage.inum.min()).from(movieImage).groupBy(movieImage.movie)))
                .orderBy(movie.mno.desc());

        // 3. 페이지 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch(); // 4. fetch() : 실행결과 가져오세요
        long count = tuple.fetchCount();
        return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }

}
