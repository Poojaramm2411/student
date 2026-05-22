package com.citpl.student.service.implementation;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.citpl.student.dto.Request.AdminRequest;
import com.citpl.student.dto.Response.AdminResponse;
import com.citpl.student.exception.BadRequestException;
import com.citpl.student.exception.ResourceNotFoundException;
import com.citpl.student.model.Admin;
import com.citpl.student.repository.AdminRepo;
import com.citpl.student.security.JwtUtil;
import com.citpl.student.service.AdminService;
import com.citpl.student.util.AdminMapper;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminServiceImpl(
            AdminRepo adminRepo,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AdminResponse register(AdminRequest request) {

        if (adminRepo.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email already exists: " + request.getEmail()); // ✅ 400
        }

        final Admin admin = AdminMapper.mapToEntity(request);

        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        Admin savedAdmin = adminRepo.save(admin);

        final String token = jwtUtil.generateToken(savedAdmin.getEmail());

        AdminResponse response = AdminMapper.mapToDto(savedAdmin);

        response.setToken(token);

        return response;
    }

    @Override
    public AdminResponse login(AdminRequest request) {

        Admin admin = adminRepo.findByEmail(request.getEmail());

        if (admin == null) {
            throw new ResourceNotFoundException(
                    "Admin not found with email: " + request.getEmail()); // ✅ 404
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                admin.getPassword())) {

            throw new BadRequestException("Invalid password provided"); // ✅ 400
        }

        String token = jwtUtil.generateToken(admin.getEmail());

        AdminResponse response = AdminMapper.mapToDto(admin);

        response.setToken(token);

        return response;
    }

    @Override
    public AdminResponse getAdminByEmail(String email) {

        Admin admin = adminRepo.findByEmail(email);

        if (admin == null) {
            throw new ResourceNotFoundException(
                    "Admin not found with email: " + email); // ✅ 404
        }

        return AdminMapper.mapToDto(admin);
    }

    @Override
    public AdminResponse updateAdmin(
            String email,
            AdminRequest request) {

        Admin admin = adminRepo.findByEmail(email);

        if (admin == null) {
            throw new ResourceNotFoundException(
                    "Admin not found with email: " + email); // ✅ 404
        }

        admin.setName(request.getName());
        admin.setEmail(request.getEmail());

        if (request.getPassword() != null &&
                !request.getPassword().isEmpty()) {

            admin.setPassword(
                    passwordEncoder.encode(request.getPassword()));
        }

        Admin updatedAdmin = adminRepo.save(admin);

        return AdminMapper.mapToDto(updatedAdmin);
    }

    @Override
    public void deleteAdmin(String email) {

        Admin admin = adminRepo.findByEmail(email);

        if (admin == null) {
            throw new ResourceNotFoundException(
                    "Admin not found with email: " + email); // ✅ 404
        }

        adminRepo.delete(admin);
    }
}