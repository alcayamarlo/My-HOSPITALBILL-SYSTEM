package mysystem;

import java.util.Scanner;

public class Mysystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        
        
        do {
            System.out.println("====================================================|");
            System.out.println("                                                    |");
            System.out.println("   WELCOME TO PATIENT & HOSPITAL BILL SYSTEM        |");
            System.out.println("                                                    |");
            System.out.println("====================================================|");
            System.out.println("1.          PATIENT INFORMATION                     |");
            System.out.println("2.          BILLING INFORMATION                     |");
            System.out.println("3.               REPORTS                            |");  
            System.out.println("4.                 EXIT                             |");
            System.out.println("====================================================|");

            
            
            int num = -1; 
            while (true) {
                System.out.print("ENTER (1-4) ONLY ! : ");
                try {
                    num = Integer.parseInt(input.nextLine().trim()); 
                    if (num >= 1 && num <= 4) { 
                        break;
                    } else {
                        System.out.println("\tWarning: Please enter a number between 1 and 4.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\tWarning: Please enter a valid integer.");
                }
            }

            switch (num) {
                case 1:
                    PatientCustomer pc = new PatientCustomer();
                    pc.patientInfo();
                    break;
                case 2:
                    BillingSystem bs = new BillingSystem();
                    bs.billingInfo();
                    break;
                case 3:
                    Report r = new Report();
                    r.reportMenu();
                    break;
                case 4:
                    System.out.println("Returning to main menu. Press Enter to continue.");
                    input.nextLine(); 
                    break;
                default:
                    System.out.println("\tError: Unexpected case encountered.");
                    break;
            }
        } while (true);

    }
}
