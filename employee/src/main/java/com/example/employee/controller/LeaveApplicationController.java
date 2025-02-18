package com.example.employee.controller;

import com.example.employee.models.Client;
import com.example.employee.models.LeaveApplication;
import com.example.employee.repositories.LeaveApplicationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/leave")
public class LeaveApplicationController {

	@Autowired
	private LeaveApplicationRepository leaveApplicationRepository;

	// ✅ Show Leave Application Form
	@GetMapping("/apply")
	public String showLeaveApplicationForm(Model model, HttpSession session) {
		Client user = (Client) session.getAttribute("user"); // Retrieve logged-in user
		if (user == null) {
			return "redirect:/login"; // Redirect if not logged in
		}

		String employeeName = user.getFirstName() + " " + user.getLastName();
		System.out.println("Debug: Employee Name - " + employeeName); // ✅ Debugging

		model.addAttribute("leaveApplication", new LeaveApplication());
		model.addAttribute("employeeName", employeeName); // ✅ Pass employeeName

		return "leave-application";
	}

	// ✅ Handle Leave Application Submission
	@PostMapping("/apply")
	public String applyForLeave(@ModelAttribute LeaveApplication leaveApplication, HttpSession session, Model model) {
		Client user = (Client) session.getAttribute("user"); // Retrieve the logged-in employee object
		if (user == null) {
			return "redirect:/login"; // Redirect if not logged in
		}

		int employeeId = user.getId(); // Fetch correct employee ID
		leaveApplication.setEmployeeId(employeeId);
		leaveApplication.setStatus("Pending"); // Default status

		// ✅ Debugging: Log employee_id before inserting
		System.out.println("Applying for leave - Employee ID: " + employeeId);

		// ✅ Validate if employee_id exists in `clients` before inserting
		if (leaveApplicationRepository.isEmployeeExists(employeeId)) {
			leaveApplicationRepository.saveLeaveApplication(leaveApplication);
			return "redirect:/leave/view"; // Redirect to leave applications
		} else {
			model.addAttribute("error", "Error: Employee not found in the system.");
			return "leave-application";
		}
	}

	// ✅ View Leave Applications (Employee)
	@GetMapping("/view")
	public String viewEmployeeLeaveApplications(Model model, HttpSession session) {
		Client user = (Client) session.getAttribute("user"); // ✅ Retrieve user object
		if (user == null) {
			return "redirect:/login"; // ✅ Redirect to login if session is null
		}

		int employeeId = user.getId(); // ✅ Retrieve user ID from Client object
		List<LeaveApplication> leaveApplications = leaveApplicationRepository
				.getLeaveApplicationsByEmployeeId(employeeId);
		model.addAttribute("leaveApplications", leaveApplications);

		return "view-leave-applications";
	}

	// ✅ View All Leave Applications (Admin)
	@GetMapping("/admin/view")
	public String viewAllLeaveApplications(Model model, HttpSession session) {
		Object admin = session.getAttribute("admin");
		if (admin == null) {
			return "redirect:/admin/login"; // Redirect to login if admin is not authenticated
		}

		List<LeaveApplication> leaveApplications = leaveApplicationRepository.getAllLeaveApplications();
		model.addAttribute("leaveApplications", leaveApplications);

		return "admin-leave-management";
	}

	// ✅ Approve/Reject Leave Application (Admin)
	@PostMapping("/admin/update-status")
	public String updateLeaveStatus(@RequestParam int id, @RequestParam String status, HttpSession session) {
		Object admin = session.getAttribute("admin");
		if (admin == null) {
			return "redirect:/admin/login"; // Redirect to admin login if not logged in
		}

		leaveApplicationRepository.updateLeaveStatus(id, status);
		return "redirect:/leave/admin/view"; // Redirect back to admin leave management page
	}
}