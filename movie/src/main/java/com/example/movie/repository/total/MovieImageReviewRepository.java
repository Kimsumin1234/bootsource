package com.example.movie.repository.total;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieImageReviewRepository {

    // Movie, MovieImage, Review 여러개를 뽑아야하면 Object[] 로 받는다
    // 전체 영화 리스트
    // Page : 페이지 정보가 담기기 때문에 사용
    Page<Object[]> getTotalList(Pageable pageable);

    // 특정 영화 조회
    // List : 영화이미지가 여러개 담길수 있기 때문에 사용
    List<Object[]> getMovieRow(Long mno);
}
