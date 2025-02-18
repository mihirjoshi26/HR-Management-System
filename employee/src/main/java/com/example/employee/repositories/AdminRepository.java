package com.example.employee.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.example.employee.models.Admin;

@Repository
public class AdminRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Get admin by email
	public Admin getAdminByEmail(String email) {
		String sql = "SELECT * FROM admins WHERE email=?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, email);

		if (rows.next()) {
			Admin admin = new Admin();
			admin.setId(rows.getInt("id"));
			admin.setEmail(rows.getString("email"));
			admin.setPassword(rows.getString("password"));
			return admin;
		}
		return null;
	}
}
