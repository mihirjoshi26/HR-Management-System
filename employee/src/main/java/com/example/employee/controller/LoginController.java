package com.example.employee.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.employee.models.Admin;
import com.example.employee.models.Client;
import com.example.employee.repositories.AdminRepository;
import com.example.employee.repositories.ClientsRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private ClientsRepository clientsRepo;

	@Autowired
	private AdminRepository adminRepo;

	// ✅ Display Login Page
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	// ✅ Handle Login for Both Users and Admins
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request,
			Model model) {
		HttpSession session = request.getSession();

		// ✅ Check Admin Credentials
		Admin admin = adminRepo.getAdminByEmail(email);
		if (admin != null && BCrypt.checkpw(password, admin.getPassword())) {
			session.setAttribute("admin", admin);
			return "redirect:/admin/dashboard";
		}

		// ✅ Check Employee Credentials
		Client client = clientsRepo.getClient(email);
		if (client != null && BCrypt.checkpw(password, client.getPassword())) {
			session.setAttribute("user", client);
			session.setAttribute("user_id", client.getId()); // ✅ Ensure user_id is stored
			session.setAttribute("user_name", client.getFirstName() + " " + client.getLastName());
			return "redirect:/profile";
		}

		// ❌ If Login Fails
		model.addAttribute("error", "Invalid email or password");
		return "login";
	}

	// ✅ Handle Logout for Both Users and Admins
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); // Invalidate the session
		}
		return "redirect:/login"; // ✅ Redirect to the single login page
	}
}