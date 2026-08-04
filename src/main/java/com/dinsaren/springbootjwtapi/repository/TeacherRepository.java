package com.dinsaren.springbootjwtapi.repository;

import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.school.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByUser(User user);

    boolean existsByUser(User user);

    List<Teacher> findByIsActiveTrue();

    Optional<Teacher> findByIdAndIsActiveTrue(Integer id);

}