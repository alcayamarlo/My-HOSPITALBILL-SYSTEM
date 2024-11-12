
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
        System.out.println("|============================================|");
        System.out.println("|                                            |");
        System.out.println("|           BILLINGS MANAGEMENT SYSTEM       |");
        System.out.println("|                                            |");
        System.out.println("|============================================|");
        System.out.println("|1.         ADD BILLING RECORDS              |");
        System.out.println("|2.             VIEW RECORD                  |");
        System.out.println("|3.            UPDATE RECORD                 |");
        System.out.println("|4.            DELETE RECORD                 |");
        System.out.println("|5.               EXIT                       |");
        System.out.println("|============================================|");

        System.out.print("CHOOSE A NUMBER (1-5) :");
        int action = getValidAction(1, 5); 
        
        switch (action) {
            case 1:
                pc.viewPatients();
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
                System.out.print("Invalid input. Please enter 'yes' or 'no' only.");
            }
        }
        
    } while (response.equals("yes"));

    System.out.println("THANK YOU FOR USING MY BILLING MANAGEMENT SYSTEM!!");
}


public void addBillingRecord() {
        try {
            int patient_id = getValidInteger("Patient ID :", 1, Integer.MAX_VALUE);
            while (conf.getSingleValue("SELECT patient_id FROM tbl_patient WHERE patient_id = ? ", patient_id ) == 0) {
                System.out.println("Patient ID doesn't exist!!:");
                System.out.print("Enter Patient ID Again :");
                patient_id = sc.nextInt();
            }
            sc.nextLine();
            System.out.print("Admission Date (YYYY-MM-DD): ");
            String admissionDate = sc.nextLine();
            
            System.out.print("Discharge Date (YYYY-MM-DD): ");
            String dischargeDate = sc.nextLine();
            System.out.print("Treatment Type: ");
            String treatmentType = sc.nextLine();

            double totalBillAmount = getValidDouble("Total Bill Amount: ");

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
            conf.addRecords(sql, patient_id, admissionDate, dischargeDate, treatmentType, totalBillAmount, paymentStatus);
            System.out.println("Billing record added successfully!");

        } catch (Exception e) {
            System.err.println("Error adding billing record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double getValidDouble(String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                value = sc.nextDouble();
                sc.nextLine(); 
                if (value >= 0) {
                    return value;
                } else {
                    System.out.println("Amount cannot be negative. Please enter a valid amount.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                sc.next();
            }
        }
    }
    
    public void viewBillingRecords() {
    try {
        String qry = "SELECT tbl_billing.billing_id, "
                   + "(tbl_patient.First_Name || ' ' || tbl_patient.Last_Name) AS patient_name, "
                   + "tbl_billing.admission_date, tbl_billing.discharge_date, tbl_billing.treatment_type, "
                   + "tbl_billing.total_bill_amount, tbl_billing.payment_status "
                   + "FROM tbl_billing "
                   + "LEFT JOIN tbl_patient ON tbl_patient.patient_id = tbl_billing.patient_id";
        
        String[] hdrs = {"BILLING ID", "PATIENT NAME", "ADMISSION DATE", "DISCHARGE DATE", "TREATMENT TYPE", "TOTAL BILLS", "PAYMENTS PAID/UNPAID"};
        String[] clmn = {"billing_id", "patient_name", "admission_date", "discharge_date", "treatment_type", "total_bill_amount", "payment_status"};
        conf.viewRecords(qry, hdrs, clmn);
    } catch (Exception e) {
        System.err.println("Error viewing billing records: " + e.getMessage());
    }
}


public void updateBillingRecord() {
    try {
        
        int billing_id;
        String currentStatus;
        while (true) {
            billing_id = getValidInteger("Enter Billing ID to Update: ", 1, Integer.MAX_VALUE);
            String qry = "SELECT payment_status FROM tbl_billing WHERE billing_id = ?";
            currentStatus = conf.getPaymentStatus(qry, billing_id);
            if (currentStatus == null) {
                System.out.print("Error!!! Billing ID not found. Please try again!!!");
            } else {
                break;
            }
        }

        if (currentStatus.equalsIgnoreCase("paid")) {
            System.out.println("Error to update. This bill is already paid.");
            return;
        }
        System.out.print("New Admission Date (YYYY-MM-DD): ");
        String admission_date = sc.nextLine();
        System.out.print("New Discharge Date (YYYY-MM-DD): ");
        String discharge_date = sc.nextLine();
        System.out.print("New Treatment Type: ");
        String treatment_type = sc.nextLine();
        double total_bill_amount = getValidDouble("New Total Bill Amount: ");
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
        String qryUpdate = "UPDATE tbl_billing SET admission_date = ?, discharge_date = ?, treatment_type = ?, total_bill_amount = ?, payment_status = ? WHERE billing_id = ?";
        conf.updateRecords(qryUpdate, admission_date, discharge_date, treatment_type, total_bill_amount, payment_status, billing_id);

        System.out.println("Billing record updated successfully!");

    } catch (Exception e) {
        System.err.println("Error updating billing record: " + e.getMessage());
    }
}



public void deleteBillingRecord() {
    try {
        int billingId = getValidInteger("Enter Billing ID to delete: ", 1, Integer.MAX_VALUE);
        String checkQuery = "SELECT COUNT(*) FROM tbl_billing WHERE billing_id = ?";
        while (!conf.checkIfExists(checkQuery, billingId)) {
            System.out.println("Warning: Billing ID not found. Please enter a valid Billing ID.");
            billingId = getValidInteger("Enter Billing ID to delete: ", 1, Integer.MAX_VALUE);
        }
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
                    System.out.print("Invalid input. Please enter a number greater than or equal to " + min + ":");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a valid number :");
                sc.nextLine();
            }
        }
        return value;
    }
    
}
