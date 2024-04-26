package com.example.movie.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movie.dto.MovieDto;
import com.example.movie.dto.PageRequestDto;
import com.example.movie.dto.PageResultDto;
import com.example.movie.entity.Movie;
import com.example.movie.entity.MovieImage;
import com.example.movie.repository.MovieImageRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public PageResultDto<MovieDto, Object[]> getList(PageRequestDto pageRequestDto) {

        Pageable pageable = pageRequestDto.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieImageRepository.getTotalList(pageable);

        // entity[0] 이렇게 담으면 Object[] 로 담은 거기 때문에 캐스팅을 사용해서 형변환을 해준다
        // List는 (List<MovieImage>) 이렇게 한다고 바로 변환해주지 않아서 Arrays.asList() 도 같이 사용
        Function<Object[], MovieDto> fn = (entity -> entityToDto((Movie) entity[0],
                (List<MovieImage>) Arrays.asList((MovieImage) entity[1]),
                (Long) entity[2], (Double) entity[3]));

        return new PageResultDto<>(result, fn);
    }

    @Override
    public MovieDto getRow(Long mno) {
        // MovieImage 만 여러개 들어오고 나머지는 동일한값이다
        List<Object[]> list = movieImageRepository.getMovieRow(mno);

        // list.get(0) : List<Object[]> 에 0번 index 를 가져오게되면
        // => [Movie(mno=100, title=Movie100), MovieImage(inum=303,
        // uuid=30218f43-fa14-41e2-b3de-a2d63a4686f8, imgName=img4.jpg, path=null), 2,
        // 3.0]

        // 동일한 값이라서 1번만 담으면된다
        // List 로 들어오기 때문에 1번 더 요소를 들어가준다 (Object[] 로 들어오면 1번만 하면되는데)
        Movie movie = (Movie) list.get(0)[0]; // Object[] 여서 (Movie) 해준다
        Long reviewCnt = (Long) list.get(0)[2];
        Double avg = (Double) list.get(0)[3];

        // MovieImage 는 여러개가 들어올수있다
        // MovieDto 에 private List<MovieImageDto> movieImageDtos = new ArrayList<>() 있음
        List<MovieImage> movieImages = new ArrayList<>();
        list.forEach(arr -> {
            MovieImage movieImage = (MovieImage) arr[1];
            movieImages.add(movieImage);
        });
        // 동일한걸 stream() 으로 사용해보기
        // List<MovieImage> movieImages = list.stream().map(entity -> (MovieImage)
        // entity[1]).collect(Collectors.toList());

        log.info("==== MovieServiceImpl getRow ====");
        log.info("entityToDto : {}", entityToDto(movie, movieImages, reviewCnt, avg));

        return entityToDto(movie, movieImages, reviewCnt, avg);
    }

    @Transactional
    @Override
    public void movieRemove(Long mno) {
        Movie movie = Movie.builder().mno(mno).build();
        movieImageRepository.deleteByMovie(movie);
        reviewRepository.deleteByMovie(movie);
        movieRepository.delete(movie);
    }

}
