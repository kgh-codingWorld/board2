package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 자동으로 생성
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 자동으로 생성
@Data
public class ReplyDTO {
    // ReplyDTO를 Reply 엔티티로 처리하거나 반대의 경우에 대한 처리는 ReplyService 인터페이스, ReplyServiceImpl 클래스를 작성하여 처리

    private Long rno; // 댓글 번호
    private String text; // 댓글 내용
    private String replyer; // 댓글 작성자
    private Long bno; // 게시글 번호
    private LocalDateTime regDate, modDate; // 댓글 작성일 및 수정일
}
