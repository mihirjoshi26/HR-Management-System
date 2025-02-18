package com.example.employee.models;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Client {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private String password;
	private LocalDate dateOfJoining;
	private String status;
	private Double salary;
	private String gender;
	private LocalDate dateOfBirth;
	private int age;

	// Getters & Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		if (dateOfJoining != null && !dateOfJoining.isEmpty()) {
			this.dateOfJoining = LocalDate.parse(dateOfJoining, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} else {
			this.dateOfJoining = LocalDate.now(); // Default to today if empty
		}
	}

	public void setDateOfJoining(LocalDate dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getFormattedDateOfJoining() {
		return (dateOfJoining != null) ? dateOfJoining.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		updateAge(); // Auto-update age when setting Date of Birth
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age; // Allows manual setting (used in repository updates)
	}

	public void updateAge() {
		if (this.dateOfBirth != null) {
			this.age = Period.between(this.dateOfBirth, LocalDate.now()).getYears();
		} else {
			this.age = 0;
		}
	}

	public String getFormattedDateOfBirth() {
		return (dateOfBirth != null) ? dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
	}
}