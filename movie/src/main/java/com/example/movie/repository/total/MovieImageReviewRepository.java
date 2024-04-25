package com.example.movie.repository.total;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieImageReviewRepository {

    // Movie, MovieImage, Review 여러개를 뽑아야하면 Object[] 로 받는다
    Page<Object[]> getTotalList(Pageable pageable);
}
