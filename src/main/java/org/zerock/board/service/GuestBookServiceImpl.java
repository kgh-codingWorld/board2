package org.zerock.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.GuestBookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.GuestBook;
import org.zerock.board.entity.QGuestBook;
import org.zerock.board.repository.GuestBookRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.function.Function;

@Service // 스프링에서 빈으로 처리되도록 함
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestBookServiceImpl implements GuestBookService { // dto로 파라미터와 리턴 타입 처리
    // dto 사용의 장점: 엔티티 객체의 범위를 한정 짓기 가능 -> 안전한 코드, 화면과 데이터 분리 취지에 부합
    // dto 사용의 단점: 엔티티와 유사한 코드를 중복으로 개발, 엔티티 객체를 dto로 변환하거나 dto 객체를 엔티티로 변환(JPA로 처리하기 위함)하는 과정이 필요
    // 화면까지 전달되는 데이터는 PageResultDTO, 이를 이용해서 화면에서는 페이지 처리를 진행하게 될 것임
    // PageResultDTO 타입으로 처리된 결과에는 시작 페이지, 끝 페이지 등 필요한 모든 정보를 담아 화면에서는 필요한 내용들만 찾아 구성 가능하도록 작성

    // JPA 처리를 위해 주입
    private final GuestBookRepository repository; //반드시 final로 선언해야 함

    // 방명록 등록
    @Override
    public Long register(GuestBookDTO dto) {

        log.info("DTO--------------");
        log.info(dto);

        GuestBook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity); // 저장

        return entity.getGno(); // 엔티티가 가지는 gno 값 반환
    }

    // 방명록 리스트
    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {
        // entityToDTO를 이용해 Function을 생성하고 이를 PageResultDTO로 구성함
        // PageResultDTO에는 jpa의 처리 결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을 DTO의 리스트로 변환하고 화면에 페이지 처리와 필요한 값 생성

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리

        // repository는 Querydsl로 작성된 BooleanBuilder를 findAll() 처리하는 용도로 사용함
        Page<GuestBook> result = repository.findAll(booleanBuilder, pageable); // Querydsl 사용

        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    } // entityToDTO()를 이용해서 java.util.Function 생성하고 PageResultDTO로 구성함

    // 방명록 조회
    @Override
    public GuestBookDTO read(Long gno) {

        Optional<GuestBook> result = repository.findById(gno);

        return result.isPresent()? entityToDTO(result.get()): null;
    }

    // 방명록 삭제
    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    // 방명록 수정
    @Override
    public void modify(GuestBookDTO dto) {
        // 업데이트 항목 : 제목, 내용
        Optional<GuestBook> result = repository.findById(dto.getGno()); // PK인 dto.getGno()를 이용해 방명록 글을 찾음

        if(result.isPresent()) { // isPresent(): 값이 존재하면 true를, 존재하지 않으면 false를 반환
            GuestBook entity = result.get();

            entity.changeTitle(dto.getTitle()); // 수정된 제목 넣기
            entity.changeContent(dto.getContent()); // 수정된 내용 넣기

            repository.save(entity); // 다시 저장
        }
    }
    // PageResultDTO에는 JPQ의 처리 결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을 DTO의 리스트로 변환하고 화면에 페이지 처리와 필요한 값들 처리

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) { // Querydsl 처리

        // // requestDTO 객체에서 type 속성을 가져와서 type이라는 이름의 String 변수에 할당
        String type = requestDTO.getType();

        //  Querydsl 라이브러리의 일부인 BooleanBuilder의 새 인스턴스를 생성, 쿼리를 구성하는 데 사용
        BooleanBuilder booleanBuilder = new BooleanBuilder(); // Querydsl을 통해 BooleanBuilder를 작성

        // QGuestBook 클래스에 대한 별칭인 qGuestBook을 생성
        // GuestBook 엔티티를 나타내는 Querydsl에서 생성된 클래스, 이 별칭은 쿼리에서 GuestBook 엔티티의 속성을 참조하는 데 사용
        QGuestBook qGuestBook = QGuestBook.guestBook;

        // requestDTO 객체에서 keyword 속성을 가져와서 keyword라는 이름의 String 변수에 할당
        String keyword = requestDTO.getKeyword();

        // GuestBook 엔티티에 대한 gno > 0 조건을 나타내는 BooleanExpression인 expression을 생성
        BooleanExpression expression = qGuestBook.gno.gt(0L); // gno > 0 조건만 생성

        // and 메소드를 사용하여 expression을 booleanBuilder에 추가
        // gno > 0 조건이 최종 쿼리에 포함되도록 함
        booleanBuilder.and(expression);

        if (type == null || type.trim().length() == 0) { // 검색 조건이 없는 경우
            // 추가적인 수정 없이 현재의 booleanBuilder를 반환
            return booleanBuilder;
        } // 검색 조건 type이 있으면 conditionBuilder 변수 생성해서 각 검색 조건을 or로 연결해 처리함
          // 없으면 gno > 0로만 생성

        // 검색 조건 작성
        // BooleanBuilder: Predicate 표현식을 위한 연속적인 빌더
        // Predicate: 참 또는 거짓을 반환하는 함수
        // 검색 유형에 기반하여 추가 조건을 구성하는 데 사용
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){ // name(키) type에 문자 t가 포함되면
            // GuestBook 엔티티의 title이 keyword를 포함하는 조건을 conditionBuilder에 추가
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")){ // name(키) type에 문자 c가 포함되면
            // GuestBook 엔티티의 content가 keyword를 포함하는 조건을 conditionBuilder에 추가
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if(type.contains("w")){ // name(키) type에 문자 w가 포함되면
            // GuestBook 엔티티의 writer가 keyword를 포함하는 조건을 conditionBuilder에 추가
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
