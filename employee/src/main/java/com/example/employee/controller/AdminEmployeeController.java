package com.example.employee.controller;

import com.example.employee.models.Client;
import com.example.employee.repositories.ClientsRepository;
import com.example.employee.services.EmailService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AdminEmployeeController {

	@Autowired
	private ClientsRepository clientsRepository;

	@Autowired
	private EmailService emailService; // ✅ Inject EmailService

	// ✅ Show Add Employee Form
	@GetMapping("/admin/add-employee")
	public String showAddEmployeeForm(Model model) {
		Client client = new Client();
		model.addAttribute("client", client);
		System.out.println("Client object added to model: " + client); // Debugging line
		return "add-employee";
	}

	// ✅ Handle Employee Creation with Default Password
	@PostMapping("/admin/add-employee")
	public String addEmployee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email,
			@RequestParam String phone, @RequestParam String address, @RequestParam String dateOfJoining,
			@RequestParam String gender, @RequestParam String dateOfBirth, @RequestParam Double salary, Model model) {

		// ✅ Check if email already exists
		if (clientsRepository.getClient(email) != null) {
			model.addAttribute("error", "Email already exists!");
			return "add-employee";
		}

		Client client = new Client();
		client.setFirstName(firstName);
		client.setLastName(lastName);
		client.setEmail(email);
		client.setPhone(phone);
		client.setAddress(address);
		client.setGender(gender);

		// ✅ Convert Date String to LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		client.setDateOfJoining(LocalDate.parse(dateOfJoining, formatter));
		client.setDateOfBirth(LocalDate.parse(dateOfBirth, formatter));

		// ✅ Set Salary
		client.setSalary(salary != null ? salary : 0.0); // Ensure salary is set correctly

		// ✅ Set Default Password ("abc123") and Hash It
		String defaultPassword = BCrypt.hashpw("abc123", BCrypt.gensalt(12)); // ✅ Hashing for security
		client.setPassword(defaultPassword);

		// ✅ Save to Database
		clientsRepository.createClient(client);

		// Send Email Notification
		emailService.sendEmployeeRegistrationEmail(client.getEmail(), client.getEmail(), defaultPassword);

		// ✅ Redirect to clients list page instead of showing success message
		return "redirect:/clients";
	}

	// ✅ Hash Password Before Storing
	private String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	// Show Salary Management Page
	@GetMapping("/admin/salary-management")
	public String showSalaryManagementPage(Model model) {
		List<Client> clients = clientsRepository.getClients(); // ✅ Ensure salary is included
		model.addAttribute("clients", clients);
		return "salary-management";
	}

	// Show Set Salary Page
	@GetMapping("/admin/set-salary/{id}")
	public String showSetSalaryPage(@PathVariable int id, Model model) {
		Client client = clientsRepository.getClient(id);
		if (client == null) {
			return "redirect:/admin/salary-management";
		}
		model.addAttribute("client", client);
		return "set-salary";
	}

	// Handle Setting Salary
	@PostMapping("/admin/set-salary")
	public String setSalary(@RequestParam int id, @RequestParam double experience, @RequestParam double performance,
			@RequestParam double baseSalary) {

		double calculatedSalary = baseSalary + (experience * 500) + (performance * 200);
		clientsRepository.updateSalary(id, calculatedSalary);

		return "redirect:/admin/salary-management";
	}

	@GetMapping("/admin/edit-employee/{id}")
	public String showEditEmployeeForm(@PathVariable int id, Model model) {
		Client client = clientsRepository.getClient(id);
		if (client == null) {
			return "redirect:/clients";
		}
		model.addAttribute("client", client);
		return "edit-employee";
	}

	@PostMapping("/admin/edit-employee")
	public String editEmployee(@ModelAttribute Client client, Model model) {
		// ✅ Ensure salary is not null
		if (client.getSalary() == null) {
			client.setSalary(0.0); // Default to 0 if salary is not provided
		}

		// ✅ Update client in the database
		clientsRepository.updateClient(client);

		return "redirect:/clients";
	}
}