package com.example.employee.controller;

import com.example.employee.models.Attendance;
import com.example.employee.models.Client;
import com.example.employee.repositories.AttendanceRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@PostMapping("/check-in")
	public String checkIn(@RequestParam("checkInTime") String checkInTime, HttpSession session) {
		Client user = (Client) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		LocalDateTime checkInDateTime = LocalDateTime.parse(checkInTime);
		attendanceRepository.checkIn(user.getId(), checkInDateTime);
		return "redirect:/profile";
	}

	@PostMapping("/check-out")
	public String checkOut(@RequestParam("checkOutTime") String checkOutTime, HttpSession session) {
		Client user = (Client) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		LocalDateTime checkOutDateTime = LocalDateTime.parse(checkOutTime);
		attendanceRepository.checkOut(user.getId(), checkOutDateTime);
		return "redirect:/profile";
	}

	@GetMapping("/view")
	public String viewAttendance(HttpSession session, Model model) {
		Client user = (Client) session.getAttribute("user");
		if (user == null) {
			return "redirect:/login";
		}
		List<Attendance> attendances = attendanceRepository.getAttendanceByEmployeeId(user.getId());
		model.addAttribute("attendances", attendances);
		return "user-attendance";
	}
}