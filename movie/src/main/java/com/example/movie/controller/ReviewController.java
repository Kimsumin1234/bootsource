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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

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

    // /3 + POST => 리뷰번호 리턴
    @PostMapping("/{mno}")
    public ResponseEntity<Long> postReview(@RequestBody ReviewDto reviewDto) {
        log.info("ReviewController postReview {} 요청", reviewDto);

        Long reviewNo = reviewService.addReview(reviewDto);

        return new ResponseEntity<>(reviewNo, HttpStatus.OK);
    }

    // 4) 컨트롤러에서 작성자와 로그인 유저가 같은지 다시 한번 비교
    // => @PreAuthorize("authentication.name == #email") String email
    @PreAuthorize("authentication.name == #email")
    @DeleteMapping("/{mno}/{reviewNo}")
    public ResponseEntity<Long> postRemove(@PathVariable("reviewNo") Long reviewNo, String email) {
        log.info("리뷰 삭제 요청 {}", reviewNo);

        reviewService.removeReview(reviewNo);

        return new ResponseEntity<>(reviewNo, HttpStatus.OK);

    }

    // /299/5 + GET
    @GetMapping("/{mno}/{reviewNo}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable("reviewNo") Long reviewNo) {
        log.info("리뷰 1개 요청 {}", reviewNo);

        return new ResponseEntity<>(reviewService.getReview(reviewNo), HttpStatus.OK);
    }

    // 리뷰수정
    // 컨트롤러에서 작성자와 로그인 유저가 같은지 다시 한번 비교
    @PreAuthorize("authentication.name == #reviewDto.email")
    @PutMapping("/{mno}/{reviewNo}")
    public ResponseEntity<Long> putReview(@PathVariable("reviewNo") Long reviewNo, @RequestBody ReviewDto reviewDto) {

        log.info("리뷰 수정 {}", reviewDto);

        Long rno = reviewService.updateReview(reviewDto);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

}
