package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 테이블 자동 관리
@Getter
@Builder
@AllArgsConstructor // 빌더패턴에 필수적임
@NoArgsConstructor // 빌더패턴에 필수적임
@ToString // 객체가 아닌 문자열로 변환시켜줌
public class GuestBook extends BaseEntity { // BaseEntity를 상속 받아 날짜를 동기화 시킴
    // 테이블 만드는 클래스 = 엔티티

    @Id // guestbook 테이블에 pk 지정
    // 기본 키(primary key) 필드에 대한 자동 생성 전략을 지정
    // 여기서 사용된 전략은 IDENTITY: 데이터베이스가 기본 키 필드의 값을 자동으로 생성하도록 하는 전략
    // auto-increment, auto-sequence 등의 기능을 사용하여 새로운 엔티티가 저장될 때마다 자동으로 기본 키 값을 증가시킴
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mariadb 용 자동 번호
    private Long gno; // 방명록에서 사용할 번호

    @Column(length = 100, nullable = false)
    private String title; // 제목

    @Column(length = 1500, nullable = false)
    private String content; // 내용

    @Column(length = 50, nullable = false)
    private String writer; // 작성자

    public void changeTitle(String title) {
        this.title = title; // 세터 역할(수정 시 사용)
    }

    public void changeContent(String content) {
        this.content = content; // 세터 역할(수정 시 사용)
    }
}
