package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    // 데이터베이스 조작을 위한 메서드들을 정의
    // 주로 CRUD(Create, Read, Update, Delete) 작업을 위한 메서드들이 포함

    // @Param: 메서드 시그니처 내에서 매개변수의 이름만으로는 해당 매개변수가 SQL 쿼리의 어떤 매개변수와 매핑되는지 명확하지 않을 수 있음
    // 특히 MyBatis와 같은 XML 기반의 SQL 매퍼에서는 이런 문제가 더욱 두드러짐
    // @Param 어노테이션을 사용하면 해당 매개변수가 어떤 역할을 하는지 명시적으로 지정할 수 있음

    // @Param("bno")은 MyBatis나 Spring Data JPA와 같은 ORM 프레임워크에서 매개변수를 매핑할 때 사용되는 어노테이션
    // bno라는 매개변수의 이름을 지정하는 역할
    // 스프링에서 매개변수로 받은 bno 값을 사용하여 게시판의 정보를 조회하는 메서드
    // 게시판의 번호(bno)를 받아 해당 게시판 정보를 조회하는 기능을 제공
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value ="SELECT b, w, count(r) " +
            " FROM Board b " +
            " LEFT JOIN b.writer w " +
            " LEFT JOIN Reply r ON r.board = b " +
            " GROUP BY b",
            countQuery ="SELECT count(b) FROM Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 댓글 페이징 때문에 Pageable 객체가 매개변수로 필요함


    @Query("SELECT b, w, count(r) " +
            " FROM Board b LEFT JOIN b.writer w " +
            " LEFT OUTER JOIN Reply r ON r.board = b" +
            " WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);


}