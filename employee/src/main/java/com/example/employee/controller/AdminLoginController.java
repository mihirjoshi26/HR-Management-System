package com.example.employee.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
	
import com.example.employee.models.Admin;
import com.example.employee.models.Client;
import com.example.employee.repositories.AdminRepository;
import com.example.employee.repositories.ClientsRepository;
import com.example.employee.services.ExcelService;
import com.example.employee.services.ExcelImportService;

@Controller
public class AdminLoginController {

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private ClientsRepository clientsRepository;

	@Autowired
	private ExcelService excelService;

	@Autowired
	private ExcelImportService excelImportService;

	// ✅ Display Admin Login Page
	@GetMapping("/admin/login")
	public String showAdminLoginPage() {
		return "admin-login";
	}

	// ✅ Handle Admin Login
	@PostMapping("/admin/login")
	public String loginAdmin(@RequestParam String email, @RequestParam String password, HttpServletRequest request,
			Model model) {
		Admin admin = adminRepo.getAdminByEmail(email);

		if (admin != null && BCrypt.checkpw(password, admin.getPassword())) {
			HttpSession session = request.getSession();
			session.setAttribute("admin", admin);
			return "redirect:/admin/dashboard"; // ✅ Redirect to Admin Dashboard
		} else {
			model.addAttribute("error", "Invalid email or password");
			return "admin-login";
		}
	}

	// ✅ Admin Logout
	@GetMapping("/admin/logout")
	public String logoutAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Invalidate session
		}
		return "redirect:/admin/login";
	}

	// ✅ Export Employee Data to Excel
	@GetMapping("/admin/export")
	public void exportEmployeeData(HttpServletResponse response) throws IOException {
		List<Client> clients = clientsRepository.getClients();
		excelService.exportEmployeeData(response, clients);
	}

	// ✅ Import Employee Data from Excel
	@PostMapping("/admin/import")
	public String importEmployeeData(@RequestParam("file") MultipartFile file, Model model) {
		try {
			excelImportService.importEmployeeData(file);
			model.addAttribute("success", "Employee data updated successfully!");
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("error", "Failed to update employee data.");
		}
		return "admin-dashboard"; // ✅ Return the Thymeleaf template instead of redirecting
	}
}
