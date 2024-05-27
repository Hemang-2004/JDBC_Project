import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

@SuppressWarnings("unused")
public class Main {
    static final String user="root";
    static final String password="Hemang#2004";
    public static void execLines (String pathx,Connection conn,Statement stmt)
    {
        // Implementation remains the same as before
        String lines="";
        Path path=Paths.get(pathx);
        byte [] bytes=null;
        try
        {
            bytes=Files.readAllBytes(path);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        lines=new String(bytes);
        String[] arrlines = lines.split(";");
        for(String line:arrlines)
        {
            if (!line.trim().isEmpty())
            {
                try
                {
                    stmt.executeUpdate(line);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // public static void addPatient(Connection conn, Statement stmt, Scanner sc) throws SQLException {
    //     System.out.println("Enter patient details:");
    //     System.out.print("Name: ");
    //     String name = sc.next();
    //     System.out.print("Date of Birth (yyyy-mm-dd): ");
    //     String dob = sc.next();
    //     System.out.print("Gender (M/F): ");
    //     String gender = sc.next();
    //     System.out.print("Ward Number: ");
    //     int wardNumber = sc.nextInt();
    //     System.out.print("Ward: ");
    //     String ward = sc.next();
    //     System.out.print("Patient Id: ");
    //     int patientId = sc.nextInt();
    //     // Insert patient into the database
    //     String insertPatientQuery = "INSERT INTO patients (patient_name, dob, gender, wardNumber, Ward, patientId) " +
    //                                 "VALUES ('"+name+"', '" + dob + "', '" + gender + "', " + wardNumber + ", '"+ward+"' ,"+patientId+");";
    //     stmt.executeUpdate(insertPatientQuery);
    //     System.out.println("Patient added successfully!");
    // }
    public static void addPatient(Connection conn, Statement stmt, Scanner sc) {
        try {
            conn.setAutoCommit(false); // Start a transaction
    
            System.out.println("Enter patient details:");
            System.out.print("Name: ");
            String name = sc.next();
            System.out.print("Date of Birth (yyyy-mm-dd): ");
            String dob = sc.next();
            System.out.print("Gender (M/F): ");
            String gender = sc.next();
            System.out.print("Ward Number: ");
            int wardNumber = sc.nextInt();
            
            // Check if user wants to enter a ward
            String ward = null;
            System.out.print("Do you want to enter a ward? (Y/N): ");
            String wantWard = sc.next();
            if(wantWard.equalsIgnoreCase("Y")) 
            {
                System.out.print("Ward: ");
                ward = sc.next();
            }
            else
            {
                ward =null;
            }
            
    
            System.out.print("Patient Id: ");
            int patientId = sc.nextInt();
    
            if (name == null || dob == null || gender == null || ward == null) {
                System.out.println("Null character entered. Rolling back transaction.");
                conn.rollback();
                return;
            }
    
            // Check if patientId already exists
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM patients WHERE patientId = " + patientId);
            if (resultSet.next()) {
                System.out.println("Patient with ID " + patientId + " already exists. Rolling back transaction.");
                conn.rollback();
                return;
            }
    
            // Insert patient into the database
            String insertPatientQuery = "INSERT INTO patients (patient_name, dob, gender, wardNumber, Ward, patientId) " +
                                        "VALUES ('" + name + "', '" + dob + "', '" + gender + "', " + wardNumber + ", '" + ward + "' ," + patientId + ")";
            stmt.executeUpdate(insertPatientQuery);
    
            // Commit the transaction
            conn.commit();
            System.out.println("Patient added successfully!");
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    System.err.print("Transaction is being rolled back.");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conn.setAutoCommit(true); // Reset auto-commit mode
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    
    
    
    public static void addDoctor(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        System.out.println("Enter doctor details:");
        System.out.print("Name: ");
        String name = sc.next();
        System.out.print("Date of Birth (yyyy-mm-dd): ");
        String dob = sc.next();
        System.out.print("Salary: ");
        double salary = sc.nextDouble();
        System.out.print("Gender (M/F): ");
        String gender = sc.next();
        System.out.println("Enter the doctor ID");
        int docId = sc.nextInt();
        // Insert doctor into the database
        String insertDoctorQuery = "INSERT INTO doctors (doctor_name, dob, salary, gender, docId) " +
                                    "VALUES ('" + name + "', '" + dob + "', " + salary + ", '" + gender + "' ,"+docId+")";
        stmt.executeUpdate(insertDoctorQuery);
        System.out.println("Doctor added successfully!");
    }
    
    public static void addRoom(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        try {
            conn.setAutoCommit(false); // Start a transaction
    
            System.out.println("Enter room details:");
            System.out.print("Room Number: ");
            int newRoomNumber = sc.nextInt();
    
            // Ask user for patient ID to assign to the room
            System.out.print("Enter patient ID to assign to the room: ");
            int patientId = sc.nextInt();
    
            // Check if the patient ID is valid
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM patients WHERE patientId = " + patientId);
            if (!resultSet.next()) {
                System.out.println("Invalid patient ID. Rolling back transaction.");
                conn.rollback();
                throw new SQLException("Invalid patient ID");
            }
    
            // If the patient is already assigned to a room, remove them from that room
            ResultSet resultSet1 = stmt.executeQuery("SELECT * FROM room WHERE patId = " + patientId);
            if (resultSet1.next()) {
                System.out.println("Patient is already assigned to a room. Removing them from that room.");
                int oldRoomNumber = resultSet1.getInt("roomNumber");
                String deleteOldRoomQuery = "DELETE FROM room WHERE roomNumber = " + oldRoomNumber + " AND patId = " + patientId;
                stmt.executeUpdate(deleteOldRoomQuery);
            }
    
            // Insert a new entry with the new room number
            String insertNewRoomQuery = "INSERT INTO room (roomNumber, patId) VALUES (" + newRoomNumber + ", " + patientId + ")";
            stmt.executeUpdate(insertNewRoomQuery);
    
            System.out.println("Room updated successfully!");
    
            // Commit the transaction
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            if (conn != null) {
                System.err.println("Transaction is being rolled back.");
                conn.rollback();
                throw new SQLException("Rollback occurred. Exiting the code.");
            }
        } finally {
            conn.setAutoCommit(true); // Reset auto-commit mode
        }
    }
    
    
    


    public static void addMedicine(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        System.out.println("Enter medicine details:");
        System.out.print("Medicine Name: ");
        String name = sc.next();
        System.out.println("Enter the medicine ID");
        int mid = sc.nextInt();
        
        // Insert medicine into the database
        String insertMedicineQuery = "INSERT INTO medicine (mid, name) " +
                                    "VALUES (" + mid + ", '" + name + "')";
        stmt.executeUpdate(insertMedicineQuery);
        System.out.println("Medicine added successfully!");
    }
    
    
    public static void updatePatient(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        System.out.println("Enter patient details to be updated");
        System.out.println("Enter the patient Id for uniqueness");
        int patientId = sc.nextInt();
        System.out.println("Enter what do you want to update");
        System.out.println("Enter 1 for name to be updated");
        System.out.println("Enter 2 for date to be updated");
        System.out.println("Enter 3 for ward to be updated");
    
        int choice = sc.nextInt();
        String sql;
        PreparedStatement pstmt;
    
        switch (choice) {
            case 1:
                System.out.println("Enter the new name:");
                String patient_name = sc.next();
                sql = "UPDATE patients SET patient_name = ? WHERE patientId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, patient_name); // Set patient_name as a string
                pstmt.setInt(2, patientId);       // Set patientId as an integer
                pstmt.executeUpdate();
                System.out.println("Patient name updated successfully.");
                break;
            case 2:
                System.out.println("Enter the new date of birth (YYYY-MM-DD):");
                String dob = sc.next();
                sql = "UPDATE patients SET dob = ? WHERE patientId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, dob); // Set dob as a string
                pstmt.setInt(2, patientId);       // Set patientId as an integer
                pstmt.executeUpdate();
                System.out.println("Date of birth updated successfully.");
                break;
            case 3:
                System.out.println("Enter the new ward:");
                String ward = sc.next();
                sql = "UPDATE patients SET Ward = ? WHERE patientId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, ward); // Set ward as a string
                pstmt.setInt(2, patientId);       // Set patientId as an integer
                pstmt.executeUpdate();
                System.out.println("Ward updated successfully.");
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
    

    public static void updateDoctor(Connection conn, Statement stmt, Scanner sc) throws SQLException {
    System.out.println("Enter doctor's details to be updated");
    System.out.println("Enter the doctor Id for uniqueness");
    int docId = sc.nextInt();
    System.out.println("Enter what do you want to update");
    System.out.println("Enter 1 for name to be updated");
    System.out.println("Enter 2 for date of birth to be updated");
    System.out.println("Enter 3 for salary to be updated");

    int choice = sc.nextInt();
    String sql;
    PreparedStatement pstmt;

    switch (choice) {
        case 1:
            System.out.println("Enter the new name:");
            String doctor_name = sc.next();
            sql = "UPDATE doctors SET doctor_name = ? WHERE docId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, doctor_name); // Set doctor_name as a string
            pstmt.setInt(2, docId);       // Set docId as an integer
            pstmt.executeUpdate();
            System.out.println("Doctor's name updated successfully.");
            break;
        case 2:
            System.out.println("Enter the new date of birth (YYYY-MM-DD):");
            String dob = sc.next();
            sql = "UPDATE doctors SET dob = ? WHERE docId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dob); // Set dob as a string
            pstmt.setInt(2, docId);       // Set docId as an integer
            pstmt.executeUpdate();
            System.out.println("Doctor's date of birth updated successfully.");
            break;
        case 3:
            System.out.println("Enter the new salary:");
            BigDecimal salary = sc.nextBigDecimal();
            sql = "UPDATE doctors SET salary = ? WHERE docId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, salary); // Set salary as a BigDecimal
            pstmt.setInt(2, docId);       // Set docId as an integer
            pstmt.executeUpdate();
            System.out.println("Doctor's salary updated successfully.");
            break;
        default:
            System.out.println("Invalid choice.");
            break;
    }
}

public static void updateMedicine(Connection conn, Scanner sc) throws SQLException {
    System.out.println("Enter medicine details to be updated");
    System.out.println("Enter the medicine ID for uniqueness");
    int medicineId = sc.nextInt();
    System.out.println("Enter what you want to update");
    System.out.println("Enter 1 for name to be updated");

    int choice = sc.nextInt();
    String sql;
    PreparedStatement pstmt;

    switch (choice) {
        case 1:
            System.out.println("Enter the new name:");
            String medicineName = sc.next();
            sql = "UPDATE medicine SET name = ? WHERE mid = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, medicineName); // Set medicine name as a string
            pstmt.setInt(2, medicineId);       // Set medicineId as an integer
            pstmt.executeUpdate();
            System.out.println("Medicine name updated successfully.");
            break;
        default:
            System.out.println("Invalid choice.");
            break;
    }
}


public static void PrintPatients(Connection conn, Statement stmt, Scanner sc) throws SQLException {
    System.out.println("Choose an option:");
    System.out.println("1. Print list of all patients");
    System.out.println("2. Print specific details for a patient");

    int choice = sc.nextInt();

    switch (choice) {
        case 1:
            // Print list of all patients
            String queryAllPatients = "SELECT patient_name FROM patients";
            ResultSet rsAllPatients = stmt.executeQuery(queryAllPatients);
            System.out.println("List of all patients:");
            while (rsAllPatients.next()) {
                String patient_name = rsAllPatients.getString("patient_name");
                System.out.println(patient_name);
            }
            break;
        case 2:
            // Print specific details for a selected patient
            System.out.println("Enter the patient ID:");
            int patientId = sc.nextInt();
            String queryPatientDetails = "SELECT * FROM patients WHERE patientId = " + patientId;
            ResultSet rsPatientDetails = stmt.executeQuery(queryPatientDetails);
            if (rsPatientDetails.next()) {
                String patient_name = rsPatientDetails.getString("patient_name");
                String dob = rsPatientDetails.getString("dob");
                String gender = rsPatientDetails.getString("gender");
                int wardNumber = rsPatientDetails.getInt("wardNumber");
                String ward = rsPatientDetails.getString("Ward");
                int patientIdPrint = rsPatientDetails.getInt("patientId");
                System.out.println("Patient details:");
                System.out.println("Name: " + patient_name);
                System.out.println("DOB: " + dob);
                System.out.println("Gender: " + gender);
                System.out.println("Ward Number: " + wardNumber);
                System.out.println("Ward: " + ward);
                System.out.println("Patient ID: " + patientIdPrint);
            } else {
                System.out.println("Patient with ID " + patientId + " not found.");
            }
            break;
        default:
            System.out.println("Invalid choice.");
            break;
    }
}

public static void PrintDoctors(Connection conn, Statement stmt, Scanner sc) throws SQLException {
    System.out.println("Choose an option:");
    System.out.println("1. Print list of all doctors");
    System.out.println("2. Print specific details for a doctor");

    int choice = sc.nextInt();

    switch (choice) {
        case 1:
            // Print list of all doctors
            String queryAllDoctors = "SELECT doctor_name FROM doctors";
            ResultSet rsAllDoctors = stmt.executeQuery(queryAllDoctors);
            System.out.println("List of all doctors:");
            while (rsAllDoctors.next()) {
                String doctor_name = rsAllDoctors.getString("doctor_name");
                System.out.println(doctor_name);
            }
            break;
        case 2:
            // Print specific details for a selected doctor
            System.out.println("Enter the doctor ID:");
            int doctorId = sc.nextInt();
            String queryDoctorDetails = "SELECT * FROM doctors WHERE docId = " + doctorId;
            ResultSet rsDoctorDetails = stmt.executeQuery(queryDoctorDetails);
            if (rsDoctorDetails.next()) {
                String doctor_name = rsDoctorDetails.getString("doctor_name");
                String dob = rsDoctorDetails.getString("dob");
                double salary = rsDoctorDetails.getDouble("salary");
                String gender = rsDoctorDetails.getString("gender");
                int docIdPrint = rsDoctorDetails.getInt("docId");
                System.out.println("Doctor details:");
                System.out.println("Name: " + doctor_name);
                System.out.println("DOB: " + dob);
                System.out.println("Salary: " + salary);
                System.out.println("Gender: " + gender);
                System.out.println("Doctor ID: " + docIdPrint);
            } else {
                System.out.println("Doctor with ID " + doctorId + " not found.");
            }
            break;
        default:
            System.out.println("Invalid choice.");
            break;
    }
}



public static void PrintMedicine(Connection conn, Statement stmt, Scanner sc) throws SQLException {
    System.out.println("Choose an option:");
    System.out.println("1. Print list of all medicines");
    System.out.println("2. Print specific details for a medicine");

    int choice = sc.nextInt();

    switch (choice) {
        case 1:
            // Print list of all medicines
            String queryAllMedicines = "SELECT name FROM medicine";
            ResultSet rsAllMedicines = stmt.executeQuery(queryAllMedicines);
            System.out.println("List of all medicines:");
            while (rsAllMedicines.next()) {
                String medicineName = rsAllMedicines.getString("name");
                System.out.println(medicineName);
            }
            break;
        case 2:
            // Print specific details for a selected medicine
            System.out.println("Enter the medicine ID:");
            int medicineId = sc.nextInt();
            String queryMedicineDetails = "SELECT * FROM medicine WHERE mid = " + medicineId;
            ResultSet rsMedicineDetails = stmt.executeQuery(queryMedicineDetails);
            if (rsMedicineDetails.next()) {
                String medicineName = rsMedicineDetails.getString("name");
                int medicineIdPrint = rsMedicineDetails.getInt("mid");
                System.out.println("Medicine details:");
                System.out.println("Name: " + medicineName);
                System.out.println("Medicine ID: " + medicineIdPrint);
            } else {
                System.out.println("Medicine with ID " + medicineId + " not found.");
            }
            break;
        default:
            System.out.println("Invalid choice.");
            break;
    }
}

public static void printRoomNumbers(Connection conn, Statement stmt, Scanner sc) throws SQLException {
    try {
        System.out.println("Choose an option:");
        System.out.println("1. Print all room numbers and patient IDs");
        System.out.println("2. Print specific room number and patient ID");

        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                // Print all room numbers and patient IDs
                String queryAllRoomNumbers = "SELECT roomNumber, patId FROM room";
                ResultSet rsAllRoomNumbers = stmt.executeQuery(queryAllRoomNumbers);
                
                System.out.println("List of all room numbers and patient IDs:");
                while (rsAllRoomNumbers.next()) {
                    int roomNumber = rsAllRoomNumbers.getInt("roomNumber");
                    int patId = rsAllRoomNumbers.getInt("patId");
                    System.out.println("Room Number: " + roomNumber + ", Patient ID: " + patId);
                }
                break;
            case 2:
                // Print specific room number and patient ID based on constraint
                System.out.print("Enter the patient ID: ");
                int patientId = sc.nextInt();
                
                String queryRoomNumberForPatient = "SELECT roomNumber FROM room WHERE patId = " + patientId;
                ResultSet rsRoomNumberForPatient = stmt.executeQuery(queryRoomNumberForPatient);
                
                if (rsRoomNumberForPatient.next()) {
                    int roomNumber = rsRoomNumberForPatient.getInt("roomNumber");
                    System.out.println("Room Number for Patient ID " + patientId + ": " + roomNumber);
                } else {
                    System.out.println("No room found for Patient ID " + patientId);
                }
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    } catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
}



    public static void deletePatient(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        System.out.println("Enter patient ID that has Died (RIP)");
        int patientId = sc.nextInt();
        stmt.executeUpdate(String.format("delete from patients where patientId = %d", patientId));
    
    }
    public static void deleteDoctor(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        System.out.println("Enter doctor's ID to be deleted ");
        int docId = sc.nextInt();
        // patient_name, dob, gender, wardNumber, Ward, patientId
        stmt.executeUpdate("delete from doctors where docId = "+docId);
        // stmt.executeUpdate(String.format("update doctors set doctor_name = %s where docId = %d", doctor_name, docId));
    }
    public static void deleteMedicine(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        System.out.println("Enter the medicine ID to be deleted:");
        int mid = sc.nextInt();
        
        // Execute SQL query to delete the medicine with the given ID
        stmt.executeUpdate("DELETE FROM medicine WHERE mid = " + mid);
        System.out.println("Medicine with ID " + mid + " deleted successfully!");
    }
    
    public static void join(Connection conn, Statement stmt, Scanner sc) throws SQLException {
        System.out.print("Enter patient id: ");
        int pid = sc.nextInt();
        ResultSet rs = stmt.executeQuery(String.format("select medicine.mid, medicine.name from medicine inner join medication on medicine.mid = medication.mid where pid = %d", pid));
        while (rs.next()) {
            int id = rs.getInt("medicine.mid");
            String name = rs.getString("medicine.name");
            System.out.print("id: " + id);
            System.out.println(", name: " + name);
        }
    }

    public static void interactive(Connection conn, Statement stmt) throws SQLException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        int choice;
        System.out.println("\n-----xxxx----xxxx----WELCOME TO THE HOSPITAL DATABASE----xxxx----xxxx-----");
        System.out.println("\n*********************************  Menu  *********************************");
        System.out.println("1. Add a new patient to the database");
        System.out.println("2. Add a new doctor to the Hospital");
        System.out.println("3. Add a new medicine to the Pharmacy");
        System.out.println("4. Update patient details");
        System.out.println("5. Update doctor details");
        System.out.println("6. Update medicine details");
        System.out.println("7. Delete patient details");
        System.out.println("8. Delete doctor details");
        System.out.println("9. Delete medicine details if the stock is over");
        System.out.println("10. Print The Details of Patients");
        System.out.println("11. Print The Details of Doctors");
        System.out.println("12. Print The existing stock of Medicines");
        System.out.println("13. Finding the medicine given to the Patient");
        System.out.println("14. For allocating the rooms to the patient");
        System.out.println("15. For printing all the rooms");
        System.out.println("16. Exit");
        
        System.out.println("****************************************************************************\n");
        
        System.out.print("Enter your choice: ");
        choice = sc.nextInt();
        sc.nextLine(); 
        
        switch (choice) {
            case 1:
                addPatient(conn, stmt, sc);
                break;
            case 2:
                addDoctor(conn, stmt, sc);
                break;
            case 3:
                addMedicine(conn, stmt, sc);
                break;
            case 4:
                updatePatient(conn, stmt, sc);
                break;
            case 5:
                updateDoctor(conn, stmt, sc);
                break;
            case 6:
                updateMedicine(conn, sc);
                break;
            case 7:
                deletePatient(conn, stmt, sc);
                break;
            case 8:
                deleteDoctor(conn, stmt, sc);
                break;
            case 9:
                deleteMedicine(conn, stmt, sc);
                break;
            case 10:
                PrintPatients(conn, stmt, sc);
                break;
            case 11:
                PrintDoctors(conn, stmt, sc);
                break;
            case 12:
                PrintMedicine(conn, stmt, sc);
                break;
            case 13:
                join(conn,stmt,sc);
                break;
            case 14:
                addRoom(conn, stmt, sc);
                break;
            case 15:
                printRoomNumbers(conn, stmt, sc);
                break;
            case 16:
                System.out.println("Exiting...");
                System.exit(0);

            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 10.");
        }
        Thread.sleep(1000);
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital?allowPublicKeyRetrieval=true&useSSL=false", user, password);
            stmt = conn.createStatement();
            
            // Uncomment the below lines to create tables and insert data
            // execLines("sql/tables.sql", conn, stmt);
            // execLines("sql/populate.sql", conn, stmt);
            
            while (true) {
                interactive(conn, stmt);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}











