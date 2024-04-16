package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.board.entity.GuestBook;

public interface GuestBookRepository extends JpaRepository<GuestBook, Long>, QuerydslPredicateExecutor<GuestBook> { // 인터페이스는 다중 상속이 가능(Qdomain 사용)
                                  // extends JpaRepository<엔티티명, pk 타입> -> Jpa가 crud 해줌(JpaRepository에 내장됨)
    //insert 작업: save 메서드(엔티티 객체)
    //select 작업: findById(키 타입)
    //update 작업: save(엔티티 객체)
    //delete 작업: deleteById(키 타입)

    //Querydsl: Q 도메인을 이용하여 자동으로 검색 조건을 완성시켜줌(다중검색)
    //http://querydsl.com/ 를 참고하여 API 의존성을 받아야 함
}
