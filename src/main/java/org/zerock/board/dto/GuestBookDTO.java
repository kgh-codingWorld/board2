package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestBookDTO { // 각 계층끼리 주고받는 우편물 개념, 목적 자체가 데이터의 전달(읽고 쓰기 가능, 일회성)
    // 서비스 계층에서 DTO로 파라미터와 리턴 타입을 처리하도록 구성할 예정
    // DTO를 사용하면 엔티티 객체의 범위를 한정지을 수 있음(안전성 확보)
    // 단점: Entity와 유사한 코드를 중복으로 개발, 엔티티 객체를 DTO로 변환하거나 반대로 DTO 객체를 엔티티로 변환하는 과정이 필요
    // getter, setter를 통해 자유로이 값을 변경할 수 있음
    private Long gno; // 방명록 번호
    private String title; // 방명록 제목
    private String content; // 방명록 내용
    private String writer; // 방명록 작성자
    private LocalDateTime regDate, modDate; // 방명록 작성 일자
}
