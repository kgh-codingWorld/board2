package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO { // 페이지 요청 처리
    // 목록 페이지를 요청할 때 사용하는 데이터를 재사용하기 쉽게 만드는 클래스
    // 목록 화면: 페이지 번호, 페이지 내 목록 개수, 검색 조건 등이 많이 사용됨(파라미터)
    // page와 size 파라미터를 수집하는 역할
    // **JPA 쪽에서 사용하는 Pageable 타입의 객체를 생성하는 것이 진짜 목적**

    private int page; // 화면에서 전달됨
    private int size; // 화면에서 전달됨
    private String type; // 서버 사이드 처리
    private String keyword; // 서버 사이드 처리

    public PageRequestDTO() { // jpa 쪽에서 사용하는 Pageable 타입의 객체를 생성하는데 쓰임

        this.page = 1; // 기본값
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){

        // 1페이지의 경우 0이 될 수 있도록 -1로 작성함
        return PageRequest.of(page - 1, size, sort);
    }
}
