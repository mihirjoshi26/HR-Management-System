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
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientsController {

	@Autowired
	private ClientsRepository repo;

	// ✅ View Employee Data (Admin Only)
	@GetMapping("")
	public String getClients(Model model, HttpSession session) {
		if (session == null || session.getAttribute("admin") == null) {
			return "redirect:/admin/login";
		}

		List<Client> clients = repo.getClients(); // ✅ Ensure salary is included
		model.addAttribute("clients", clients);
		return "clients/index"; // ✅ Ensure this matches your Thymeleaf file name
	}

	// ✅ Add Employee (Admin Only)
	@GetMapping("/add")
	public String showAddEmployeePage(Model model) {
		model.addAttribute("client", new Client());
		return "add-employee"; // ✅ Ensure add-employee.html exists
	}

	@PostMapping("/add")
	public String addEmployee(@ModelAttribute Client client, Model model) {
		// Check if email already exists
		if (repo.getClient(client.getEmail()) != null) {
			model.addAttribute("error", "Email already exists!");
			return "add-employee";
		}

		// ✅ Ensure Date is set
		if (client.getDateOfJoining() == null) {
			client.setDateOfJoining(LocalDate.now());
		}

		// ✅ Hash default password ("abc123") before saving
		String defaultPassword = "abc123";
		client.setPassword(BCrypt.hashpw(defaultPassword, BCrypt.gensalt()));

		repo.createClient(client);
		model.addAttribute("success", "Employee added successfully!");
		return "redirect:/clients"; // ✅ Redirect to clients list after adding
	}

	// ✅ Show Profile Page
	@GetMapping("/profile")
	public String showProfile(Model model, HttpSession session) {
		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}
		model.addAttribute("client", loggedInClient);
		return "profile";
	}

	// ✅ Show Update Details Page
	@GetMapping("/profile/update")
	public String showUpdateDetailsPage(Model model, HttpSession session) {
		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}

		// Ensure dateOfJoining and dateOfBirth are properly formatted
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		if (loggedInClient.getDateOfJoining() != null) {
			model.addAttribute("formattedDateOfJoining", loggedInClient.getDateOfJoining().format(formatter));
		} else {
			model.addAttribute("formattedDateOfJoining", ""); // Default to empty if null
		}

		if (loggedInClient.getDateOfBirth() != null) {
			model.addAttribute("formattedDateOfBirth", loggedInClient.getDateOfBirth().format(formatter));
		} else {
			model.addAttribute("formattedDateOfBirth", ""); // Default to empty if null
		}

		model.addAttribute("client", loggedInClient);
		return "update-details";
	}

	// ✅ Handle Profile Update
	@PostMapping("/profile/update")
	public String updateProfile(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String phone, @RequestParam String address, @RequestParam String dateOfJoining,
			@RequestParam String gender, @RequestParam String dateOfBirth,
			@RequestParam(required = false) Double salary, HttpSession session, Model model) {

		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}

		// Preserve existing ID and email
		loggedInClient.setFirstName(firstName);
		loggedInClient.setLastName(lastName);
		loggedInClient.setPhone(phone);
		loggedInClient.setAddress(address);
		loggedInClient.setGender(gender);

		// ✅ Preserve existing Salary if it's missing in the update
		if (salary != null) {
			loggedInClient.setSalary(salary);
		}

		// ✅ Convert Date Strings to LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		loggedInClient.setDateOfJoining(LocalDate.parse(dateOfJoining, formatter));
		loggedInClient.setDateOfBirth(LocalDate.parse(dateOfBirth, formatter)); // Auto-updates Age

		// ✅ Update in Database
		repo.updateClient(loggedInClient);
		session.setAttribute("user", loggedInClient);

		model.addAttribute("success", "Profile updated successfully!");
		return "redirect:/profile";
	}

	// ✅ Show Change Password Page
	@GetMapping("/profile/change-password")
	public String showChangePasswordPage() {
		return "change-password"; // ✅ Ensure change-password.html exists
	}

	// ✅ Handle Password Change
	@PostMapping("/profile/change-password")
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
			HttpSession session, Model model) {
		Client loggedInClient = (Client) session.getAttribute("user");
		if (loggedInClient == null) {
			return "redirect:/login";
		}

		// ✅ Ensure current password is correct
		if (!BCrypt.checkpw(currentPassword, loggedInClient.getPassword())) {
			model.addAttribute("error", "Incorrect current password.");
			return "change-password";
		}

		// ✅ Hash new password before storing
		String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		repo.updatePassword(loggedInClient.getEmail(), hashedPassword);

		// ✅ Update session with new password
		loggedInClient.setPassword(hashedPassword);
		session.setAttribute("user", loggedInClient);

		return "redirect:/profile"; // ✅ Redirect after successful update
	}

	@PostMapping("/update-status")
	public String updateClientStatus(@RequestParam int id, @RequestParam String status) {
		// Ensure only "Active" or "Inactive" are allowed
		if (!status.equals("Active") && !status.equals("Inactive")) {
			status = "Active"; // Default to Active if invalid
		}

		repo.updateClientStatus(id, status);
		return "redirect:/clients"; // Reload the page with updated data
	}

	// ✅ Handle User Logout
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // Clear session
		return "redirect:/login"; // ✅ Redirect to login page
	}
}