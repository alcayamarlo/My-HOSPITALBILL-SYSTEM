package mysystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BillingSystem {
    Scanner sc = new Scanner(System.in);
    config conf = new config();
    PatientCustomer pc = new PatientCustomer();
    public void billingInfo() {
    String response;
    do {
        System.out.println("=============================================|");
        System.out.println("                                             |");
        System.out.println("           BILLING SYSTEM MANAGEMENT         |");
        System.out.println("                                             |");
        System.out.println("=============================================|");
        
        System.out.println("1.         ADD BILLING RECORD                |");
        System.out.println("2.       VIEW ALL BILLING RECORDS            |");
        System.out.println("3.        UPDATE BILLING RECORD              |");
        System.out.println("4.        DELETE BILLING RECORD              |");
        System.out.println("5.                EXIT                       |");
        System.out.println("=============================================|");

        System.out.print("ENTER (1-5) ONLY! :");
        int action = getValidAction(1, 5); 

        switch (action) {
            case 1:
                addBillingRecord();  
                break;
            case 2:
                viewBillingRecords(); 
                break;
            case 3:
                viewBillingRecords();
                updateBillingRecord(); 
                viewBillingRecords();
                break;
            case 4:
                viewBillingRecords();
                deleteBillingRecord();
                viewBillingRecords();
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

    System.out.println("Thank you for using the Billing Management System!");
}


    public void addBillingRecord() {
        try {
            System.out.print("Patient ID: "); 
            int patientId = getValidInteger("", 1, Integer.MAX_VALUE);
            
            System.out.print("Admission Date (YYYY-MM-DD): ");
            String admissionDate = sc.nextLine();
            
            System.out.print("Discharge Date (YYYY-MM-DD): ");
            String dischargeDate = sc.nextLine();
            
            System.out.print("Treatment Type: ");
            String treatmentType = sc.nextLine();
            double totalBillAmount = getValidDouble("Total Bill Amount: ", 0.0); 
            
            String paymentStatus;
            while (true) {
                System.out.print("Payment Status (paid/unpaid): ");
                paymentStatus = sc.nextLine().toLowerCase();
                if (paymentStatus.equals("paid") || paymentStatus.equals("unpaid")) {
                    break; 
                } else {
                    System.out.println("Invalid input. Please enter 'paid' or 'unpaid' only.");
                }
            }

            String sql = "INSERT INTO tbl_billing (patient_id, admission_date, discharge_date, treatment_type, total_bill_amount, payment_status) VALUES (?, ?, ?, ?, ?, ?)";
            conf.addRecords(sql, patientId, admissionDate, dischargeDate, treatmentType, totalBillAmount, paymentStatus); 
            System.out.println("Billing record added successfully!"); 
        } catch (Exception e) {
            System.err.println("Error adding billing record: " + e.getMessage());
        }
    }

    public void viewBillingRecords() {
        try {
            String qry = "SELECT * FROM tbl_billing";
            String[] hdrs = {"Billing ID", "Patient ID", "Admission Date", "Discharge Date", "Treatment Type", "Total Bill Amount", "Payment Status"};
            String[] clmn = {"billing_id", "patient_id", "admission_date", "discharge_date", "treatment_type", "total_bill_amount", "payment_status"};
            conf.viewRecords(qry, hdrs, clmn);
        } catch (Exception e) {
            System.err.println("Error viewing billing records: " + e.getMessage());
        }
    }

    public void updateBillingRecord() {
        try {
            int billing_id = getValidInteger("Enter Billing ID to Update: ", 1, Integer.MAX_VALUE); 
            
            String qry = "SELECT payment_status FROM tbl_billing WHERE billing_id = ?";
            String currentStatus = conf.getPaymentStatus(qry, billing_id); 
            
            if (currentStatus.equalsIgnoreCase("paid")) {
                System.out.println("Cannot update. This billing record is already paid.");
                return; 
            }

            sc.nextLine();  
            System.out.print("New Patient Name: ");
            String patient_name = sc.nextLine();
            
            System.out.print("New Admission Date (YYYY-MM-YY): ");
            String admission_date = sc.nextLine();
            
            System.out.print("New Discharge Date (YYYY-MM-DD): ");
            String discharge_date = sc.nextLine();
            
            System.out.print("New Treatment Type: ");
            String treatment_type = sc.nextLine();
            double total_bill_amount = getValidDouble("New Total Bill Amount: ", 0.0); 
            
            String payment_status;
            while (true) {
                System.out.print("New Payment Status (paid/unpaid): ");
                payment_status = sc.nextLine().toLowerCase();
                if (payment_status.equals("paid") || payment_status.equals("unpaid")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'paid' or 'unpaid' only.");
                }
            }

            String qryUpdate = "UPDATE tbl_billing SET patient_name = ?, admission_date = ?, discharge_date = ?, treatment_type = ?, total_bill_amount = ?, payment_status = ? WHERE billing_id = ?";
            conf.updateRecords(qryUpdate, patient_name, admission_date, discharge_date, treatment_type, total_bill_amount, payment_status, billing_id);
            System.out.println("Billing record updated successfully!"); 
        } catch (Exception e) {
            System.err.println("Error updating billing record: " + e.getMessage());
        }
    }

    public void deleteBillingRecord() {
        try {
            int billingId = getValidInteger("Enter Billing ID to delete: ", 1, Integer.MAX_VALUE); 
            String qry = "DELETE FROM tbl_billing WHERE billing_id = ?";
            conf.deleteRecords(qry, billingId);
            System.out.println("Billing record deleted successfully!"); 
        } catch (Exception e) {
            System.err.println("Error deleting billing record: " + e.getMessage());
        }
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

    private double getValidDouble(String prompt, double min) {
        double value = -1;
        while (value < min) {
            System.out.print(prompt);
            try {
                value = sc.nextDouble();
                sc.nextLine();
                if (value < min) {
                    System.out.println("Invalid input. Please enter a number greater than or equal to " + min + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
            }
        }
        return value;
    }
}
