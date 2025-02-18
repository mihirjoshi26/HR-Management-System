package com.example.employee.controller;

import com.example.employee.models.Client;
import com.example.employee.repositories.ClientsRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private ClientsRepository repo;

	// ✅ Show Profile Page with Updated Salary
	@GetMapping
	public String showProfile(Model model, HttpSession session) {
		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}

		// ✅ Fetch updated client data from database
		Client updatedClient = repo.getClient(loggedInClient.getId());
		if (updatedClient == null) {
			return "redirect:/login";
		}

		// ✅ Ensure salary is not null
		if (updatedClient.getSalary() == null) {
			updatedClient.setSalary(0.0);
		}

		// ✅ Update session with the latest data
		session.setAttribute("user", updatedClient);
		model.addAttribute("client", updatedClient);

		return "profile";
	}

	// ✅ Show Update Details Page
	@GetMapping("/update")
	public String showUpdateDetailsPage(Model model, HttpSession session) {
		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}

		if (loggedInClient.getDateOfJoining() != null) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        model.addAttribute("formattedDateOfJoining", loggedInClient.getDateOfJoining().format(formatter));
	    } else {
	        model.addAttribute("formattedDateOfJoining", ""); // Prevents null issues
	    }

		model.addAttribute("client", loggedInClient);
		return "update-details"; // Ensure update-details.html exists
	}

	// ✅ Handle Profile Update
	@PostMapping("/update")
	public String updateProfile(@ModelAttribute Client client, HttpSession session, Model model) {
		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}

		// Preserve existing ID and email
		client.setId(loggedInClient.getId());
		client.setEmail(loggedInClient.getEmail());

		// Ensure correct Date format conversion
		if (client.getDateOfJoining() == null) {
			client.setDateOfJoining(LocalDate.now());
		}

		// ✅ Update database and session
		repo.updateClient(client);
		session.setAttribute("user", client);
		return "redirect:/profile"; // ✅ Redirect to profile after updating
	}

	// ✅ Show Change Password Page
	@GetMapping("/change-password")
	public String showChangePasswordPage() {
		return "change-password";
	}

	// ✅ Handle Password Change
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
			HttpSession session, Model model) {
		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}

		// ✅ Ensure password is not null before checking
		if (loggedInClient.getPassword() == null || loggedInClient.getPassword().isEmpty()) {
			model.addAttribute("error", "Your password is not set. Contact admin to reset.");
			return "change-password";
		}

		// ✅ Validate current password
		if (!BCrypt.checkpw(currentPassword, loggedInClient.getPassword())) {
			model.addAttribute("error", "Incorrect current password.");
			return "change-password";
		}

		// ✅ Generate new hashed password before storing
		String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
		repo.updatePassword(loggedInClient.getEmail(), hashedPassword);

		// ✅ Update session with new password
		loggedInClient.setPassword(hashedPassword);
		session.setAttribute("user", loggedInClient);

		model.addAttribute("success", "Password changed successfully!");
		return "redirect:/profile"; // ✅ Redirect after successful update
	}

	// ✅ Handle User Logout
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // Clear session
		return "redirect:/login"; // Redirect to login page
	}
}
