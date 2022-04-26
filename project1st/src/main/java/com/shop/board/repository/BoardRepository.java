package com.shop.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.board.domain.Board;
import com.shop.board.domain.User;

public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findByTitleContaining(String keyword);

    Board findByUser(User user);
}
