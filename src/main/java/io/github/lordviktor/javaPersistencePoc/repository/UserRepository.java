package io.github.lordviktor.javaPersistencePoc.repository;

import io.github.lordviktor.javaPersistencePoc.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
