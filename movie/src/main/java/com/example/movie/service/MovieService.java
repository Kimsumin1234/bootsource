package com.example.movie.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.movie.dto.MovieDto;
import com.example.movie.dto.MovieImageDto;
import com.example.movie.dto.PageRequestDto;
import com.example.movie.dto.PageResultDto;
import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;

public interface MovieService {

    PageResultDto<MovieDto, Object[]> getList(PageRequestDto pageRequestDto);

    // entityToDto
    // list 에 이런형태로 온다 [Movie(mno=61, title=Movie61), MovieImage(inum=176,
    // uuid=081029d2-5b9d-4d57-9e8c-fe2bb8306912, imgName=img0.jpg, path=null), 4,
    // 4.0]
    public default MovieDto entityToDto(Movie movie, List<MovieImage> movieImages, Long reviewCnt, Double avg) {
        MovieDto movieDto = MovieDto.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .createdDate(movie.getCreatedDate())
                .lastModifiedDate(movie.getLastModifiedDate())
                .reviewCnt(reviewCnt != null ? reviewCnt : 0)
                .avg(avg != null ? avg : 0)
                .build();

        // 영화 상세 조회 => 이미지를 모두 보여주기
        List<MovieImageDto> movieImageDtos = movieImages.stream().map(movieImage -> {
            return MovieImageDto.builder()
                    .inum(movieImage.getInum())
                    .uuid(movieImage.getUuid())
                    .imgName(movieImage.getImgName())
                    .path(movieImage.getImgName())
                    .build();
        }).collect(Collectors.toList());

        movieDto.setMovieImageDtos(movieImageDtos);

        return movieDto;
    }

    // dtoToEntity
    public default Map<String, Object> dtoToEntity(MovieDto dto) {
        return null;
    }
}
