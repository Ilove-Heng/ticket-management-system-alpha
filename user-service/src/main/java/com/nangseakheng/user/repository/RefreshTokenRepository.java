package com.nangseakheng.user.repository;


import com.nangseakheng.user.entity.RefreshToken;
import com.nangseakheng.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteAllByUserIn(List<User> users);

    Optional<RefreshToken> findByUser(User user);
}