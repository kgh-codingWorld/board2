package org.zerock.board.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.MemberROLE;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Member member = Member.builder()
                    .email("user" + i + "@aaa.com")
                    .mpw(passwordEncoder.encode("1111"))
                    .mid("USER" + i)
                    .build();

            member.addRole(MemberROLE.USER);

            if (i >= 90) {
                member.addRole(MemberROLE.ADMIN);
            }

            memberRepository.save(member);
        });
    }
}