package com.example.movie.service;

import java.util.List;

import com.example.movie.dto.ReviewDto;
import com.example.movie.entity.Member;
import com.example.movie.entity.Movie;
import com.example.movie.entity.Review;

public interface ReviewService {
    // 특정 영화의 모든 리뷰 가져오기
    List<ReviewDto> getListOfMovie(Long mno);

    // 특정 영화의 리뷰 등록
    Long addReview(ReviewDto reviewDto);

    // 리뷰 삭제
    void removeReview(Long reviewNo);

    // 리뷰 1개 가져오기
    ReviewDto getReview(Long reviewNo);

    // 리뷰 수정
    Long updateReview(ReviewDto reviewDto);

    // entityToDto
    public default ReviewDto entityToDto(Review review) {

        ReviewDto reviewDto = ReviewDto.builder()
                .reviewNo(review.getReviewNo())
                .grade(review.getGrade())
                .text(review.getText() != null ? review.getText() : "")
                .createdDate(review.getCreatedDate())
                .lastModifiedDate(review.getLastModifiedDate())
                .mid(review.getMember().getMid())
                .email(review.getMember().getEmail())
                .nickname(review.getMember().getNickname())
                .mno(review.getMovie().getMno())
                .build();

        return reviewDto;
    }

    // dtoToEntity
    public default Review dtoToEntity(ReviewDto reviewDto) {
        Member member = Member.builder().mid(reviewDto.getMid()).build();
        Movie movie = Movie.builder().mno(reviewDto.getMno()).build();

        // Review review = Review.builder()
        // .reviewNo(reviewDto.getReviewNo())
        // .grade(reviewDto.getGrade())
        // .text(reviewDto.getText())
        // .member(member)
        // .movie(movie)
        // .build();

        // 수정후 날짜가 null로 반영이 되서
        // reviewDto.getCreatedDate() 이거를 담을려면 builder() 방식은 사용못해서 이방법 사용
        Review review = new Review();
        review.setReviewNo(reviewDto.getReviewNo());
        review.setGrade(reviewDto.getGrade());
        review.setText(reviewDto.getText());
        review.setCreatedDate(reviewDto.getCreatedDate());
        review.setMember(member);
        review.setMovie(movie);

        return review;
    }
}
