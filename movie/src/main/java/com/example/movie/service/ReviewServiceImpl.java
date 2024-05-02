package com.example.movie.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.movie.dto.ReviewDto;
import com.example.movie.entity.Movie;
import com.example.movie.entity.Review;
import com.example.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDto> getListOfMovie(Long mno) {
        log.info("ReviewServiceImpl getListOfMovie {}", mno);

        Movie movie = Movie.builder().mno(mno).build();

        List<Review> reviews = reviewRepository.findByMovie(movie);

        // List<Review> => List<ReviewDto>
        Function<Review, ReviewDto> fn = review -> entityToDto(review);
        return reviews.stream().map(fn).collect(Collectors.toList());
    }

    @Override
    public Long addReview(ReviewDto reviewDto) {
        Review review = dtoToEntity(reviewDto);

        return reviewRepository.save(review).getReviewNo();
    }

    @Override
    public void removeReview(Long reviewNo) {
        reviewRepository.deleteById(reviewNo);
    }

    @Override
    public ReviewDto getReview(Long reviewNo) {
        return entityToDto(reviewRepository.findById(reviewNo).get());
    }

    @Override
    public Long updateReview(ReviewDto reviewDto) {
        Optional<Review> result = reviewRepository.findById(reviewDto.getReviewNo());
        if (result.isPresent()) {
            Review review = result.get();
            review.setGrade(reviewDto.getGrade());
            review.setText(reviewDto.getText());
            reviewRepository.save(dtoToEntity(reviewDto));
        }
        return reviewDto.getReviewNo();

        // save() 호출을 하면
        // 내부적으로 select 를 자동으로 한다
        // 그래서 이걸 insert 할지 update를 할지 자동으로 결정해준다 (데이터가 변한게 있으면 update)
        // return reviewRepository.save(dtoToEntity(reviewDto)).getReviewNo();
    }

}
