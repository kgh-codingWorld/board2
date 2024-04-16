package org.zerock.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 직접적인 테이블용은 아님을 명시(테이블로 생성되지 않음), 실제 테이블은 BaseEntity를 상속한 엔티티 클래스로 DB 테이블이 생성됨
@Getter
@EntityListeners(value = {AuditingEntityListener.class}) // 세터 대신 사용하는 감시용 코드(데이터 변경 감지 -> 적용: 메인 메서드에 추가 코드 넣어줘야 함)
// JPA에서 사용하는 엔티티 객체들은 MyBatis와 달리 유지(콘텍스트에서 관리)되기 때문에 재사용이 가능, 엔티티 객체들에 일어나는 변화를 감지
// 이를 통해 regDate, modDate에 적절한 값이 지정됨
// AuditingEntityListener를 활성화시키기 위해 Application 클래스에 @EnableJpaAuditing 설정 추가

abstract class BaseEntity { // 테이블의 공통 부분을 상속시켜주는 클래스

    // @CreatedDate: JPA에서 엔티티의 생성 시간 처리, 칼럼값이 변경되지 않도록 updatable 속성을 false로 지정함
    @CreatedDate // 게시물 생성 시 동작
    @Column(name = "regdate", updatable = false) // db 테이블의 필드명 지정, 업데이트 안 되게 강제 적용시킴
    private LocalDateTime regDate; // 게시물 등록 시간

    // @LastModifiedDate: 최종 수정 시간을 자동으로 처리
    @LastModifiedDate // 게시물 수정 시 동작
    @Column(name = "moddate") // db 테이블의 필드명 지정
    private LocalDateTime modDate; // 게시물 수정 시간
} // 현재 사용하는 Guestbook의 엔티티 클래스 이외에도 다른 엔티티 클래스에서도 사용 가능 -> 코드 중복 방지
