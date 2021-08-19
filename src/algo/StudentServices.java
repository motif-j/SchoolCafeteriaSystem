package algo;

import database.IDbHelper;
import database.SystemDb;
import models.Card;
import models.Product;
import models.Sale;
import models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentServices {

    IDbHelper dbHelper;
    Student currentStudent;
    Scanner scanner = new Scanner(System.in);

    public StudentServices() {
        dbHelper = SystemDb.getInstance();
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public void performStudentServices(ServiceType serviceType) {
        switch (serviceType) {
            case CHECK_BALANCE:
                checkBalance();
                break;
            case BUY_ITEM:
                buyProducts();
                break;

        }
    }

    private void buyProducts() {
        double totalCost=0.0;
        Card stCard = dbHelper.getPurchaseCard(currentStudent.getStudentId());
        System.out.println("Welcome to Shopping Service\nThe following is our menu");
        List<Product> products = dbHelper.getAvailableProducts();
        List<Product> selectedProducts=new ArrayList<>();

        for (Product product : products) {
            System.out.println(product.getProductId() + " " + product.getProductName() + " ---- Kshs. " + product.getAmount());
        }

        try {
            String sentinel="y";
            while (sentinel.equals("y")){
                System.out.println("Enter the code  of the item: ");
                Product selectedProduct;
                int productId = 0;
                int quantity = 1;

                productId = scanner.nextInt();
                selectedProduct = products.get(productId-1);

                System.out.println("Enter quantity::");
                quantity = scanner.nextInt();
                selectedProduct.setQuantity(quantity);
                selectedProducts.add(selectedProduct);
                System.out.println("Press Y to add another item or any key to exit");
                sentinel= scanner.next().toLowerCase();
            }
            //display total proucts
            for (Product p:selectedProducts){
                double amount=p.getAmount()*p.getQuantity();
                totalCost=totalCost+amount;
                System.out.println(p.getProductName()+"  "+p.getQuantity()+" == "+amount);
            }


            if (stCard.getCardNo() != 0) {
                //check if the available balance can purchase
                System.out.println("Confirm the purchase at a total cost of Kshs"+totalCost+" by pressing 1");

                int ver=scanner.nextInt();
                if (ver==1){

                    double stdBalance = dbHelper.getAvailableCardBalance(currentStudent.getStudentId());
                    if (stdBalance >= totalCost) {
                        //process payment
                        stdBalance = stdBalance - totalCost;
                        int res = dbHelper.updateAccountBalance(currentStudent, stdBalance);
                        if (res > 0) {
                            //update cafeteria sale
                            // s
                            for (Product p:selectedProducts){
                                Sale sale = new Sale();
                                sale.setCardNo(stCard.getCardNo());
                                sale.setItemId(p.getProductId());
                                sale.setQuantity(p.getQuantity());
                                sale.setTotalCost(p.getAmount()*p.getQuantity());
                                int saleRes = dbHelper.updateSale(sale);

                            }

                            System.out.println("Thank you for shopping with us");


                        } else {
                            System.out.println("An error occurred while processing your request");
                        }
                    } else {
                        System.out.println("Your card has insufficient balance. Kindly top up");
                    }
                }else {
                    System.out.println("Transaction cancelled");
                }
            } else {
                System.out.println("You do not have a card, Kindly apply for one");
            }

        } catch (Exception e) {
            System.out.println("Sorry we dont have that yet");
        }

    }

    private void checkBalance() {
        System.out.println("Balance Service");
        double balance = dbHelper.getAvailableCardBalance(currentStudent.getStudentId());
        System.out.println("Your balance is " + balance);

    }

}
