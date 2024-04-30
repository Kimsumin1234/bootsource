package com.example.movie.service;

import java.util.ArrayList;
import java.util.HashMap;
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

    // PageResultDto : 페이지 정보를 담아야 해서 (전체리스트 조회)
    PageResultDto<MovieDto, Object[]> getList(PageRequestDto pageRequestDto);

    // MovieDto : 영화 정보 1개만 보기 때문에 (행1개 조회)
    MovieDto getRow(Long mno);

    // 영화 삭제
    void movieRemove(Long mno);

    // 영화 등록
    Long movieInsert(MovieDto movieDto);

    // 영화 수정
    Long movieUpdate(MovieDto movieDto);

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
                .avg(avg != null ? avg : 0.0d)
                .build();

        // 영화 상세 조회 => 이미지를 모두 보여주기
        List<MovieImageDto> movieImageDtos = movieImages.stream().map(movieImage -> {
            return MovieImageDto.builder()
                    .inum(movieImage.getInum())
                    .uuid(movieImage.getUuid())
                    .imgName(movieImage.getImgName())
                    .path(movieImage.getPath())
                    .build();
        }).collect(Collectors.toList());

        movieDto.setMovieImageDtos(movieImageDtos);

        return movieDto;
    }

    // dtoToEntity
    // Map 은 key : value 형태로 담긴다
    // Map<String, Object> Object 를 사용한 이유 : 어떤거는 ("movie", movie) 담고,
    // 어떤거는 ("imgList", movieImages) 담아야해서 2개이상을 담아야 해서
    // 부모타입인 Object 를 사용
    public default Map<String, Object> dtoToEntity(MovieDto dto) {

        Map<String, Object> entityMap = new HashMap<>();

        // Movie Entity 생성
        Movie movie = Movie.builder()
                .mno(dto.getMno())
                .title(dto.getTitle())
                .build();
        // 생성된 Movie Entity 를 Map 에 담기 : put()
        entityMap.put("movie", movie);

        // List<MovieImageDto> movieImageDtos 를 List<MovieImage> 변환
        List<MovieImageDto> movieImageDtos = dto.getMovieImageDtos();

        // 1번쨰 방법
        // List<MovieImage> movieImages = new ArrayList<>();
        // if (movieImageDtos != null && movieImageDtos.size() > 0) {
        // for (MovieImageDto mDto : movieImageDtos) {
        // MovieImage movieImage = MovieImage.builder()
        // .imgName(mDto.getImgName())
        // .uuid(mDto.getUuid())
        // .path(mDto.getPath())
        // .build();
        // movieImages.add(movieImage);
        // }
        // }
        // entityMap.put("imgList", movieImages);

        // 2번쨰 방법
        if (movieImageDtos != null && movieImageDtos.size() > 0) {
            List<MovieImage> movieImages = movieImageDtos.stream().map(movieImageDto -> {
                MovieImage movieImage = MovieImage.builder()
                        .uuid(movieImageDto.getUuid())
                        .imgName(movieImageDto.getImgName())
                        .path(movieImageDto.getPath())
                        .movie(movie)
                        .build();
                return movieImage;

            }).collect(Collectors.toList());
            // 변환이 끝난 entity list 를 Map 담기 : put()
            entityMap.put("imgList", movieImages);
        }

        return entityMap;
    }
}
