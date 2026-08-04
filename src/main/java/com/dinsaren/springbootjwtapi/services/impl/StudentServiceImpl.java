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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dinsaren.springbootjwtapi.utils.helper.generateUsername;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setStatus("A");
        user.setVerifyEmail("Y");
        user.setChangePassword("N");

        // Temporary username
        user.setUsername("TEMP");

        // Assign Role
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        // Save user to get generated ID
        user = userRepository.save(user);

        // Generate Username
        String username = generateUsername(
                request.getFirstName(),
                request.getLastName(),
                user.getId());

        user.setUsername(username);

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
        return StudentMapper.toResponse(student);
    }

    @Override
    public StudentRes update(Integer id, StudentReq request) {
        return null;
    }

    @Override
    public StudentRes findById(Integer id) {
        return null;
    }

    @Override
    public List<StudentRes> findAll() {
        return List.of();
    }

    @Override
    public void delete(Integer id) {

    }
}