package com.example.employee.services;

import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.example.employee.models.Client;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelService {

	public void exportEmployeeData(HttpServletResponse response, List<Client> clients) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");

		// Create header row
		Row headerRow = sheet.createRow(0);
		String[] columns = { "ID", "First Name", "Last Name", "Email", "Phone", "Address", "Password",
				"Date of Joining", "Gender", "Date of Birth", "Age", "Status", "Salary" };

		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
		}

		// Fill data rows
		int rowNum = 1;
		for (Client client : clients) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(client.getId());
			row.createCell(1).setCellValue(client.getFirstName());
			row.createCell(2).setCellValue(client.getLastName());
			row.createCell(3).setCellValue(client.getEmail());
			row.createCell(4).setCellValue(client.getPhone());
			row.createCell(5).setCellValue(client.getAddress());
			row.createCell(6).setCellValue(client.getPassword()); // Already hashed password
			row.createCell(7).setCellValue(client.getDateOfJoining().toString()); // Date format
			row.createCell(8).setCellValue(client.getGender()); // Gender
			row.createCell(9).setCellValue(client.getDateOfBirth().toString()); // Date of Birth
			row.createCell(10).setCellValue(client.getAge()); // Age
			row.createCell(11).setCellValue(client.getStatus()); // ✅ Include Status
			row.createCell(12).setCellValue(client.getSalary() != null ? client.getSalary() : 0.0);
		}

		// Write to response output stream
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}