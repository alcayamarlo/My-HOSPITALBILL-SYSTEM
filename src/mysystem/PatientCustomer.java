package mysystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PatientCustomer {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    public void patientInfo() {
        String response;
        do {
            System.out.println("|====================================|");
            System.out.println("|                                    |");
            System.out.println("|     PATIENTS MANAGEMENT SYSTEM     |");
            System.out.println("|                                    |");
            System.out.println("|====================================|");
            System.out.println("|1.        ADD PATIENT DETAILS       |");
            System.out.println("|2.          VIEW DETAILS            |");
            System.out.println("|3.         UPDATE DETAILS           |");
            System.out.println("|4.         DELETE DETAILS           |");
            System.out.println("|5.            EXIT                  |");
            System.out.println("|====================================|");
            System.out.print("\tCHOOSE A NUMBER (1-5) :");

            int action = getValidAction(1, 5);

            switch (action) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    viewPatients();
                    break;
                case 3:
                    viewPatients();
                    updatePatient();
                    viewPatients();
                    break;
                case 4:
                    viewPatients();
                    deletePatient();
                    viewPatients();
                    break;
                case 5:
                    return;
                default:
                    System.out.print("Invalid action. Please select a valid option. :");
                    break;
            }

            while (true) {
                System.out.print("Do you want to perform another action? (yes/no): ");
                response = sc.nextLine().toLowerCase();
                if (response.equals("yes") || response.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no' only.");
                }
            }

        } while (response.equals("yes"));

        System.out.println("Thank you for using the Patient Management System!");
    }
    
    public void addPatient() {
        System.out.print("Patient First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Patient Last Name: ");
        String lastName = sc.nextLine();
        String address = getAddress();
        String contactNo = getContactNumber();
        int age = getValidInteger("Patient Age: ", 0, 120);
        System.out.print("Patient Gender: ");
        String gender = sc.nextLine();
        System.out.print("Patient Email: ");
        String email = sc.nextLine();

        String inOutStatus = getInOutStatus();

        String sql = "INSERT INTO tbl_patient (First_Name, Last_Name, Address, Contact_No, Age, Gender, Email, InOutStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecords(sql, firstName, lastName, address, contactNo, age, gender, email, inOutStatus);
        System.out.println("Patient added successfully!");
    }

    public void viewPatients() {
        String qry = "SELECT * FROM tbl_patient";
        String[] hdrs = {"PATIENT ID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT NUMBER", "AGE", "GENDER", "EMAIL ACCOUNT", "IN / OUT STATUS"};
        String[] clmn = {"patient_id", "First_Name", "Last_Name", "Address", "Contact_No", "Age", "Gender", "Email", "InOutStatus"};
        conf.viewRecords(qry, hdrs, clmn);
    }

   public void updatePatient() {
    try {
        int patientId;
        patientId = getValidInteger("Enter Patient ID to Update: ", 1, Integer.MAX_VALUE);
        String checkQuery = "SELECT patient_id FROM tbl_patient WHERE patient_id = ?";
        while (!conf.checkIfExists(checkQuery, patientId)) {
            System.out.println("Patient ID not found! Please enter a valid Patient ID:");
            patientId = Integer.parseInt(sc.nextLine()); 
        }

        System.out.print("New First Name: ");
        String firstName = sc.nextLine().trim();
        while (firstName.isEmpty()) {
            System.out.print("First Name cannot be empty. Please enter a valid First Name: ");
            firstName = sc.nextLine().trim();
        }

        System.out.print("New Last Name: ");
        String lastName = sc.nextLine().trim();
        while (lastName.isEmpty()) {
            System.out.print("Last Name cannot be empty. Please enter a valid Last Name: ");
            lastName = sc.nextLine().trim();
        }

        System.out.print("New Address: ");
        String address = sc.nextLine();
        System.out.print("New Contact Number: ");
        String contactNo = sc.nextLine();
        int age = getValidInteger("New Age: ", 0, 120);
        System.out.print("New Gender: ");
        String gender = sc.nextLine();
        System.out.print("New Email: ");
        String email = sc.nextLine();
        String inOutStatus = getInOutStatus();

        String qry = "UPDATE tbl_patient SET First_Name = ?, Last_Name = ?, Address = ?, Contact_No = ?, Age = ?, Gender = ?, Email = ?, InOutStatus = ? WHERE patient_id = ?";
        conf.updateRecords(qry, firstName, lastName, address, contactNo, age, gender, email, inOutStatus, patientId);
        System.out.println("Patient updated successfully!");
    } catch (Exception e) {
        System.err.println("Error updating patient: " + e.getMessage());
    }
}


public void deletePatient() {
    try {
        int patientId = getValidInteger("Enter Patient ID to delete: ", 1, Integer.MAX_VALUE);
        String checkQuery = "SELECT patient_id FROM tbl_patient WHERE patient_id = ?";
        while (!conf.checkIfExists(checkQuery, patientId)) {
            System.out.println("Warning: Patient ID not found. Please enter a valid Patient ID.");
            patientId = getValidInteger("Enter Patient ID to delete: ", 1, Integer.MAX_VALUE);
        }
        String qry = "DELETE FROM tbl_patient WHERE patient_id = ?";
        conf.deleteRecords(qry, patientId);
        System.out.println("Patient deleted successfully!");

    } catch (Exception e) {
        System.err.println("Error deleting patient: " + e.getMessage());
    }
}


    private int getValidAction(int min, int max) {
        int action = -1;
        while (action < min || action > max) {
            try {
                action = sc.nextInt();
                sc.nextLine();
                if (action < min || action > max) {
                    System.out.print("Invalid option. Please enter a number between " + min + " and " + max + " :");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number :");
                sc.nextLine();
            }
        }
        return action;
    }

    private int getValidInteger(String prompt, int min, int max) {
        int value = -1;
        while (value < min || value > max) {
            System.out.print(prompt);
            try {
                value = sc.nextInt();
                sc.nextLine();
                if (value < min || value > max) {
                    System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.nextLine();
            }
        }
        return value;
    }

    private String getAddress() {
        System.out.print("Patient Address: ");
        return sc.nextLine();
    }

    private String getContactNumber() {
        System.out.print("Patient Contact Number: ");
        return sc.nextLine();
    }

    private String getInOutStatus() {
        String inOutStatus = "";
        while (true) {
            System.out.print("Is the patient an In-Patient or Out-Patient? (Enter 'In' or 'Out'): ");
            inOutStatus = sc.nextLine().trim().toLowerCase();
            if (inOutStatus.equals("in")) {
                return "In";
            } else if (inOutStatus.equals("out")) {
                return "Out";
            } else {
                System.out.println("Invalid input. Please enter 'In' or 'Out' only.");
            }
        }
    }
}
