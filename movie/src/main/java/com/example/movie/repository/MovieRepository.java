package com.example.movie.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.movie.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // @Query("select m, avg(r.grade), count(distinct r) from Movie m left join
    // Review r on r.movie = m group by m") // 오라클은 엔티티 2개 까지는 가능

    // @Query("select m, min(mi), avg(r.grade), count(distinct r) from Movie m left
    // join MovieImage mi on mi.movie = m " +
    // "left join Review r on r.movie = m group by m")
    // Page<Object[]> getListPage2(Pageable pageable); // 오라클은 엔티티 3개 는 불가능(다른 DB는
    // 가능한게 있음)

    // nativeQuery = true : 이걸 사용해서 지금 오라클 sql문을 그대로 사용함 (테이블3개 조인)
    @Query(value = "SELECT * FROM MOVIE m LEFT JOIN " +
            "(SELECT r.MOVIE_MNO, COUNT(*) ,AVG(r.GRADE) FROM REVIEW r GROUP BY r.MOVIE_MNO) r1 " +
            "ON m.MNO = r1.movie_mno LEFT JOIN (SELECT mi2.* FROM MOVIE_IMAGE mi2 WHERE mi2.inum " +
            "IN (SELECT MIN(mi.INUM) FROM MOVIE_IMAGE mi GROUP BY mi.MOVIE_MNO)) A ON m.mno = A.movie_mno ORDER BY mno desc", nativeQuery = true)
    Page<Object[]> getListPage(Pageable pageable);
}
