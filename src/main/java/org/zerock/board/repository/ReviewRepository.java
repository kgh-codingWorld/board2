package org.zerock.board.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Mmember;
import org.zerock.board.entity.Movie;
import org.zerock.board.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // @EntityGraph를 이용해 Review 객체를 가져올 때 Mmember 객체를 로딩
    // @EntityGraph: 엔티티의 특정한 속성을 같이 로딩하도록 표시하는 어노테이션
    // 속성
    // attributePaths: 로딩 설정을 변경하고 싶은 속성의 아름을 배열로 명시
    // type: @EntityGraph를 어떤 방식으로 적용할 것인지 설정
    // FETCH: attributePaths에 명시한 속성은 EAGER로 처리, 나머지는 LAZY로 처리
    // LOAD: attributePaths에 명시한 속성은 EAGER로 처리, 나머지는 엔티티 클래스에 명시되거나 기본 방식으로 처리
    @EntityGraph(attributePaths = {"mmember"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie); // 특정 영화의 리뷰 찾기

    // 테이블에서 특정 회원을 삭제하려면 우선 review 테이블에서 먼저 삭제하고 mmember 테이블에서 삭제해야 함
    @Modifying
    @Query("DELETE FROM Review mr where mr.mmember =: mmember") // 한 번에 삭제
    void deleteByMmember(Mmember mmember);
}
