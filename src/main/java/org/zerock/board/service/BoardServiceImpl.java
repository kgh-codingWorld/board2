package org.zerock.board.service;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.*;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;
import org.zerock.board.service.BoardService;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {
    // 서비스 인터페이스의 구현체

    private final BoardRepository repository; // 게시물 가져오는 쿼리문

    private final ReplyRepository replyRepository; // 댓글 가져오는 쿼리문


    // 등록
    @Override
    public Long register(BoardDTO dto) { // 컨트롤러에서 전송 받은 DTO 객체 사용

        log.info(dto);

        Board board  = dtoToEntity(dto); // Board 엔티티(테이블) 타입의 변수에 DTO 객체를 담은 Service의 dtoToEntity()를 넣음

        repository.save(board); // 게시물 가져오는 쿼리문에 엔티티 객체 타입으로 변환하여 board 객체 저장

        return board.getBno(); // Long 타입의 게시물 번호를 반환
    }

    // 목록 조회
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) { // 조회 페이지 요청하여 결과 페이지로 반환

        log.info(pageRequestDTO);

        // Function<Object[], BoardDTO>은 입력으로 Object 배열을 받아들이고 BoardDTO를 반환하는 함수
        // Function<>: 해당 메서드에는 람다식 때문에 사용함
        // en은 입력 파라미터(Object의 배열)
        // en 배열의 첫 번째 요소를 Board, 두 번째 요소를 Member, 세 번째 요소를 Long으로 형변환하고, 이들을 entityToDTO 함수에 전달하는 역할
        // 람다식을 사용하여 Object 배열을 입력으로 받아 BoardDTO 객체를 반환하는 함수를 정의
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));

//        Page<Object[]> result = repository.getBoardWithReplyCount(
//                pageRequestDTO.getPageable(Sort.by("bno").descending())  );
        // repository에서 페이지별로 데이터를 검색하는 기능을 수행
        // 인자로는 검색을 위한 type, keyword, 그리고 페이지 요청 정보를 나타내는 Pageable 객체가 전달됨
        // Page<Object[]>: 페이징 처리 결과를 나타내는 클래스, 객체 자체는 배열이 아니라 페이지 정보를 담고 있는 객체
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()) // 페이지 요청 정보를 생성하는 메서드
                // 페이지 번호와 페이지 당 항목 수를 가져와서 Sort 객체를 이용하여 정렬 방식을 지정한 후, Pageable 객체를 생성
        );


        return new PageResultDTO<>(result, fn);
    }

    // 상세보기
    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno); // 게시물 번호로 게시물 한 개 가져옴

        Object[] arr = (Object[])result; // 일반 Object 객체 타입의 result를 배열로 형변환함

        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
        // entityToDTO() 메서드를 호출하여 엔티티 객체들을 DTO 객체로 변환
        // 배열의 각 요소를 엔티티 객체(Board, Member, Long)로 강제 형변환하여 메서드에 전달
        // 이를 통해 게시물 정보를 담은 BoardDTO 객체를 반환
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        //댓글 부터 삭제
        replyRepository.deleteByBno(bno);

        repository.deleteById(bno);

    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = repository.getOne(boardDTO.getBno());

        if(board != null) {

            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            repository.save(board);
        }
    }
}