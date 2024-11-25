package mysystem;

import java.util.Scanner;

public class Mysystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = true;
        do {
            
            System.out.println("|^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^|");
            System.out.println("|                                                    |");
            System.out.println("|      WELCOME TO PATIENT AND HOSPITAL BILL SYSTEM   |");
            System.out.println("|                                                    |");
            System.out.println("|____________________________________________________|");
            System.out.println("|                                                    |");
            System.out.println("|  1.          PATIENTS INFORMATION                  |");
            System.out.println("|                                                    |");
            System.out.println("|  2.          BILLINGS INFORMATION                  |");
            System.out.println("|                                                    |");
            System.out.println("|  3.               REPORTS                          |");
            System.out.println("|                                                    |");
            System.out.println("|  4.                 EXIT                           |");
            System.out.println("|____________________________________________________|");

            int num = -1; 
            while (true) {
                System.out.print("\tCHOOSE A NUMBER (1-4) : ");
                try {
                    num = Integer.parseInt(input.nextLine().trim()); 
                    if (num >= 1 && num <= 4) { 
                        break;
                    } else {
                        System.out.println("WARNING:PLEASE ENTER A NUMBER BETWEEN 1 AND 4 !!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("WARNING!! ENTER A VALID INTEGER.");
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
                    exit = false;
                    System.out.print("Exiting The Program, Thank You For Using!!!!");
                    break;
                default:
                    System.out.println("Error.");
                    break;
            }
        } while (exit);

    }
}
