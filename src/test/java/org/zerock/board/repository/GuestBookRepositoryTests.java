package org.zerock.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.dto.GuestBookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.GuestBook;
import org.zerock.board.entity.QGuestBook;
import org.zerock.board.service.GuestBookService;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestBookRepositoryTests {

    @Autowired
    private GuestBookRepository guestBookRepository;

    @Autowired
    private GuestBookService service;

    @Test
    public void insertDummies(){ // 테이블에 더미데이터 300개 추가
        IntStream.rangeClosed(1,300).forEach(i -> {
            GuestBook guestBook = GuestBook.builder()
                    .title("제목"+i)
                    .content("내용"+i)
                    .writer("작성자"+(i%10)) // 작성자 0 ~ 30
                    .build();
            System.out.println(guestBookRepository.save(guestBook));
            //jpa save 메서드를 실행하며 출력까지 진행
        });
    }

    @Test
    public void updateTest(){ // 게시물 수정 테스트
        Optional<GuestBook> result = guestBookRepository.findById(300L); // 300번 게시물 찾기 -> 찾아온 게시물이 result로 들어감

        if (result.isPresent()) { // result 안에 값이 있는가?
            GuestBook guestBook = result.get(); // 엔티티를 가져와 변수에 넣음
            System.out.println(guestBook.getTitle());
            System.out.println(guestBook.getWriter());
            guestBook.changeTitle("수정된 제목..."); // 엔티티에서 만든 메서드
            guestBook.changeContent("수정된 내용..."); // 엔티티에서 만든 메서드
            guestBookRepository.save(guestBook); // 있으면 update, 없으면 insert(자동)
        }
    }

    @Test
    public void testQuery1(){ //단일 조건으로 쿼리 생성 + 페이징 + 정렬

        // 페이지 타입으로 요청 처리(0번 페이지에 10개씩 객체 생성, gno를 기준으로 내림차순 정렬)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestBook qGuestBook = QGuestBook.guestBook; // Querydsl용 객체 생성(동적처리용)

        String keyword = "9"; // 프론트 페이지에서 1번을 찾겠다는 변수

        BooleanBuilder builder = new BooleanBuilder(); // 다중 조건 처리용 객체

        BooleanExpression expressionTitle = qGuestBook.title.contains(keyword); // expression(표현식) title=1
        BooleanExpression expressionContent = qGuestBook.content.contains(keyword);
        BooleanExpression exAll = expressionTitle.or(expressionContent);

        builder.and(exAll); // 다중 조건 처리용 객체에 표현식을 밀어넣음
        builder.and(qGuestBook.gno.gt(0L)); // while문 추가(gno 0보다 크다)

        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable); // 페이지 타입의 객체가 나옴(.findAll(): 검색된 모든 객체가 나옴)

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

    @Test
    public void testRegister() {

        GuestBookDTO guestBookDTO = GuestBookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content")
                .writer("user0")
                .build();
        System.out.println(service.register(guestBookDTO));
    }

    @Test
    public void testSearch() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc") // 검색 조건
                .keyword("한글") // 검색 키워드
                .build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: " + resultDTO.isPrev());
        System.out.println("NEXT: " + resultDTO.isNext());
        System.out.println("TOTAL: " + resultDTO.getTotalPage());

        System.out.println("--------------------------------");
        for(GuestBookDTO guestBookDTO : resultDTO.getDtoList()){
            System.out.println(guestBookDTO);
        }

        System.out.println("================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }
}
