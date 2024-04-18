package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageRequestDTO { // 페이지 요청 처리
    // 목록 페이지를 요청할 때 사용하는 데이터를 재사용하기 쉽게 만드는 클래스
    // 목록 화면: 페이지 번호, 페이지 내 목록 개수, 검색 조건 등이 많이 사용됨(파라미터)
    // page와 size 파라미터를 수집하는 역할
    // **JPA 쪽에서 사용하는 Pageable 타입의 객체를 생성하는 것이 진짜 목적**

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type;

    private String keyword;

    public String[] getTypes() {
        if(type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String...props) {
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    private String link;

    public String getLink() {
        if(link == null) {
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);

            if(type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }

            if(keyword != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }
            }
            link = builder().link;
        }
        return link;
    }
}
