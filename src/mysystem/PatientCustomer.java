package mysystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PatientCustomer {
    Scanner sc = new Scanner(System.in);
    config conf = new config();

    public void patientInfo() {
        String response;
        do {
            System.out.println("====================================|");
            System.out.println("                                    |");
            System.out.println("     PATIENT MANAGEMENT SYSTEM      |");
            System.out.println("                                    |");
            System.out.println("====================================|");
            System.out.println("1.        ADD PATIENT               |");
            System.out.println("2.      VIEW ALL PATIENTS           |");
            System.out.println("3.       UPDATE PATIENT             |");
            System.out.println("4.       DELETE PATIENT             |");
            System.out.println("5.           EXIT                   |");
            System.out.println("====================================|");
            System.out.print("ENTER (1-5) ONLY ! :"); 

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
                    System.out.println("Invalid action. Please select a valid option.");
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
        System.out.print("Patient Email: ");
        String email = sc.nextLine(); 
        System.out.print("Patient Gender: ");
        String gender = sc.nextLine(); 
        
        String inOutStatus = getInOutStatus();

        String sql = "INSERT INTO tbl_patient (First_Name, Last_Name, Age, Gender,Address, Contact_No, Email, InOutStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        conf.addRecords(sql, firstName, lastName, address, contactNo, age, email, gender, inOutStatus);
        System.out.println("Patient added successfully!"); 
    }

    public void viewPatients() {
        String qry = "SELECT * FROM tbl_patient";
        String[] hdrs = {"Patient ID", "First Name", "Last Name", "Age", "Gender","Address", "Contact No", "Email", "In/Out Status"};
        String[] clmn = {"patient_id", "First_Name", "Last_Name", "Age", "Gender","Address", "Contact_No", "Email", "InOutStatus"};
        conf.viewRecords(qry, hdrs, clmn); 
    }

    public void updatePatient() {
        int patientId = getValidInteger("Enter Patient ID to Update: ", 1, Integer.MAX_VALUE);
        System.out.print("New First Name: ");
        String firstName = sc.nextLine();
        System.out.print("New Last Name: ");
        String lastName = sc.nextLine();
        int age = getValidInteger("New Age: ", 0, 120); 
        System.out.print("New Gender: ");
        String gender = sc.nextLine();
        System.out.print("New Address : ");
        String address = sc.nextLine();
        System.out.print("New Contact Number : ");
        String contactNo = sc.nextLine();
        System.out.print("New Email: ");
        String email = sc.nextLine();
        
        String inOutStatus = getInOutStatus();

        String qry = "UPDATE tbl_patient SET First_Name = ?, Last_Name = ?, Age = ?, Gender = ?, Address = ?, Contact_No = ?, Email = ?, InOutStatus = ? WHERE patient_id = ?";
        conf.updateRecords(qry, firstName, lastName, age, gender, address, contactNo, email, inOutStatus, patientId); 
        System.out.println("Patient updated successfully!"); 
    }

    public void deletePatient() {
        int patientId = getValidInteger("Enter Patient ID to delete: ", 1, Integer.MAX_VALUE); 
        String qry = "DELETE FROM tbl_patient WHERE patient_id = ?";
        conf.deleteRecords(qry, patientId); 
        System.out.println("Patient deleted successfully!");
    }

    private int getValidAction(int min, int max) {
        int action = -1;
        while (action < min || action > max) {
            try {
                action = sc.nextInt();
                sc.nextLine(); 
                if (action < min || action > max) {
                    System.out.println("Invalid option. Please enter a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
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
