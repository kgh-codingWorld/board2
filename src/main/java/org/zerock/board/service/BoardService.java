package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResponseDTO;

public interface BoardService {
    // 비즈니스 로직을 포함
    // 컨트롤러와 리포지토리 사이에서 중간 계층으로 사용
    // 여러 리포지토리를 조합하거나 트랜잭션 관리
    // 객체 변환

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}
