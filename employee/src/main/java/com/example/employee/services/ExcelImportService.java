package com.example.employee.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.employee.models.Client;
import com.example.employee.repositories.ClientsRepository;

@Service
public class ExcelImportService {

	@Autowired
	private ClientsRepository clientsRepository;

	public void importEmployeeData(MultipartFile file) throws IOException {
		InputStream inputStream = file.getInputStream();
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		List<Client> clients = new ArrayList<>();

		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue; // Skip header row

			Client client = new Client();
			client.setId((int) row.getCell(0).getNumericCellValue()); // Ensure ID is numeric
			client.setFirstName(getCellValueAsString(row.getCell(1))); // Handle string or numeric
			client.setLastName(getCellValueAsString(row.getCell(2))); // Handle string or numeric
			client.setEmail(getCellValueAsString(row.getCell(3))); // Handle string or numeric

			// Handle phone number (can be numeric or string)
			Cell phoneCell = row.getCell(4);
			if (phoneCell != null) {
				if (phoneCell.getCellType() == CellType.NUMERIC) {
					client.setPhone(String.valueOf((long) phoneCell.getNumericCellValue())); // Convert numeric to
																								// string
				} else {
					client.setPhone(phoneCell.getStringCellValue()); // Read as string
				}
			} else {
				client.setPhone(""); // Default value if cell is empty
			}

			client.setAddress(getCellValueAsString(row.getCell(5))); // Handle string or numeric
			client.setPassword(getCellValueAsString(row.getCell(6))); // Handle string or numeric

			// Handle Date of Joining
			Cell dateCell = row.getCell(7);
			if (dateCell != null) {
				if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
					Date date = dateCell.getDateCellValue();
					client.setDateOfJoining(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				} else if (dateCell.getCellType() == CellType.STRING) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					client.setDateOfJoining(LocalDate.parse(dateCell.getStringCellValue(), formatter));
				} else {
					client.setDateOfJoining(LocalDate.now()); // Default to today if empty or invalid
				}
			} else {
				client.setDateOfJoining(LocalDate.now()); // Default to today if cell is empty
			}

			// Handle Gender
			client.setGender(getCellValueAsString(row.getCell(8))); // Handle string or numeric

			// Handle Date of Birth
			Cell dobCell = row.getCell(9);
			if (dobCell != null) {
				if (dobCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dobCell)) {
					Date date = dobCell.getDateCellValue();
					client.setDateOfBirth(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				} else if (dobCell.getCellType() == CellType.STRING) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					client.setDateOfBirth(LocalDate.parse(dobCell.getStringCellValue(), formatter));
				} else {
					client.setDateOfBirth(LocalDate.now()); // Default to today if empty or invalid
				}
			} else {
				client.setDateOfBirth(LocalDate.now()); // Default to today if cell is empty
			}

			// Handle Status
			Cell statusCell = row.getCell(10);
			if (statusCell != null) {
				client.setStatus(getCellValueAsString(statusCell)); // Handle string or numeric
			} else {
				client.setStatus("Active"); // Default to Active if status is missing
			}

			// Handle Salary
			Cell salaryCell = row.getCell(11);
			if (salaryCell != null && salaryCell.getCellType() == CellType.NUMERIC) {
				client.setSalary(salaryCell.getNumericCellValue()); // Read numeric value
			} else {
				// If the salary cell is empty or invalid, retain the existing salary
				Client existingClient = clientsRepository.getClient(client.getId());
				if (existingClient != null) {
					client.setSalary(existingClient.getSalary()); // Preserve existing salary
				} else {
					client.setSalary(0.0); // Default salary for new employees
				}
			}

			clients.add(client);
		}

		workbook.close();

		// Update database
		for (Client client : clients) {
			clientsRepository.updateClient(client);
		}
	}

	// Helper method to get cell value as string, regardless of cell type
	private String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return ""; // Return empty string if cell is null
		}
		if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf((long) cell.getNumericCellValue()); // Convert numeric to string
		} else {
			return cell.getStringCellValue(); // Read as string
		}
	}
}