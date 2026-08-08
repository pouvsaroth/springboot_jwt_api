package com.dinsaren.springbootjwtapi.services.impl;

import com.dinsaren.springbootjwtapi.mapper.StudentMapper;
import com.dinsaren.springbootjwtapi.models.Role;
import com.dinsaren.springbootjwtapi.models.UserRole;
import com.dinsaren.springbootjwtapi.models.school.Student;
import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.req.StudentReq;
import com.dinsaren.springbootjwtapi.models.res.StudentRes;
import com.dinsaren.springbootjwtapi.repository.RoleRepository;
import com.dinsaren.springbootjwtapi.repository.StudentRepository;
import com.dinsaren.springbootjwtapi.repository.UserRepository;
import com.dinsaren.springbootjwtapi.services.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${server.base_url}")
    private String baseUrl;
    public StudentServiceImpl(StudentRepository studentRepository,
                              UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StudentRes create(StudentReq request) {
        // Validate Email
        if (userRepository.existsByEmailAndStatus(request.getEmail(), "A")) {
            throw new RuntimeException("Email already exists.");
        }

        // Validate Phone
        if (userRepository.existsByPhoneNumberAndStatus(request.getPhone(), "A")) {
            throw new RuntimeException("Phone number already exists.");
        }

        // Find Student Role
        Role role = roleRepository.findByName(UserRole.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Role not found."));

        // Create User
        User user = new User();

        user.setUsername(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setPassword(passwordEncoder.encode("123456"));

        user.setStatus("ACT");
        user.setVerifyEmail("Y");
        user.setChangePassword("N");
        user.setProfile(request.getPhoto());


        // Assign Role
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        // Save user to get generated ID
        user = userRepository.save(user);

        // Create Student
        Student student = new Student();

        student.setUser(user);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());
        student.setPhoto(request.getPhoto());
        student.setIsActive(true);

        student = studentRepository.save(student);

        // Return Response using Mapper
        return StudentMapper.toResponse(student,baseUrl);
    }

    @Override
    public StudentRes update(Integer id, StudentReq request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        User user = student.getUser();

        // Email validation
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmailAndStatus(request.getEmail(), "ACT")) {

            throw new RuntimeException("Email already exists.");
        }

        // Phone validation
        if (!user.getPhoneNumber().equals(request.getPhone())
                && userRepository.existsByPhoneNumberAndStatus(request.getPhone(), "ACT")) {

            throw new RuntimeException("Phone number already exists.");
        }

        // Update User
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setProfile(request.getPhoto());

        userRepository.save(user);

        // Update Student
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());
        student.setPhoto(request.getPhoto());

        studentRepository.save(student);

        return StudentMapper.toResponse(student,baseUrl);
    }

    @Override
    public StudentRes findById(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        return StudentMapper.toResponse(student,baseUrl);
    }

    @Override
    public List<StudentRes> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(student -> StudentMapper.toResponse(student, baseUrl))
                .toList();
    }

    @Override
    public void delete(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        User user = student.getUser();

        user.setStatus("DEL");
        userRepository.save(user);

        student.setIsActive(false);
        studentRepository.save(student);
    }
}