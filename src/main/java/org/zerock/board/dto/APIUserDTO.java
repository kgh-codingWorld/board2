package org.zerock.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.zerock.board.entity.APIUser;

import java.util.Collection;

@Getter
@Setter
@ToString
public class APIUserDTO extends User { // APIUserDetailsService의 loadUserByUsername()의 결과를 처리하기 위한 DTO 클래스

    private String mid;
    private String mpw;

    public APIUserDTO(String username, String password, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.mid = username;
        this.mpw = password;
    }
}
