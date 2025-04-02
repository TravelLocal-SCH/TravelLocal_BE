package sch.travellocal.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sch.travellocal.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
