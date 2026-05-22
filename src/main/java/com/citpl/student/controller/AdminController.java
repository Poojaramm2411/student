package com.citpl.student.controller;

import org.springframework.web.bind.annotation.*;

import com.citpl.student.dto.Request.AdminRequest;
import com.citpl.student.dto.Response.AdminResponse;
import com.citpl.student.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*") 
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public AdminResponse register(
            @RequestBody AdminRequest request) {

        return adminService.register(request);
    }

    @PostMapping("/login")
    public AdminResponse login(
            @RequestBody AdminRequest request) {

        return adminService.login(request);
    }

    @GetMapping("/get/{email}")
    public AdminResponse getAdminByEmail(
            @PathVariable String email) {

        return adminService.getAdminByEmail(email);
    }

    @PutMapping("/update/{email}")
    public AdminResponse updateAdmin(
            @PathVariable String email,
            @RequestBody AdminRequest request) {

        return adminService.updateAdmin(email, request);
    }

    @DeleteMapping("/delete/{email}")
    public String deleteAdmin(
            @PathVariable String email) {

        adminService.deleteAdmin(email);

        return "Admin Deleted Successfully";
    }
}