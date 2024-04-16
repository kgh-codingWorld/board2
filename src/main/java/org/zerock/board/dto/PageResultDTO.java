package org.zerock.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> { // 다양한 곳에서 사용할 수 있도록 제네릭 타입을 사용하여 타입 지정
    // 페이징 처리 담당(결과)
    // JPA를 이용하는 Repository에서는 페이지 처리 결과를 Page<Entity> 타입으로 반환하는데 해당 클래스에서 이를 처리
    // Page<Entity>의 엔티티 객체들을 dto 객체로 변환해 자료구조로 담아주어야 함
    // 화면 출력에 필요한 페이지 정보들을 구성해 주어야 함

    // DTO 리스트
    private List<DTO> dtoList;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전, 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private  List<Integer> pageList;
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){ // Page<Entity> 타입을 이용해 생성할 수 있도록 생성자로 작성
        // function: 엔티티 객체들을 DTO로 변환해주는 기능

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages(); // result는 Page<GuestBook>

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1; // 0부터 시작하기 때문에 1 추가
        this.size = pageable.getPageSize();

        // temp and page
        // Math.ceil은 소수점을 올림으로 처리함
        // 끝 번호 end는 아직 개선의 여지가 있음, 전체 데이터 수가 적으면 10페이지로 끝나면 안 되는 상황이 생길 수도 있음
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10; // 페이지 번호가 10개씩 보인다고 가정

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd: totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
