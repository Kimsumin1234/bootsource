package com.example.movie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.dto.ReviewDto;
import com.example.movie.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // /3/all
    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDto>> getListReview(@PathVariable("mno") Long mno) {
        log.info("ReviewController getListReview {} 요청", mno);

        List<ReviewDto> reviewDtos = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

}
