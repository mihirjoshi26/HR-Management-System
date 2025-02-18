package com.example.employee.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

	@GetMapping("/admin/dashboard")
	public String showAdminDashboard(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);

		// Ensure only admin can access
		if (session == null || session.getAttribute("admin") == null) {
			return "redirect:/login"; // Redirect unauthorized users to login
		}

		return "admin-dashboard"; // Load the Thymeleaf template
	}
}