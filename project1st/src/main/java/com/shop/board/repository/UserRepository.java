package com.shop.board.repository;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.board.domain.User;

public interface UserRepository extends JpaRepository<User,Long>{
	 Optional<User> findByEmail(String email);
}
