package hu.bearmaster.springtutorial.boot.repository;

import hu.bearmaster.springtutorial.boot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
