package com.vitor.authuser.repository;

import com.vitor.authuser.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByCpf(String cpf);

    @Transactional
    @Modifying
    @Query("update User usuario set usuario.email = :email where usuario.userId = :userId")
    void updateEmail(@Param("userId") UUID userId,
                     @Param("email") String email);
}
