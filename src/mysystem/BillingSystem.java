
package mysystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            System.out.println("|         BILLINGS MANAGEMENT SYSTEM         |");
            System.out.println("|                                            |");
            System.out.println("|============================================|");
            System.out.println("|                                            |");
            System.out.println("|  1.            ADD BILLING RECORDS         |");
            System.out.println("|                                            |");
            System.out.println("|  2.              VIEW RECORD               |");
            System.out.println("|                                            |");
            System.out.println("|  3.             UPDATE RECORD              |");
            System.out.println("|                                            |");
            System.out.println("|  4.             DELETE RECORD              |");
            System.out.println("|                                            |");
            System.out.println("|  5.                EXIT                    |");
            System.out.println("|                                            |");
            System.out.println("|============================================|");


            System.out.print("CHOOSE A NUMBER (1-5): ");
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
                    pc.viewPatients();
                    addBillingRecord();  
                    break;
                case 2:
                    viewBillingRecord(); 
                    break;
                case 3:
                    viewBillingRecord();
                    updateBillingRecord(); 
                    viewBillingRecord();
                    break;
                case 4:
                    viewBillingRecord();
                    deleteBillingRecord();
                    viewBillingRecord();
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

        System.out.println("THANK YOU FOR USING MY BILLING MANAGEMENT SYSTEM!!");
    }

   public void addBillingRecord() {
        try {
            boolean addAnotherRecord = true;
            while (addAnotherRecord) {
                int patient_id = -1;
                while (patient_id < 1) {
                    try {
                        System.out.print("Patient ID: ");
                        patient_id = sc.nextInt();
                        sc.nextLine();
                        if (conf.getSingleValue("SELECT patient_id FROM tbl_patient WHERE patient_id = ?", patient_id) == 0) {
                            System.out.println("Patient ID doesn't exist. Enter again!");
                            patient_id = -1;
                        } else {
                            int existingBillingCount = conf.getSingleValue("SELECT COUNT(*) FROM tbl_billing WHERE patient_id = ?", patient_id);
                            if (existingBillingCount > 0) {
                                System.out.println("This patient already has a billing record.");
                                return; 
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.print("Invalid input. Please enter a valid integer for Patient ID: ");
                        sc.nextLine(); 
                    }
                }

                String admissionDate;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                while (true) {
                    System.out.print("Admission Date (YYYY-MM-DD): ");
                    admissionDate = sc.nextLine();
                    try {
                        LocalDate.parse(admissionDate, formatter);
                        break; 
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date. Please enter a valid date (YYYY-MM-DD).");
                    }
                }

                String dischargeDate;
                while (true) {
                    System.out.print("Discharge Date (YYYY-MM-DD): ");
                    dischargeDate = sc.nextLine();
                    try {
                        LocalDate.parse(dischargeDate, formatter);
                        break; 
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date. Please enter a valid discharge date (YYYY-MM-DD).");
                    }
                }

                System.out.print("Treatment Type: ");
                String treatmentType = sc.nextLine();

                double totalBillAmount = -1;
                while (totalBillAmount < 0) {
                    System.out.print("Total Bill Amount: ");
                    if (sc.hasNextDouble()) {
                        totalBillAmount = sc.nextDouble();
                        sc.nextLine();
                        if (totalBillAmount < 0) {
                            System.out.println("Amount cannot be negative. Please enter a valid amount.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a valid decimal number.");
                        sc.next(); 
                    }
                }

                String paymentStatus;
                while (true) {
                    System.out.print("Payment Status (paid/unpaid): ");
                    paymentStatus = sc.nextLine().toLowerCase();
                    if (paymentStatus.equals("paid") || paymentStatus.equals("unpaid")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter paid or unpaid only.");
                    }
                }

                String sql = "INSERT INTO tbl_billing (patient_id, admission_date, discharge_date, treatment_type, total_bill_amount, payment_status) VALUES (?, ?, ?, ?, ?, ?)";
                conf.addRecords(sql, patient_id, admissionDate, dischargeDate, treatmentType, totalBillAmount, paymentStatus);
                System.out.println("Billing record added successfully!");

                while (true) {
                    System.out.print("Do you want to perform another action? (yes/no): ");
                    String response = sc.nextLine().trim().toLowerCase();
                    if (response.equals("yes")) {
                        addAnotherRecord = true;
                        break;  
                    } else if (response.equals("no")) {
                        addAnotherRecord = false;
                        System.out.println("RETURNING TO MAIN MENU");
                        return; 
                    } else {
                        System.out.println("Invalid input. Please enter yes or no only.");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error adding billing record: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void viewBillingRecord() {
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
            System.out.print("Enter Billing ID to Update: ");
            int billing_id = -1;
            String currentStatus = null;
            while (billing_id < 1 || currentStatus == null) {
                try {
                    billing_id = sc.nextInt();
                    sc.nextLine();
                    String qry = "SELECT payment_status FROM tbl_billing WHERE billing_id = ?";
                    currentStatus = conf.getPaymentStatus(qry, billing_id);
                    if (currentStatus == null) {
                        System.out.print("Error! Billing ID not found. Please try again: ");
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a valid integer for Billing ID: ");
                    sc.nextLine();
                }
            }

            if (currentStatus.equalsIgnoreCase("paid")) {
                System.out.println("Error: This bill is already paid and cannot be updated.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String admission_date;
            while (true) {
                System.out.print("Enter New Admission Date (YYYY-MM-DD): ");
                admission_date = sc.nextLine();
                try {
                    LocalDate.parse(admission_date, formatter);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date. Please enter a valid date in YYYY-MM-DD format.");
                }
            }

            String discharge_date;
            while (true) {
                System.out.print("Enter New Discharge Date (YYYY-MM-DD): ");
                discharge_date = sc.nextLine();
                try {
                    LocalDate.parse(discharge_date, formatter);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date. Please enter a valid date in YYYY-MM-DD format.");
                }
            }

            System.out.print("New Treatment Type: ");
            String treatment_type = sc.nextLine();

            System.out.print("New Total Bill Amount: ");
            double total_bill_amount = -1;
            while (total_bill_amount < 0) {
                if (sc.hasNextDouble()) {
                    total_bill_amount = sc.nextDouble();
                    sc.nextLine();
                    if (total_bill_amount < 0) {
                        System.out.println("Amount cannot be negative. Please enter a valid amount.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid decimal number.");
                    sc.next();
                }
            }

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

            String sql = "UPDATE tbl_billing SET admission_date = ?, discharge_date = ?, treatment_type = ?, total_bill_amount = ?, payment_status = ? WHERE billing_id = ?";
            conf.updateRecords(sql, admission_date, discharge_date, treatment_type, total_bill_amount, payment_status, billing_id);
            System.out.println("Billing record updated successfully!");

        } catch (Exception e) {
            System.err.println("Error updating billing record: " + e.getMessage());
        }
    }

    public void deleteBillingRecord() {
        try {
            System.out.print("Enter Billing ID to Delete: ");
            int billing_id = -1;
            while (billing_id < 1) {
                try {
                    billing_id = sc.nextInt();
                    sc.nextLine();
                    if (conf.getSingleValue("SELECT billing_id FROM tbl_billing WHERE billing_id = ?", billing_id) == 0) {
                        System.out.print("Billing ID doesn't exist. Please try again: ");
                        billing_id = -1;
                    }
                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a valid integer for Billing ID: ");
                    sc.nextLine();
                }
            }

            String sql = "DELETE FROM tbl_billing WHERE billing_id = ?";
            conf.deleteRecords(sql, billing_id);
            System.out.println("Billing record deleted successfully!");

        } catch (Exception e) {
            System.err.println("Error deleting billing record: " + e.getMessage());
        }
    }
}
