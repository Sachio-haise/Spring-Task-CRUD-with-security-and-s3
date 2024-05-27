package com.spring.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.security.entity.Token;


public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id WHERE t.user.id = :userId AND t.isLoggedOut = false")
    List<Token> findAllTokenByUser(@Param("userId") Integer userId);

    Optional<Token> findByToken(String token);
}
