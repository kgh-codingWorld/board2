package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.BoardSearch;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    // 데이터베이스 조작을 위한 메서드들을 정의
    // 주로 CRUD(Create, Read, Update, Delete) 작업을 위한 메서드들이 포함

    @Query(value="SELECT now()", nativeQuery = true)
    String getTime();

}