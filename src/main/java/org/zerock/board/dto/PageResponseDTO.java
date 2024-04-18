package org.zerock.board.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO<E> { // 다양한 곳에서 사용할 수 있도록 제네릭 타입을 사용하여 타입 지정
    // 페이징 처리 담당(결과)
    // JPA를 이용하는 Repository에서는 페이지 처리 결과를 Page<Entity> 타입으로 반환하는데 해당 클래스에서 이를 처리
    // Page<Entity>의 엔티티 객체들을 dto 객체로 변환해 자료구조로 담아주어야 함
    // 화면 출력에 필요한 페이지 정보들을 구성해 주어야 함


    private int page;
    private int size;
    private int total;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전, 다음
    private boolean prev, next;

    // DTO 리스트
    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){ // Page<Entity> 타입을 이용해 생성할 수 있도록 생성자로 작성

        if(total <= 0) {
            return;
        }
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; // 화면에서의 마지막 번호

        this.start = this.end - 9; // 화면에서의 시작 번호

        int last = (int)(Math.ceil((total/(double)size))); // 데이터의 개수를 계산한 마지막 페이지 번호

        this.end = end > last ? last: end;

        this.prev = this.start > 1;

        this.next = total > this.end * this.size;
    }

}
