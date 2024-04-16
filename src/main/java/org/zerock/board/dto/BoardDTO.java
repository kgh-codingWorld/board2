package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    // 데이터 전송을 위한 객체
    // 클라이언트와 서버 간의 데이터 전송에 사용
    // 컨트롤러와 서비스 사이에서 데이터를 전달하는 데 사용될 수 있음

    private Long bno;
    private String title;
    private String content;
    private String writerEmail; // 작성자 이메일(아이디)
    private String writerName; // 작성자 이름
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount; // 해당 게시글의 댓글 수
}
