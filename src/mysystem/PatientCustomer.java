package mysystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PatientCustomer {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    public void patientInfo() {
        String response;
        
        do {
            
            System.out.println("|============================================|");
            System.out.println("|                                            |");
            System.out.println("|         PATIENTS MANAGEMENT SYSTEM         |");
            System.out.println("|                                            |");
            System.out.println("|============================================|");
            System.out.println("|                                            |");
            System.out.println("|  1.            ADD PATIENT DETAILS         |");
            System.out.println("|                                            |");
            System.out.println("|  2.              VIEW DETAILS              |");
            System.out.println("|                                            |");
            System.out.println("|  3.             UPDATE DETAILS             |");
            System.out.println("|                                            |");
            System.out.println("|  4.             DELETE DETAILS             |");
            System.out.println("|                                            |");
            System.out.println("|  5.                EXIT                    |");
            System.out.println("|                                            |");
            System.out.println("|============================================|");

            System.out.print("\tCHOOSE A NUMBER (1-5) :");

            int action = -1;
            while (action < 1 || action > 5) {
                try {
                    action = sc.nextInt();
                    sc.nextLine();
                    if (action < 1 || action > 5) {
                        System.out.print("Invalid option. Please enter a number between 1 and 5: ");
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a number: ");
                    sc.nextLine();
                }
            }

            switch (action) {
                case 1:
                    addPatients();
                    break;
                case 2:
                    viewPatients();
                    break;
                case 3:
                    viewPatients();
                    updatePatients();
                    viewPatients();
                    break;
                case 4:
                    viewPatients();
                    deletePatients();
                    viewPatients();
                    break;
                case 5:
                    return;
                default:
                    System.out.print("Invalid action. Please select a valid option: ");
                    break;
            }

            while (true) {
                System.out.print("Do you want to perform another action? (yes/no): ");
                response = sc.nextLine().toLowerCase();
                if (response.equals("yes") || response.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter yes or no only.");
                }
            }

        } while (response.equals("yes"));

        System.out.println("Thank you for using the Patient Management System!");
    }
    
    public void addPatients() {
        System.out.print("Patient First Name: ");
        String First_Name = sc.nextLine();
        System.out.print("Patient Last Name: ");
        String Last_Name = sc.nextLine();
        
        System.out.print("Patient Address: ");
        String Address = sc.nextLine();
        
        System.out.print("Patient Contact Number: ");
        String Contact_No = sc.nextLine();

        int Age = -1;
        while (Age < 0 || Age > 120) {
            System.out.print("Patient Age: ");
            try {
                Age = sc.nextInt();
                sc.nextLine();
                if (Age < 0 || Age > 120) {
                    System.out.println("Invalid input. Please enter a number between 0 and 120.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.nextLine();
            }
        }
        
        System.out.print("Patient Gender: ");
        String Gender = sc.nextLine();
        System.out.print("Patient Email: ");
        String Email = sc.nextLine();

        String InOutStatus = "";
        while (true) {
            System.out.print("Is the patient an In-Patient or Out-Patient? (Enter In or Out): ");
            InOutStatus = sc.nextLine().trim().toLowerCase();
            if (InOutStatus.equals("in")) {
                InOutStatus = "In";
                break;
            } else if (InOutStatus.equals("out")) {
                InOutStatus = "Out";
                break;
            } else {
                System.out.println("Invalid input. Please enter In or Out only.");
            }
        }

        String sql = "INSERT INTO tbl_patient (First_Name, Last_Name, Address, Contact_No, Age, Gender, Email, InOutStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecords(sql, First_Name, Last_Name, Address, Contact_No, Age, Gender, Email, InOutStatus);
        System.out.println("Patient added successfully!");
    }

    public void viewPatients() {
        String qry = "SELECT * FROM tbl_patient";
        String[] hdrs = {"PATIENT ID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT NUMBER", "AGE", "GENDER", "EMAIL ACCOUNT", "IN / OUT STATUS"};
        String[] clmn = {"patient_id", "First_Name", "Last_Name", "Address", "Contact_No", "Age", "Gender", "Email", "InOutStatus"};
        conf.viewRecords(qry, hdrs, clmn);
    }

    public void updatePatients() {
        try {
            System.out.print("Enter Patient ID to Update: ");
            int patient_id = -1;
            while (patient_id < 1) {
                try {
                    patient_id = sc.nextInt();
                    sc.nextLine(); 
                    String checkQuery = "SELECT patient_id FROM tbl_patient WHERE patient_id = ?";
                    
                    if (!conf.checkIfExists(checkQuery, patient_id)) {
                        System.out.println("Patient ID not found! Please try again.");
                        patient_id = -1; 
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a valid ID for Patient: ");
                    sc.nextLine(); 
                }
            }
            System.out.print("New First Name: ");
            String First_Name = sc.nextLine().trim();
            while (First_Name.isEmpty()) {
                System.out.print("First Name cannot be empty. Please enter a valid First Name: ");
                First_Name = sc.nextLine().trim();
            }

            System.out.print("New Last Name: ");
            String Last_Name = sc.nextLine().trim();
            while (Last_Name.isEmpty()) {
                System.out.print("Last Name cannot be empty. Please enter a valid Last Name: ");
                Last_Name = sc.nextLine().trim();
            }

            System.out.print("New Address: ");
            String Address = sc.nextLine();
            System.out.print("New Contact Number: ");
            String Contact_No = sc.nextLine();

            int Age = -1;
            while (Age < 0 || Age > 120) {
                System.out.print("New Age: ");
                try {
                    Age = sc.nextInt();
                    sc.nextLine();
                    if (Age < 0 || Age > 120) {
                        System.out.println("Invalid input. Please enter a number between 0 and 120.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    sc.nextLine();
                }
            }

            System.out.print("New Gender: ");
            String Gender = sc.nextLine();
            System.out.print("New Email: ");
            String Email = sc.nextLine();
            
            String InOutStatus = "";
            while (true) {
                System.out.print("Is the patient an In-Patient or Out-Patient? (Enter 'In' or 'Out'): ");
                InOutStatus = sc.nextLine().trim().toLowerCase();
                if (InOutStatus.equals("in")) {
                    InOutStatus = "In";
                    break;
                } else if (InOutStatus.equals("out")) {
                    InOutStatus = "Out";
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'In' or 'Out' only.");
                }
            }

            String qry = "UPDATE tbl_patient SET First_Name = ?, Last_Name = ?, Address = ?, Contact_No = ?, Age = ?, Gender = ?, Email = ?, InOutStatus = ? WHERE patient_id = ?";
            conf.updateRecords(qry, First_Name, Last_Name, Address, Contact_No, Age, Gender, Email, InOutStatus, patient_id);
            System.out.println("Patient updated successfully!");
        } catch (Exception e) {
            System.err.println("Error updating patient: " + e.getMessage());
        }
    }

    public void deletePatients() {
        try {
            int patient_id = -1;
            while (patient_id < 1) {
                System.out.print("Enter Patient ID to delete: ");
                try {
                    patient_id = sc.nextInt();
                    sc.nextLine();
                    if (patient_id < 1) {
                        System.out.println("Invalid input. Please enter a valid Patient ID.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    sc.nextLine();
                }
            }
            
            String checkQuery = "SELECT patient_id FROM tbl_patient WHERE patient_id = ?";
            while (!conf.checkIfExists(checkQuery, patient_id)) {
                System.out.println("Warning: Patient ID not found. Please enter a valid Patient ID.");
                patient_id = -1;
                while (patient_id < 1) {
                    System.out.print("Enter Patient ID to delete: ");
                    try {
                        patient_id = sc.nextInt();
                        sc.nextLine();
                        if (patient_id < 1) {
                            System.out.println("Invalid input. Please enter a valid Patient ID.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid integer.");
                        sc.nextLine();
                    }
                }
            }
            
            String qry = "DELETE FROM tbl_patient WHERE patient_id = ?";
            conf.deleteRecords(qry, patient_id);
            System.out.println("Patient deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting patient: " + e.getMessage());
        }
    }
}
