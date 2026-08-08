package com.dinsaren.springbootjwtapi.services.impl;

import com.dinsaren.springbootjwtapi.mapper.TeacherMapper;
import com.dinsaren.springbootjwtapi.models.Role;
import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.UserRole;
import com.dinsaren.springbootjwtapi.models.req.TeacherReq;
import com.dinsaren.springbootjwtapi.models.res.TeacherRes;
import com.dinsaren.springbootjwtapi.models.school.Teacher;
import com.dinsaren.springbootjwtapi.repository.RoleRepository;
import com.dinsaren.springbootjwtapi.repository.TeacherRepository;
import com.dinsaren.springbootjwtapi.repository.UserRepository;
import com.dinsaren.springbootjwtapi.services.TeacherService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherServiceImpl(
            TeacherRepository teacherRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TeacherRes create(TeacherReq request) {

        // Validate Email
        if (userRepository.existsByEmailAndStatus(request.getEmail(), "ACT")) {
            throw new RuntimeException("Email already exists.");
        }

        // Validate Phone
        if (userRepository.existsByPhoneNumberAndStatus(request.getPhone(), "ACT")) {
            throw new RuntimeException("Phone number already exists.");
        }

        // Find Teacher Role
        Role role = roleRepository.findByName(UserRole.ROLE_TEACHER)
                .orElseThrow(() -> new RuntimeException("Role not found."));

        // Create User
        User user = new User();

        user.setUsername(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhone());

        // Default Password
        user.setPassword(passwordEncoder.encode("123456"));

        user.setStatus("ACT");
        user.setVerifyEmail("Y");
        user.setChangePassword("N");

        // Assign Role
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setProfile(request.getPhoto());
        // Save User
        user = userRepository.save(user);
        // Create Teacher
        Teacher teacher = new Teacher();

        teacher.setUser(user);
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setGender(request.getGender());
        teacher.setDateOfBirth(request.getDateOfBirth());
        teacher.setPhone(request.getPhone());
        teacher.setAddress(request.getAddress());
        teacher.setPhoto(request.getPhoto());
        teacher.setIsActive(true);

        teacher = teacherRepository.save(teacher);

        return TeacherMapper.toResponse(teacher);
    }

    @Override
    public TeacherRes update(Integer id, TeacherReq request) {

        Teacher teacher = teacherRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found."));

        User user = teacher.getUser();

        // Validate Email
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmailAndStatus(request.getEmail(), "ACT")) {

            throw new RuntimeException("Email already exists.");
        }

        // Validate Phone
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

        // Update Teacher
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setGender(request.getGender());
        teacher.setDateOfBirth(request.getDateOfBirth());
        teacher.setPhone(request.getPhone());
        teacher.setAddress(request.getAddress());
        teacher.setPhoto(request.getPhoto());

        teacherRepository.save(teacher);

        return TeacherMapper.toResponse(teacher);
    }

    @Override
    public TeacherRes findById(Integer id) {

        Teacher teacher = teacherRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found."));

        return TeacherMapper.toResponse(teacher);
    }

    @Override
    public List<TeacherRes> findAll() {

        return teacherRepository.findByIsActiveTrue()
                .stream()
                .map(TeacherMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Integer id) {

        Teacher teacher = teacherRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found."));

        User user = teacher.getUser();

        // Soft Delete
        user.setStatus("DEL");
        teacher.setIsActive(false);

        userRepository.save(user);
        teacherRepository.save(teacher);
    }
}