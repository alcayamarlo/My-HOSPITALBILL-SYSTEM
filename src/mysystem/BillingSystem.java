package mysystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class BillingSystem {
    Scanner sc = new Scanner(System.in);
    config conf = new config(); 
    PatientCustomer pc = new PatientCustomer();

    public void billingInfo() {
        String response;

        do {
            System.out.println("|^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^|");
            System.out.println("|                                            |");
            System.out.println("|         BILLINGS MANAGEMENT SYSTEM         |");
            System.out.println("|                                            |");
            System.out.println("|____________________________________________|");
            System.out.println("|  1.            ADD BILLING RECORDS         |");
            System.out.println("|                                            |");
            System.out.println("|  2.              VIEW RECORD               |");
            System.out.println("|                                            |");
            System.out.println("|  3.             UPDATE RECORD              |");
            System.out.println("|                                            |");
            System.out.println("|  4.             DELETE RECORD              |");
            System.out.println("|                                            |");
            System.out.println("|  5.                EXIT                    |");
            System.out.println("|____________________________________________|");

            int action = getUserActionChoice();

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
            }

            response = getYesOrNoInput("Do you want to perform another action? (yes/no): ");
        } while (response.equals("yes"));

        System.out.println("THANK YOU FOR USING MY BILLING MANAGEMENT SYSTEM!!");
    }

    private int getUserActionChoice() {
        int action = -1;
        while (action < 1 || action > 5) {
            try {
                System.out.print("CHOOSE A NUMBER (1-5): ");
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
        return action;
    }

  public void addBillingRecord() {
    try {
        int patient_id = getPatientID(); 

        boolean addAnotherRecord = true;
        while (addAnotherRecord) {
            int existingBillingCount = conf.getSingleValue("SELECT COUNT(*) FROM tbl_billing WHERE patient_id = ?", patient_id);

            if (existingBillingCount > 100)
            {
                System.out.println("YOU CAN ADD NEW CHARGES AND UPDATE A NEW TREATMENT TYPE.");

                int currentPaymentStatusInt = conf.getSingleValue("SELECT payment_status FROM tbl_billing WHERE patient_id = ?", patient_id);
                String currentPaymentStatus = (currentPaymentStatusInt == 1) ? "paid" : "unpaid"; 

                if ("paid".equalsIgnoreCase(currentPaymentStatus)) {
                    System.out.println("Error: This billing record is already paid and cannot be updated.");
                    return;  
                }

                int currentTreatment = conf.getSingleValue("SELECT treatment_type FROM tbl_billing WHERE patient_id = ?", patient_id);
                System.out.println("Current Treatment Type: " + currentTreatment);

                System.out.print("TreatmentType : ");
                String newTreatmentType = sc.nextLine();

                double additionalBillAmount = getPositiveDoubleInput("Additional Bill: ");
                String paymentStatus = getPaidOrUnpaidInput("Payment Status paid/unpai): ");

                String updatedTreatment = currentTreatment + ", " + newTreatmentType;

                String sqlUpdate = "UPDATE tbl_billing SET treatment_type = ?, total_bill_amount = total_bill_amount + ?, payment_status = ? WHERE patient_id = ?";
                conf.updateRecords(sqlUpdate, updatedTreatment, additionalBillAmount, paymentStatus, patient_id);
                System.out.println("NEW CHARGES AND TREATMENT TYPE ADDED SUCCESSFULLY!");
            } else {
                System.out.println("No billing record for this patient. Creating a new billing record.");

                String admissionDate = getDateInput("Admission Date (YYYY-MM-DD): ");
                String dischargeDate = getDateInput("Discharge Date (YYYY-MM-DD): ");
                System.out.print("Treatment Type: ");
                String treatmentType = sc.nextLine();

                double totalBillAmount = getPositiveDoubleInput("Total Bill Amount: ");
                String paymentStatus = getPaidOrUnpaidInput("Payment Status (paid/unpaid): ");

                String sqlInsert = "INSERT INTO tbl_billing (patient_id, admission_date, discharge_date, treatment_type, total_bill_amount, payment_status) VALUES (?, ?, ?, ?, ?, ?)";
                conf.addRecords(sqlInsert, patient_id, admissionDate, dischargeDate, treatmentType, totalBillAmount, paymentStatus);
                System.out.println("Billing record created successfully!");
            }

            addAnotherRecord = getYesOrNoInput("Do you want to add another billing record for this patient? yes/no: ").equals("yes");
        }
    } catch (Exception e) {
        System.err.println("Error adding billing record: " + e.getMessage());
    }
}

    private int getPatientID() {
        int patient_id = -1;
        while (patient_id < 1) {
            try {
                System.out.print("Patient ID: ");
                patient_id = sc.nextInt();
                sc.nextLine();
                if (conf.getSingleValue("SELECT patient_id FROM tbl_patient WHERE patient_id = ?", patient_id) == 0) {
                    System.out.println("Patient ID doesn't exist. Enter again!");
                    patient_id = -1;
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a valid integer for Patient ID: ");
                sc.nextLine();
            }
        }
        return patient_id;
    }

    private String getDateInput(String prompt) {
        String dateInput;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt);
            dateInput = sc.nextLine();
            try {
                LocalDate.parse(dateInput, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please enter a valid date in YYYY-MM-DD format.");
            }
        }
        return dateInput;
    }

    private double getPositiveDoubleInput(String prompt) {
        double input = -1;
        while (input < 0) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                input = sc.nextDouble();
                sc.nextLine();
                if (input < 0) {
                    System.out.println("Amount cannot be negative. Please enter a valid amount.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid decimal number.");
                sc.next();
            }
        }
        return input;
    }

    private String getPaidOrUnpaidInput(String prompt) {
        String status;
        while (true) {
            System.out.print(prompt);
            status = sc.nextLine().toLowerCase();
            if (status.equals("paid") || status.equals("unpaid")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'paid' or 'unpaid' only.");
            }
        }
        return status;
    }

    private String getYesOrNoInput(String prompt) {
        String response;
        while (true) {
            System.out.print(prompt);
            response = sc.nextLine().trim().toLowerCase();
            if (response.equals("yes") || response.equals("no")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no' only.");
            }
        }
        return response;
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

            if ("paid".equalsIgnoreCase(currentStatus)) {
                System.out.println("Error: This billing record is already paid and cannot be updated.");
                return;  
            }

            System.out.print("Enter New Payment Status (paid/unpaid): ");
            String newStatus = sc.nextLine();

            if (!newStatus.equalsIgnoreCase("paid") && !newStatus.equalsIgnoreCase("unpaid")) {
                System.out.println("Invalid payment status. Please enter 'paid' or 'unpaid'.");
                return;
            }

            String updateQuery = "UPDATE tbl_billing SET payment_status = ? WHERE billing_id = ?";
            conf.updateRecords(updateQuery, newStatus, billing_id);

            System.out.println("Billing record updated successfully!");

        } catch (Exception e) {
            System.err.println("Error updating billing record: " + e.getMessage());
        }
    }

    public void deleteBillingRecord() {
      try {
          int billing_id = -1;
          boolean validBillingId = false;

          while (!validBillingId) {
              System.out.print("Enter Billing ID to Delete: ");
              if (sc.hasNextInt()) {
                  billing_id = sc.nextInt();
                  sc.nextLine();  
                  String checkQuery = "SELECT COUNT(*) FROM tbl_billing WHERE billing_id = ?";
                  int recordCount = conf.getSingleValue(checkQuery, billing_id);

                  if (recordCount == 0) {
                      System.out.println("Error: No billing record found with the provided Billing ID.");
                  } else {
                      validBillingId = true; 
                  }
              } else {
                  System.out.println("Invalid input. Please enter a valid integer for Billing ID.");
                  sc.nextLine();  
              }
          }

          if (validBillingId) {
              String deleteQuery = "DELETE FROM tbl_billing WHERE billing_id = ?";
              conf.updateRecords(deleteQuery, billing_id);

              String checkQuery = "SELECT COUNT(*) FROM tbl_billing WHERE billing_id = ?";
              int recordCount = conf.getSingleValue(checkQuery, billing_id);
              if (recordCount == 0) {
                  System.out.println("Billing record deleted successfully!");
              } else {
                  System.out.println("Error: Failed to delete the billing record.");
              }
          }
      } catch (Exception e) {
          System.err.println("Error deleting billing record: " + e.getMessage());
    
      }
  }
    
    
 }