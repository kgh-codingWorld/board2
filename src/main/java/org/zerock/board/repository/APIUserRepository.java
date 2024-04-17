package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.board.entity.APIUser;

public interface APIUserRepository extends JpaRepository<APIUser, String> {
}
