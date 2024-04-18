package org.zerock.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResponseDTO;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Test...")
                .content("Test...")
                .writer("user55@aaa.com") // 현재 데이터베이스에 존재하는 회원 이메일
                .build();
        Long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated...101")
                .content("Updated content 101...")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }
}
