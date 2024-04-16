package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // Board 삭제 시에 댓글들도 삭제 처리
    @Modifying
    @Query("DELETE FROM Reply r WHERE r.board.bno =:bno ")
    void deleteByBno(Long bno);

    // 게시물로 모든 댓글 목록 순번대로 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
