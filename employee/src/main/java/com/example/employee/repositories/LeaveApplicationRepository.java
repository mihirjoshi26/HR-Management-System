package com.example.employee.repositories;

import com.example.employee.models.LeaveApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LeaveApplicationRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean isEmployeeExists(int employeeId) {
		String sql = "SELECT COUNT(*) FROM clients WHERE id = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, employeeId);
		return count != null && count > 0;
	}

	// ✅ Save a new leave application
	public void saveLeaveApplication(LeaveApplication leave) {
		String sql = "INSERT INTO leave_applications (employee_id, start_date, end_date, leave_type, remarks, status) VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, leave.getEmployeeId(), leave.getStartDate(), leave.getEndDate(), leave.getLeaveType(),
				leave.getRemarks(), "Pending");
	}

	// ✅ Get all leave applications for a specific employee
	public List<LeaveApplication> getLeaveApplicationsByEmployeeId(int employeeId) {
		String sql = "SELECT * FROM leave_applications WHERE employee_id = ? ORDER BY created_at DESC";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, employeeId);
		return mapLeaveApplications(rows);
	}

	// ✅ Get all leave applications for admin view
	public List<LeaveApplication> getAllLeaveApplications() {
	    String sql = "SELECT la.id, c.firstName, c.lastName, la.start_date, la.end_date, la.leave_type, la.remarks, la.status " +
	                 "FROM leave_applications la " +
	                 "JOIN clients c ON la.employee_id = c.id " +
	                 "ORDER BY la.created_at DESC";

	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        LeaveApplication leave = new LeaveApplication();
	        leave.setId(rs.getInt("id"));
	        leave.setEmployeeName(rs.getString("firstName") + " " + rs.getString("lastName")); // ✅ Fetch full name
	        leave.setStartDate(rs.getDate("start_date").toLocalDate());
	        leave.setEndDate(rs.getDate("end_date").toLocalDate());
	        leave.setLeaveType(rs.getString("leave_type"));
	        leave.setRemarks(rs.getString("remarks"));
	        leave.setStatus(rs.getString("status"));
	        return leave;
	    });
	}


	// ✅ Update leave status (Approve/Reject)
	public void updateLeaveStatus(int leaveId, String status) {
		String sql = "UPDATE leave_applications SET status = ? WHERE id = ?";
		jdbcTemplate.update(sql, status, leaveId);
	}

	// ✅ Helper method to map SQL result to LeaveApplication objects
	private List<LeaveApplication> mapLeaveApplications(SqlRowSet rows) {
		List<LeaveApplication> leaveApplications = new ArrayList<>();
		while (rows.next()) {
			LeaveApplication leave = new LeaveApplication();
			leave.setId(rows.getInt("id"));
			leave.setEmployeeId(rows.getInt("employee_id"));
			leave.setStartDate(rows.getDate("start_date").toLocalDate());
			leave.setEndDate(rows.getDate("end_date").toLocalDate());
			leave.setLeaveType(rows.getString("leave_type"));
			leave.setRemarks(rows.getString("remarks"));
			leave.setStatus(rows.getString("status"));
			leaveApplications.add(leave);
		}
		return leaveApplications;
	}
}