package com.example.employee.repositories;

import com.example.employee.models.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void checkIn(int employeeId, LocalDateTime checkInTime) {
		String sql = "INSERT INTO attendance (employee_id, check_in_time, date, status) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, employeeId, checkInTime, LocalDateTime.now().toLocalDate(), "Present");
	}

	public void checkOut(int employeeId, LocalDateTime checkOutTime) {
		String sql = "UPDATE attendance SET check_out_time = ?, total_hours = TIMESTAMPDIFF(HOUR, check_in_time, ?), status = ? WHERE employee_id = ? AND date = ?";
		jdbcTemplate.update(sql, checkOutTime, checkOutTime, "Present", employeeId, LocalDateTime.now().toLocalDate());
	}

	public List<Attendance> getAttendanceByEmployeeId(int employeeId) {
		String sql = "SELECT * FROM attendance WHERE employee_id = ? ORDER BY date DESC";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, employeeId);
		List<Attendance> attendances = new ArrayList<>();
		while (rows.next()) {
			attendances.add(mapRowToAttendance(rows));
		}
		return attendances;
	}

	private Attendance mapRowToAttendance(SqlRowSet rows) {
		Attendance attendance = new Attendance();
		attendance.setId(rows.getInt("id"));
		attendance.setEmployeeId(rows.getInt("employee_id"));
		attendance.setCheckInTime(rows.getTimestamp("check_in_time").toLocalDateTime());
		attendance.setCheckOutTime(
				rows.getTimestamp("check_out_time") != null ? rows.getTimestamp("check_out_time").toLocalDateTime()
						: null);
		attendance.setTotalHours(rows.getDouble("total_hours"));
		attendance.setStatus(rows.getString("status"));
		attendance.setDate(rows.getDate("date").toLocalDate().atStartOfDay()); // Convert Date to LocalDateTime
		return attendance;
	}

	public int getTotalEmployees() {
		String sql = "SELECT COUNT(*) FROM clients";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public int getPresentEmployees() {
		String sql = "SELECT COUNT(DISTINCT employee_id) FROM attendance WHERE date = ? AND check_in_time IS NOT NULL";
		return jdbcTemplate.queryForObject(sql, Integer.class, LocalDateTime.now().toLocalDate());
	}

	public int getAbsentEmployees() {
		String sql = "SELECT COUNT(*) FROM clients WHERE id NOT IN (SELECT employee_id FROM attendance WHERE date = ?)";
		return jdbcTemplate.queryForObject(sql, Integer.class, LocalDateTime.now().toLocalDate());
	}

	public int getOnLeaveEmployees() {
		String sql = "SELECT COUNT(*) FROM leave_applications WHERE status = 'Approved' AND ? BETWEEN start_date AND end_date";
		return jdbcTemplate.queryForObject(sql, Integer.class, LocalDateTime.now().toLocalDate());
	}
}