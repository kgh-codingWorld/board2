package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Movie;
import org.zerock.board.entity.Review;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> { // 평점 데이터 처리

    //    @Query("select m, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
//            "left outer join Review  r on r.movie = m group by m")
//    Page<Object[]> getListPage(Pageable pageable);

    // Movie 객치와 MovieImage 객체 하나, double 값으로 나오는 영화의 평균 평점/long 타입의 리뷰 개수를 Object[]로 반환
    @Query("select m, mi, avg(coalesce(r.grade,0)),  count(r) from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review  r on r.movie = m group by m ")
    Page<Object[]> getListPage(Pageable pageable); // 페이지 처리


    // 특정 영화의 모든 이미지와 평균 평점/리뷰 개수
    @Query("select m, mi ,avg(coalesce(r.grade,0)),  count(r)" +
            " from Movie m left outer join MovieImage mi on mi.movie = m " +
            " left outer join Review  r on r.movie = m "+
            " where m.mno = :mno group by mi") // 영화 이미지별로 그룹을 만들어 영화 이미지들 개수만큼 데이터를 만들어 냄
    List<Object[]> getMovieWithAll(Long mno); // 특정 영화 조회
}
