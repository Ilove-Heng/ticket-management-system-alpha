package com.nangseakheng.user.repository;

import com.nangseakheng.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    // Optional from java 8 to handle null values
    Optional<User> findFirstById(Long id);
    Optional<User> findFirstByUsernameAndStatus(String username, String status);
    List<User> findAllByIdIn(Set<Long> ids);
}
