package com.example.employee.controller;

import com.example.employee.models.Attendance;
import com.example.employee.repositories.AttendanceRepository;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/attendance")
public class AdminAttendanceController {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@GetMapping("/overview")
	public String attendanceOverview(Model model, HttpSession session) {
		if (session.getAttribute("admin") == null) {
			return "redirect:/admin/login";
		}

		int totalEmployees = attendanceRepository.getTotalEmployees();
		int presentEmployees = attendanceRepository.getPresentEmployees();
		int absentEmployees = attendanceRepository.getAbsentEmployees();
		int onLeaveEmployees = attendanceRepository.getOnLeaveEmployees();

		model.addAttribute("totalEmployees", totalEmployees);
		model.addAttribute("presentEmployees", presentEmployees);
		model.addAttribute("absentEmployees", absentEmployees);
		model.addAttribute("onLeaveEmployees", onLeaveEmployees);

		return "admin-attendance-overview";
	}

	@GetMapping("/admin/attendance/{id}")
	public String viewEmployeeAttendance(@PathVariable int id, Model model, HttpSession session) {
		if (session.getAttribute("admin") == null) {
			return "redirect:/admin/login";
		}

		List<Attendance> attendances = attendanceRepository.getAttendanceByEmployeeId(id);
		model.addAttribute("attendances", attendances);
		model.addAttribute("employeeId", id);

		return "admin-employee-attendance";
	}
}