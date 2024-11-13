package mysystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Report{
    Scanner input = new Scanner(System.in);
    config conf = new config();

    public void reportMenu(){
        boolean exit = true;
        do {
            System.out.println("|===========================================|");
            System.out.println("|                                           |");
            System.out.println("|              REPORTS MENU                 |");
            System.out.println("|                                           |");
            System.out.println("|===========================================|");
            System.out.println("|1.           VIEW ALL REPORTS              |");
            System.out.println("|2.         INDIVIDUAL REPORTS              |");
            System.out.println("|3.              EXIT                       |");
            System.out.println("|===========================================|");
            System.out.print("\tCHOOSE A NUMBER (1-3): ");
            
            int choice;
            try {
                choice = input.nextInt();
            } catch (Exception e) {
                input.next();
                choice = -1;
            }

            switch (choice) {
                case 1:
                    viewReport();
                    break;
                case 2:
                    individualReport();
                    break;
                case 3:
                    exit = false;
                    break;
                default:
                    System.out.println("ERROR NOT FOUND!!! CHOOSE BETWEEN (1-3).");
            }
        } while (exit);

        System.out.println("\tTHANK YOU FOR USING THE REPORTS MENU!!!");
    }

    public void view1(){
        
        String qry = "SELECT patient_id, First_Name, Last_Name, Address, Contact_No, Age, Email, Gender, InOutStatus FROM tbl_patient";
        String[] hdrs = {"PATIENT ID", "FIRST NAME", "LAST NAME", "ADDRESS", "CONTACT NUMBER", "AGE", "EMAIL ACCOUNTS", "GENDER", "STATUS IN/OUT"};
        String[] clmn = {"patient_id", "First_Name", "Last_Name", "Address", "Contact_No", "Age", "Email", "Gender", "InOutStatus"};
        conf.viewRecords(qry, hdrs, clmn);
    }

    private void viewReport(){
        System.out.println("|============================================================|");
        System.out.println("|                VIEWING PATIENT AND BILL REPORTS            |");
        System.out.println("|============================================================|");

        String query = "SELECT p.patient_id, p.First_Name, p.Last_Name, p.Address, p.Contact_No, p.Age, p.Email, p.Gender, p.InOutStatus, "
                     + "b.billing_id, b.admission_date, b.discharge_date, b.treatment_type, "
                     + "b.total_bill_amount, b.payment_status "
                     + "FROM tbl_patient p LEFT JOIN tbl_billing b ON p.patient_id = b.patient_id";
        
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("|======================================================================================================================================================================================================================================================================================|");
            System.out.println("| PATIENT ID      |   FIRST NAME    |  LAST NAME      |     ADDRESS     | CONTACT NUMBER  |  AGE     |       EMAIL ACCOUNT        |  GENDER  | STATUS IN/OUT  |  BILLING ID  | ADMISSION DATE  |  DISCHARGE DATE | TREATMENT TYPE  |  TOTAL BILLS     |  PAYMENT STATUS PAID/UNPAID    |");
            System.out.println("|======================================================================================================================================================================================================================================================================================|");

            while (rs.next()) {
                System.out.printf("| %-15d | %-15s | %-15s | %-15s | %-15s | %-8s | %-26s | %-8s | %-14s | %-12s | %-15s | %-15s | %-15s | %-16s | %-28s   |\n", 
                                  rs.getInt("patient_id"), 
                                  rs.getString("First_Name"), 
                                  rs.getString("Last_Name"), 
                                  rs.getString("Address"), 
                                  rs.getString("Contact_No"), 
                                  rs.getString("Age"), 
                                  rs.getString("Email"), 
                                  rs.getString("Gender"), 
                                  rs.getString("InOutStatus"), 
                                  rs.getInt("billing_id"), 
                                  rs.getString("admission_date"), 
                                  rs.getString("discharge_date"), 
                                  rs.getString("treatment_type"), 
                                  rs.getString("total_bill_amount"), 
                                  rs.getString("payment_status"));
            }
            System.out.println("|======================================================================================================================================================================================================================================================================================|");

        } catch (SQLException e) {
            System.err.println("ERROR TO GENERATE REPORTS MENU! : " + e.getMessage());
        }
    }
    private void individualReport(){
        view1();
        int patient_id = -1;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("ENTER PATIENT ID TO VIEW BILLING DETAILS!: ");
            try {
                patient_id = input.nextInt();
                input.nextLine();

                String patientQuery = "SELECT * FROM tbl_patient WHERE patient_id = ?";
                String billingQuery = "SELECT * FROM tbl_billing WHERE patient_id = ?";

                try (Connection conn = conf.connectDB();
                     PreparedStatement patientStmt = conn.prepareStatement(patientQuery)){

                    patientStmt.setInt(1, patient_id);
                    ResultSet rsPatient = patientStmt.executeQuery();

                    if (rsPatient.next()) {
                        System.out.println("|========================================================|");
                        System.out.println("|          PATIENT INFO        |    PATIENT DETAILS      |");
                        System.out.println("|========================================================|");
                        System.out.printf("| %-28s |%-25s|\n", "Patient ID:", rsPatient.getInt("patient_id"));
                        System.out.printf("| %-28s |%-25s|\n", "First Name:", rsPatient.getString("First_Name"));
                        System.out.printf("| %-28s |%-25s|\n", "Last Name:", rsPatient.getString("Last_Name"));
                        System.out.printf("| %-28s |%-25s|\n", "Address:", rsPatient.getString("Address"));
                        System.out.printf("| %-28s |%-25s|\n", "Contact No:", rsPatient.getString("Contact_No"));
                        System.out.printf("| %-28s |%-25s|\n", "Age:", rsPatient.getString("Age"));
                        System.out.printf("| %-28s |%-25s|\n", "Email:", rsPatient.getString("Email"));
                        System.out.printf("| %-28s |%-25s|\n", "Gender:", rsPatient.getString("Gender"));
                        System.out.printf("| %-28s |%-25s|\n", "InOutStatus:", rsPatient.getString("InOutStatus"));
                        System.out.println("|========================================================|");

                        System.out.println("|========================================================|");
                        System.out.println("|                 BILLING INFORMATION                    |");
                        System.out.println("|========================================================|");
                        System.out.println("|===================================================================================================================================================|");
                        System.out.printf(" | %-20s| %-16s | %-15s | %-15s | %-15s | %-15s | %-18s      |\n", 
                                          "PATIENT NAME", "BILLING ID", "ADMISSION DATE", "DISCHARGE DATE", 
                                          "TREATMENT TYPE", "TOTAL BILLS", "PAYMENT STATUS PAID/UNPAID");
                        System.out.println("|===================================================================================================================================================|");

                        try (PreparedStatement billingStmt = conn.prepareStatement(billingQuery)){
                            billingStmt.setInt(1, patient_id);
                            ResultSet rsBilling = billingStmt.executeQuery();

                            boolean hasBillingRecords = false;
                            while (rsBilling.next()) {
                                hasBillingRecords = true;
                                String patientName = rsPatient.getString("First_Name") + " " + rsPatient.getString("Last_Name");

                                System.out.printf("| %-20s | %-16d | %-15s | %-15s | %-15s | %-15s | %-15s                 |\n", 
                                                  patientName, 
                                                  rsBilling.getInt("billing_id"), 
                                                  rsBilling.getString("admission_date"), 
                                                  rsBilling.getString("discharge_date"), 
                                                  rsBilling.getString("treatment_type"), 
                                                  rsBilling.getString("total_bill_amount"), 
                                                  rsBilling.getString("payment_status"));
                            }

                            if (!hasBillingRecords){
                                System.out.println("| NO BILLING RECORDS FOUND FOR THIS PATIENT.                                                 |");
                            }
                        }
                        System.out.println("|===================================================================================================================================================|");

                        validInput = true; 
                    } else {
                        System.out.println("PATIENT WITH ID " + patient_id + " NOT FOUND.");
                    }
                } catch (SQLException e) {
                    System.err.println("ERROR GENERATING INDIVIDUAL REPORT: " + e.getMessage());
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid patient ID.");
                input.next(); 
            }
        }
    }
    }