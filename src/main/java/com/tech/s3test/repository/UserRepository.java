package com.tech.s3test.repository;

import com.tech.s3test.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByEmail(String email);
}
