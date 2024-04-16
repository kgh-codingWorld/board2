package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController // 모든 메서드의 리턴 타입은 기본으로 JSON을 사용
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; // 자동주입을 위한 final

    // {bno}: @PathVariable로 처리 -> 특정 게시물 번호로 조회할 때 특정 데이터를 변수로 처리하는 것이 가능
    @GetMapping(value="/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {
        // http의 상태코드를 전달하기 위해 ResponseEntity 객체로 반환

        log.info("bno:"+ bno);

        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
        // 댓글 등록: ajax를 이용한 POST 방식
        // @RequestBody: JSON으로 들어오는 데이터를 자동으로 해당 타입의 객체(ReplyDTO)로 매핑해줌

        log.info(replyDTO);

        Long rno = replyService.register(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    // {rno}: @PathVariable에서 처리해줌
    @PutMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {

        log.info("RNO: " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // {rno}: @PathVariable에서 처리해줌
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {

        log.info(replyDTO);

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
