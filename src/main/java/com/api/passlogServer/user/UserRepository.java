package com.api.passlogServer.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    @Query("SELECT u FROM users u WHERE u.username = ?1 AND u.password = ?2")
    Optional <Users> findAllByUsernameAndPassword(String username, String password);

    @Query("SELECT u FROM users u WHERE u.username = ?1 AND u.fname = ?2")
    Optional <Users> findUsersByUsernameAndFname(String username, String fname);

    @Query("SELECT u FROM users u WHERE u.username = ?1")
    Optional<Users> findUsersByUsername(String username);

    @Query("SELECT count(u.username) > 0 FROM users u WHERE u.username = ?1")
    boolean existsByUsername(String username);

    @Query("SELECT u FROM users u WHERE u.username = ?1")
    Optional<Users> findByUsername(String username);
}
