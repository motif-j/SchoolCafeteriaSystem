package algo;

import database.IDbHelper;
import models.Product;
import models.Sale;
import models.Student;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AdminServices {
    IDbHelper dbHelper;
    Scanner scanner = new Scanner(System.in);

    public AdminServices(IDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void performAdminServices(ServiceType serviceType) {
        switch (serviceType) {
            case ENROLL_STUDENT:
                admitNewStudent();
                break;
            case ISSUE_CARD:
                issueCard();
                break;
            case TOP_UP:
                topUpStudentAccount();
                break;
            case GENERATE_REPORT:
                generateSalesReport();
                break;
        }

    }

    private void topUpStudentAccount() {
        String sentinel = "y";
        System.out.println("Welcome to top up service");
        while (sentinel.equals("y")) {
            System.out.println("Enter the Student Id");
            try {
                int verify = 0;
                int stdId = scanner.nextInt();
                double amount = 0.0;

                Student student = dbHelper.validateStudent(stdId);
                if (student.isValidated()) {
                    double availableAmount = dbHelper.getAvailableCardBalance(stdId);
                    System.out.println("Enter the amount in Kshs:");
                    amount = scanner.nextDouble();
                    System.out.println("Confirm depositing Kshs " + amount + " to Student " + student.getFirstName() + " " + student.getLastName() + " Id No " + student.getStudentId() + " By pressing 1");
                    verify = scanner.nextInt();
                    if (verify == 1) {
                        amount = amount + availableAmount;
                        int result = dbHelper.updateAccountBalance(student, amount);
                        if (result > 0) {
                            System.out.println("Account updated");
                        } else {
                            System.out.println("Account not topped up");
                        }
                    } else {
                        System.out.println("Transaction cancelled");
                    }

                } else {
                    System.out.println("Invalid student");
                }


            } catch (Exception e) {
                System.out.println("An error occurred please retry");
            }

            sentinel = getSentinel();
        }
    }

    private String getSentinel() {
        System.out.println("Press Y to continue or any character to exit");
        return scanner.next().toLowerCase();
    }

    private void issueCard() {
        //get all students without a card
        String sentinel = "y";
        System.out.println("Welcome to card issuance center");
        while (sentinel.equals("y")) {
            System.out.println("Students without cards");
            List<Student> students = dbHelper.studentsWithoutCard();
            for (Student student : students) {
                System.out.println(student.getStudentId() + ". " + student.getFirstName() + " " + student.getLastName());
            }
            System.out.println("Enter the Student Id number to issue card");
            try {
                int idNo = scanner.nextInt();
                int cardStat = dbHelper.issueStudentWithCard(idNo);
                if (cardStat > 0) {
                    int updateStat = dbHelper.updateStudentCardStatus(idNo);
                    if (updateStat > 0) {
                        System.out.println("Student id " + idNo + " card issuance was successful");
                    } else {
                        System.out.println("Student id " + idNo + " card issuance failed");
                    }
                } else {
                    System.out.println("Failed to issue card to student id " + idNo);
                }
            } catch (Exception e) {
                System.out.println("Invalid Id");
                break;
            }

            System.out.println("Press  y to continue or any key to exit");
            sentinel = scanner.next().toLowerCase();
        }


    }

    private void admitNewStudent() {

        String sentinel = "y";
        System.out.println("Welcome to admission center");

        while (sentinel.equals("y")) {
            String firstName, lastName;
            System.out.println("Enter Student first name");
            firstName = scanner.next();
            System.out.println("Enter Student last name");

            lastName = scanner.next();
            if (!firstName.isEmpty() || !lastName.isEmpty()) {

                Student student = new Student(firstName, lastName);
                student.setPassWord(generateRandomPassword());
                int result = dbHelper.addNewStudent(student);
                if (result > 0) {
                    System.out.println("successfully enrolled " + student.getFirstName());
                    System.out.println("PASSWORD IS "+student.getPassWord());
                } else {
                    System.out.println("Failed to enroll the student");
                }
            } else {
                System.out.println("Invalid values");
            }
            System.out.println("Press Y to continue enrolling students or any letter to exit");
            sentinel = scanner.next().toLowerCase();
        }

    }


    private void generateSalesReport() {
        double totalSales=0.0;
        System.out.println("Welcome to sales generation report");
        List<Product> productList=dbHelper.getAvailableProducts();
        System.out.println("Product        "+"Quantity "+"Amount");
        for (Product product:productList){
            int quantity=0;
            double amount=0.0;
            for (Sale sale:dbHelper.getSaleProduct(product.getProductId())){
                quantity=quantity+ sale.getQuantity();
                amount=amount+ sale.getTotalCost();

            }
            totalSales=totalSales+amount;


            System.out.println(product.getProductName()+"       "+quantity+" ---"+amount);
        }
        System.out.println("Total sales as at "+new SimpleDateFormat().format(new Date().getTime())+" is "+totalSales);
    }

    private String generateRandomPassword()
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

}
