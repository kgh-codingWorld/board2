package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie", "mmember"}) // 연관 관계 주의

public class Review extends BaseEntity { // Movie와 Member를 양쪽으로 참고하는 구조이므로 @ManyToOne으로 설계

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mmember mmember;

    private int grade;

    private String text;

    // 450 추가
    public void changeGrade(int grade) {
        this.grade = grade;
    }

    public void changeText(String text) {
        this.text = text;

    }
}
