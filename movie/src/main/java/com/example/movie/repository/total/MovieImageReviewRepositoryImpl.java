package com.example.movie.repository.total;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.entity.QMovie;
import com.example.movie.entity.QMovieImage;
import com.example.movie.entity.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MovieImageReviewRepositoryImpl extends QuerydslRepositorySupport implements MovieImageReviewRepository {

    public MovieImageReviewRepositoryImpl() {
        // extends 할 클래스를 써준다
        super(MovieImage.class);
    }

    // @Override
    // public Page<Object[]> getTotalList(Pageable pageable) {
    // log.info("==== querydsl getTotalList ====");
    // // 1. 일단 Q 클래스 가져오기
    // QMovie movie = QMovie.movie;
    // QReview review = QReview.review;
    // QMovieImage movieImage = QMovieImage.movieImage;

    // // 2024-04-25 디비버 참고해서 작성
    // // 2. 기준이 되는 클래스 는 From 절
    // JPQLQuery<MovieImage> query = from(movieImage);
    // // movieImage.movie.eq(movie) : MovieImage(엔티티) 에 Movie 랑 Movie (엔티티) 같으면
    // // leftJoin(movie,movieImage.movie) 이렇게 해도 된다
    // query.leftJoin(movie).on(movieImage.movie.eq(movie));

    // // JPAExpressions : 서브 쿼리는 from 절에서 사용을 못하고 select 절이나 where 절에서 사용
    // // m.CREATED_DATE , m.TITLE , m.mno : movie 로 퉁친다 (movieImage 도 동일)
    // JPQLQuery<Tuple> tuple = query.select(movie, movieImage,
    // JPAExpressions.select(review.countDistinct()).from(review).where(review.movie.eq(movieImage.movie)),
    // JPAExpressions.select(review.grade.avg().round()).from(review)
    // .where(review.movie.eq(movieImage.movie)))
    // .where(movieImage.inum
    // .in(JPAExpressions.select(movieImage.inum.min()).from(movieImage).groupBy(movieImage.movie)));
    // // 3. 정렬을 다른기준으로 사용할수 있기 때문에 여기서 정하지 않는다
    // // .orderBy(movie.mno.desc());

    // // 정렬 기준이 바뀔경우에 대비하기 위한 코드 (코드를 유연하게 작성하기 위한 방법)
    // // 페이지 나누기
    // // Pageable pageable = requestDto.getPageable(Sort.by("gno").descending());
    // 이게
    // // 바로 안된다
    // // sort 지정 코드
    // Sort sort = pageable.getSort(); // 여러개의 테이블 sort 방식을 다 가져옴
    // sort.stream().forEach(order -> {
    // // 정렬방식이 ASC 인지 DESC 인지 지정
    // Order direction = order.isAscending() ? Order.ASC : Order.DESC;
    // String prop = order.getProperty();
    // // 여기서는 Movie 를 기준으로 하기로함 (기준만 변경하면 다른대서도 사용가능)
    // PathBuilder<Movie> orderByExpression = new PathBuilder<>(Movie.class,
    // "movie");
    // tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
    // });

    // // 4. 페이지 처리
    // tuple.offset(pageable.getOffset());
    // tuple.limit(pageable.getPageSize());

    // List<Tuple> result = tuple.fetch(); // 4. fetch() : 실행결과 가져오세요
    // long count = tuple.fetchCount();
    // return new PageImpl<>(result.stream().map(t ->
    // t.toArray()).collect(Collectors.toList()), pageable, count);
    // }

    // 검색 추가
    @Override
    public Page<Object[]> getTotalList(String type, String keyword, Pageable pageable) {
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
                        .in(JPAExpressions.select(movieImage.inum.min()).from(movieImage).groupBy(movieImage.movie)));
        // 3. 정렬을 다른기준으로 사용할수 있기 때문에 여기서 정하지 않는다
        // .orderBy(movie.mno.desc());

        // 검색 조건
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(movie.mno.gt(0L));

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if (type.contains("t")) {
            conditionBuilder.or(movie.title.contains(keyword));
        }
        builder.and(conditionBuilder);
        tuple.where(builder);

        // 정렬 기준이 바뀔경우에 대비하기 위한 코드 (코드를 유연하게 작성하기 위한 방법)
        // 페이지 나누기
        // Pageable pageable = requestDto.getPageable(Sort.by("gno").descending()); 이게
        // 바로 안된다
        // sort 지정 코드
        Sort sort = pageable.getSort(); // 여러개의 테이블 sort 방식을 다 가져옴
        sort.stream().forEach(order -> {
            // 정렬방식이 ASC 인지 DESC 인지 지정
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            // 여기서는 Movie 를 기준으로 하기로함 (기준만 변경하면 다른대서도 사용가능)
            PathBuilder<Movie> orderByExpression = new PathBuilder<>(Movie.class, "movie");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        // 4. 페이지 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch(); // 4. fetch() : 실행결과 가져오세요
        long count = tuple.fetchCount();
        return new PageImpl<>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }

    // 위에꺼 에서 복사 떠옴
    @Override
    public List<Object[]> getMovieRow(Long mno) {
        log.info("==== querydsl getMovieRow ====");
        // 1. 일단 Q 클래스 가져오기
        QMovie movie = QMovie.movie;
        QReview review = QReview.review;
        QMovieImage movieImage = QMovieImage.movieImage;

        // 2024-04-26 디비버 참고해서 작성
        // 2. 기준이 되는 클래스 는 From 절
        JPQLQuery<MovieImage> query = from(movieImage);
        query.leftJoin(movie).on(movieImage.movie.eq(movie));

        JPQLQuery<Tuple> tuple = query.select(movie, movieImage,
                JPAExpressions.select(review.countDistinct()).from(review).where(review.movie.eq(movieImage.movie)),
                JPAExpressions.select(review.grade.avg().round()).from(review)
                        .where(review.movie.eq(movieImage.movie)))
                .where(movieImage.movie.mno.eq(mno)) // movieImage.movie.mno : Long mno 을 사용해서
                .orderBy(movieImage.inum.desc());

        List<Tuple> result = tuple.fetch();
        return result.stream().map(tup -> tup.toArray()).collect(Collectors.toList());
    }

}
