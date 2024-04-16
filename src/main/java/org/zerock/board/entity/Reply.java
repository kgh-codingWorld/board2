package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board") // Board를 toString() 시에 제외
public class Reply extends BaseEntity {

    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에서 자동으로 값을 생성하도록 지정
    private Long rno;

    private String text;

    private String replyer;

    // @ManyToOne: 현재 엔티티가 다른 엔티티에 대한 다대일(N:1) 관계를 가짐, 현재 엔티티가 다른 엔티티에 속한다는 것
    // fetch = FetchType.LAZY: 해당 관계를 어떻게 로딩할지를 지정
    // LAZY 옵션은 관련 엔티티가 필요할 때까지 데이터베이스에서 로딩하지 않고, 실제로 해당 엔티티에 접근할 때 로딩
    // 필요한 경우에만 데이터베이스 쿼리를 실행
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board; // 연관 관계 지정, board의 pk를 참조해야 함
}
