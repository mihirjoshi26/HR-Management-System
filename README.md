# HR-Management-System

Certainly! Below is a well-structured and detailed `README.md` file for your Employee Management System project. You can customize it further as needed.

---

# Employee Management System

![Project Banner](![image](https://github.com/user-attachments/assets/b68e16ac-a847-4f98-83c0-47fffad7509d))<!-- Add a banner image if available -->

The **Employee Management System** is a web-based application designed to streamline employee management tasks such as attendance tracking, leave management, salary management, and employee data administration. It provides features for both employees and administrators, ensuring efficient and organized workforce management.

---

## Features

### **User Features**
1. **Profile Management:**
   - View and update personal details.
   - Change password securely.

2. **Attendance Tracking:**
   - Check-in and check-out with manual time selection.
   - View attendance history in a calendar format.

3. **Leave Management:**
   - Apply for leave with start and end dates.
   - View leave application status (Pending, Approved, Rejected).

4. **Salary Details:**
   - View salary information (if set by the admin).

### **Admin Features**
1. **Employee Management:**
   - Add, edit, and delete employee records.
   - Export and import employee data via Excel.

2. **Attendance Overview:**
   - View real-time attendance statistics (total employees, present, absent, on leave).

3. **Leave Management:**
   - Approve or reject leave applications.
   - View all leave applications.

4. **Salary Management:**
   - Set and update employee salaries based on experience and performance.

---

## Technologies Used

### **Frontend**
- **HTML5, CSS3, JavaScript**: For building the user interface.
- **Bootstrap**: For responsive and modern UI design.
- **Thymeleaf**: For server-side rendering of HTML templates.

### **Backend**
- **Java (Spring Boot)**: For building the application logic and APIs.
- **MySQL**: For database management.
- **JPA/Hibernate**: For ORM (Object-Relational Mapping).

### **Tools**
- **Maven**: For dependency management.
- **Apache POI**: For Excel file handling (import/export).
- **JavaMail**: For sending email notifications.

---

## Setup Instructions

### **Prerequisites**
1. **Java Development Kit (JDK)**: Version 17 or higher.
2. **MySQL**: Installed and running.
3. **Maven**: Installed for dependency management.
4. **IDE**: IntelliJ IDEA, Eclipse, or any Java-supported IDE.

### **Steps to Run the Project**
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/mihirjoshi26/employee-management-system.git
   cd employee-management-system
   ```

2. **Set Up the Database:**
   - Create a MySQL database named `employee_management`.
   - Update the `application.properties` file with your database credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build the Project:**
   ```bash
   mvn clean install
   ```

4. **Run the Application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application:**
   - Open your browser and navigate to `http://localhost:8080`.
   - Use the following credentials to log in:
     - **Admin**: `admin@example.com` / `admin123`
     - **Employee**: `employee@example.com` / `abc123`

---

## Project Structure

```
employee-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/employee/
│   │   │       ├── controller/          # Controllers for handling requests
│   │   │       ├── models/              # Entity classes (e.g., Client, Attendance)
│   │   │       ├── repositories/        # Database repositories
│   │   │       ├── services/            # Business logic and services
│   │   │       └── EmployeeApplication.java # Main application class
│   │   └── resources/
│   │       ├── static/                  # Static files (CSS, JS, images)
│   │       ├── templates/               # Thymeleaf HTML templates
│   │       └── application.properties   # Configuration file
│   └── test/                            # Unit and integration tests
├── pom.xml                              # Maven dependencies
└── README.md                            # Project documentation
```

---

## Screenshots

### **User Profile Page**
![User Profile](![image](https://github.com/user-attachments/assets/f8a22bd2-a1eb-4dcf-9ad3-1c23dfae3e0c)
)

### **Admin Dashboard**
![Admin Dashboard](![image](https://github.com/user-attachments/assets/de26c06d-b1ac-4de8-9df9-0edeb62fbc0d)
)

### **Attendance Calendar**
![Attendance Calendar](![image](https://github.com/user-attachments/assets/563d977e-9db5-4c4c-a343-7e1b0aada7b9)
)

---

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes.
4. Submit a pull request.

---

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## Acknowledgments

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/doc/)

---

## Contact

For any questions or feedback, feel free to reach out:

- **Joshi Mihir**  
- **Email**: mihir26joshi@gmail.com  
- **GitHub**: [mihirjoshi26](https://github.com/mihirjoshi26)

---

Thank you for checking out the Employee Management System! 🚀

---

You can copy and paste this content into a `README.md` file in your project repository. Let me know if you need further assistance!
