package com.example.employee.repositories;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import com.example.employee.models.Client;

@Repository
public class ClientsRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ✅ Retrieve All Clients (Including Salary)
	public List<Client> getClients() {
		List<Client> clients = new ArrayList<>();
		String sql = "SELECT * FROM clients ORDER BY id DESC"; // ✅ Ensure salary column exists
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

		while (rows.next()) {
			clients.add(mapRowToClient(rows));
		}
		return clients;
	}

	// ✅ Get a Client by ID (Including Salary)
	public Client getClient(int id) {
		String sql = "SELECT * FROM clients WHERE id=?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

		if (rows.next()) {
			return mapRowToClient(rows);
		}
		return null;
	}

	// ✅ Get a Client by Email
	public Client getClient(String email) {
		String sql = "SELECT * FROM clients WHERE email=?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, email);

		if (rows.next()) {
			return mapRowToClient(rows);
		}
		return null;
	}

	// ✅ Create a New Client
	public Client createClient(Client client) {
		String sql = "INSERT INTO clients (firstname, lastname, email, phone, address, password, dateOfJoining, gender, date_of_birth, age, salary) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int count = jdbcTemplate.update(sql, client.getFirstName(), client.getLastName(), client.getEmail(),
				client.getPhone(), client.getAddress(), client.getPassword(), client.getDateOfJoining().toString(),
				client.getGender(), client.getDateOfBirth().toString(), client.getAge(), client.getSalary());

		if (count > 0) {
			int id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
			return getClient(id);
		}
		return null;
	}

	// ✅ Update an Existing Client
	public Client updateClient(Client client) {
	    String sql = "UPDATE clients SET firstname=?, lastname=?, phone=?, address=?, gender=?, salary=? WHERE id=?";

	    int count = jdbcTemplate.update(sql, client.getFirstName(), client.getLastName(), client.getPhone(),
	            client.getAddress(), client.getGender(), client.getSalary(), client.getId());

	    return count > 0 ? getClient(client.getId()) : null;
	}

	// ✅ Update Client Age Separately (When DOB Changes)
	public void updateClientAge(int id, int age) {
		String sql = "UPDATE clients SET age=? WHERE id=?";
		jdbcTemplate.update(sql, age, id);
	}

	// ✅ Update Client Salary
	public void updateSalary(int id, double salary) {
		String sql = "UPDATE clients SET salary=? WHERE id=?";
		jdbcTemplate.update(sql, salary, id);
	}

	// ✅ Update Client Password with Hashing
	public void updatePassword(String email, String hashedPassword) {
		String sql = "UPDATE clients SET password=? WHERE email=?";
		jdbcTemplate.update(sql, hashedPassword, email); // ✅ No extra hashing here
	}

	// ✅ Update Client Status (Active/Inactive)
	public void updateClientStatus(int id, String status) {
		String sql = "UPDATE clients SET status=? WHERE id=?";
		jdbcTemplate.update(sql, status, id);
	}

	// ✅ Delete a Client by ID
	public void deleteClient(int id) {
		String sql = "DELETE FROM clients WHERE id=?";
		jdbcTemplate.update(sql, id);
	}

	// ✅ Helper method to map SQL Row Data to Client Object (Including Salary)
	private Client mapRowToClient(SqlRowSet rows) {
		Client client = new Client();
		client.setId(rows.getInt("id"));
		client.setFirstName(rows.getString("firstname"));
		client.setLastName(rows.getString("lastname"));
		client.setEmail(rows.getString("email"));
		client.setPhone(rows.getString("phone"));
		client.setAddress(rows.getString("address"));
		client.setPassword(rows.getString("password"));
		client.setGender(rows.getString("gender"));

		// Convert date strings to LocalDate
		String dobString = rows.getString("date_of_birth");
		if (dobString != null && !dobString.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			client.setDateOfBirth(LocalDate.parse(dobString, formatter));
		}

		String dateOfJoiningString = rows.getString("dateOfJoining");
		if (dateOfJoiningString != null && !dateOfJoiningString.isEmpty()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			client.setDateOfJoining(LocalDate.parse(dateOfJoiningString, formatter));
		}

		// Fetch Salary
		client.setSalary(rows.getDouble("salary"));

		String status = rows.getString("status");
		client.setStatus((status != null && !status.isEmpty()) ? status : "Active");

		return client;
	}
}