package mysystem;

    import java.sql.*;
    import java.util.Scanner;

public class Report {
    Scanner input = new Scanner(System.in);
    config conf = new config();

    public void reportMenu() {
        boolean exit = true;
        do {
            System.out.println("===========================================|");
            System.out.println("                                           |");
            System.out.println("              Report Menu                  |");
            System.out.println("                                           |");
            System.out.println("===========================================|");
            System.out.println("1.           VIEW REPORTS                  |");
            System.out.println("2.         INDIVIDUAL REPORTS              |");
            System.out.println("3.              EXIT                       |");
            System.out.println("===========================================|");
            System.out.print("Choose an option (1-3): ");
            
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
                    System.out.println("Invalid option. Please choose between 1 and 3.");
            }
        } while (exit);

        System.out.println("Thank you for using the Report Generator!");
    }

    public void view1(){
        String qry = "SELECT * FROM tbl_patient";
        String[] hdrs = {"Patient ID", "First Name", "Last Name","Contact No","Address"};
        String[] clmn = {"patient_id", "First_Name", "Last_Name","Contact_No","Address"};
        conf.viewRecords(qry, hdrs, clmn); 
    }
    
    private void viewReport() {
        System.out.println("============================================================|");
        System.out.println("============================================================|");
        System.out.println("                                                            |");
        System.out.println("                Viewing Patient & Billing Report            |");
        System.out.println("                                                            |");
        System.out.println("============================================================|");
        System.out.println("============================================================|");

        String query = "SELECT p.patient_id, p.First_Name, p.Last_Name, p.Contact_No, p.Address, "
                     + "b.billing_id, b.admission_date, b.discharge_date, b.treatment_type, "
                     + "b.total_bill_amount, b.payment_status "
                     + "FROM tbl_patient p LEFT JOIN tbl_billing b ON p.patient_id = b.patient_id";
        
        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.printf("%-10s %-15s %-15s %-15s %-15s %-10s %-15s %-15s %-15s %-15s%n", 
                              "Patient ID", "First Name", "Last Name", "Contact No", "Address", 
                              "Billing ID", "Admission Date", "Discharge Date", "Treatment Type", 
                              "Total Bill", "Payment Status");

            while (rs.next()) {
                System.out.printf("%-10d %-15s %-15s %-15s %-15s %-10d %-15s %-15s %-15s %-15s%n", 
                                  rs.getInt("patient_id"), rs.getString("First_Name"), 
                                  rs.getString("Last_Name"), rs.getString("Contact_No"), 
                                  rs.getString("Address"), rs.getInt("billing_id"), 
                                  rs.getString("admission_date"), rs.getString("discharge_date"), 
                                  rs.getString("treatment_type"), rs.getString("total_bill_amount"), 
                                  rs.getString("payment_status"));
            }
        } catch (SQLException e) {
            System.err.println("Error generating general report: " + e.getMessage());
        }
    }

    private void individualReport() {
        view1();
        System.out.print("Enter Patient ID to view billing details: ");
        int patientId = input.nextInt();

        System.out.println("=========================================================");
        System.out.println("           INDIVIDUAL PATIENT & BILLING REPORT           ");
        System.out.println("=========================================================");

        String patientQuery = "SELECT * FROM tbl_patient WHERE patient_id = ?";
        String billingQuery = "SELECT * FROM tbl_billing WHERE patient_id = ?";
        
        try (Connection conn = conf.connectDB();
             PreparedStatement patientStmt = conn.prepareStatement(patientQuery)) {

            patientStmt.setInt(1, patientId);
            ResultSet rsPatient = patientStmt.executeQuery();

            if (rsPatient.next()) {
                System.out.println("Patient Information:");
                System.out.printf("Patient ID: %d%n", rsPatient.getInt("patient_id"));
                System.out.printf("First Name: %s%n", rsPatient.getString("First_Name"));
                System.out.printf("Last Name: %s%n", rsPatient.getString("Last_Name"));
                System.out.printf("Contact No: %s%n", rsPatient.getString("Contact_No"));
                System.out.printf("Address: %s%n", rsPatient.getString("Address"));
                System.out.printf("Age: %s%n", rsPatient.getString("Age"));
                System.out.printf("Email: %s%n", rsPatient.getString("Email"));

                System.out.println("Billing Information:");
                System.out.printf("%-10s %-15s %-15s %-15s %-15s %-15s%n", 
                                  "Billing ID", "Admission Date", "Discharge Date", 
                                  "Treatment Type", "Total Bill", "Payment Status");

                try (PreparedStatement billingStmt = conn.prepareStatement(billingQuery)) {
                    billingStmt.setInt(1, patientId);
                    ResultSet rsBilling = billingStmt.executeQuery();

                    boolean hasBillingRecords = false;
                    while (rsBilling.next()) {
                        hasBillingRecords = true;
                        System.out.printf("%-10d %-15s %-15s %-15s %-15s %-15s%n", 
                                          rsBilling.getInt("billing_id"), 
                                          rsBilling.getString("admission_date"), 
                                          rsBilling.getString("discharge_date"), 
                                          rsBilling.getString("treatment_type"), 
                                          rsBilling.getString("total_bill_amount"), 
                                          rsBilling.getString("payment_status"));
                    }

                    if (!hasBillingRecords) {
                        System.out.println("No billing records found for this patient.");
                    }
                }
            } else {
                System.out.println("Patient with ID " + patientId + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error generating individual report: " + e.getMessage());
        }
    }
}
