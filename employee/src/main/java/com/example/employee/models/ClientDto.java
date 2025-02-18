package com.example.employee.models;

import java.time.LocalDate;
import java.time.Period;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class ClientDto {

	@NotEmpty(message = "The First Name is Required")
	private String firstName;

	@NotEmpty(message = "The Last Name is Required")
	private String lastName;

	@NotEmpty(message = "The Email is Required")
	@Email
	private String email;

	private String phone;
	private String address;
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;
	private String confirmPassword;
	@NotEmpty(message = "Gender is required")
	@Pattern(regexp = "Male|Female", message = "Gender must be Male or Female")
	private String gender;

	@Past(message = "Date of Birth must be in the past")
	private LocalDate dateOfBirth;

	private int age; // Auto-calculated

	// Getters and Setters
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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

	private void updateAge() {
		if (this.dateOfBirth != null) {
			this.age = Period.between(this.dateOfBirth, LocalDate.now()).getYears();
		} else {
			this.age = 0;
		}
	}
}