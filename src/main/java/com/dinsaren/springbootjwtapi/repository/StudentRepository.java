package com.dinsaren.springbootjwtapi.repository;

import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.school.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByUser(User user);

    boolean existsByUser(User user);

    List<Student> findByIsActiveTrue();

    Optional<Student> findByIdAndIsActiveTrue(Integer id);
}