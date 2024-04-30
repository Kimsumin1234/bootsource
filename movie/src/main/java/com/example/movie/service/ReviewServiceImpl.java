package com.example.movie.service;

import java.util.List;
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

}
