package com.collegeerp.service.impl;

import com.collegeerp.dto.request.LoginRequest;
import com.collegeerp.dto.request.UserCreateRequest;
import com.collegeerp.dto.response.AuthResponse;
import com.collegeerp.dto.response.UserResponse;
import com.collegeerp.entity.Student;
import com.collegeerp.entity.Teacher;
import com.collegeerp.entity.User;
import com.collegeerp.entity.enums.Role;
import com.collegeerp.exception.BadRequestException;
import com.collegeerp.exception.DuplicateResourceException;
import com.collegeerp.exception.ResourceNotFoundException;
import com.collegeerp.mapper.UserMapper;
import com.collegeerp.repository.StudentRepository;
import com.collegeerp.repository.TeacherRepository;
import com.collegeerp.repository.UserRepository;
import com.collegeerp.security.jwt.JwtService;
import com.collegeerp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return AuthResponse.builder()
                .token(jwtService.generateToken(userDetails))
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(request.getEnabled() == null || request.getEnabled())
                .build();

        if (request.getRole() == Role.STUDENT) {
            if (request.getStudentId() == null) {
                throw new BadRequestException("studentId is required for STUDENT role");
            }
            Student student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
            user.setLinkedStudent(student);
        }
        if (request.getRole() == Role.TEACHER) {
            if (request.getTeacherId() == null) {
                throw new BadRequestException("teacherId is required for TEACHER role");
            }
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));
            user.setLinkedTeacher(teacher);
        }
        User saved = userRepository.save(user);
        return UserMapper.toResponse(saved);
    }
}
