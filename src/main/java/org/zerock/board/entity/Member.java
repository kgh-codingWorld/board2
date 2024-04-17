package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "roleSet")
public class Member extends BaseEntity {

    @Id // pk 선언
    private String mid;

    private String mpw;
    private String email;
    private boolean del; // 탈퇴 여부
    private boolean social; // 소셜 로그인 자동 회원 가입 여부

    @ElementCollection(fetch = FetchType.LAZY) // USER 혹은 ADMIN 권한을 가지도록 함
    @Builder.Default
    private Set<MemberROLE> roleSet = new HashSet<>();

    public void changePassword(String mpw) {
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del) {
        this.del = del;
    }

    public void addRole(MemberROLE memberROLE) {
        this.roleSet.add(memberROLE);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }

}