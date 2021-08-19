package database;

import models.*;

import javax.sound.sampled.Port;
import java.util.List;

public interface IDbHelper {

    Student validateStudent(int studentId,String passWord);

    Student validateStudent(int studentId);

    Admin validateAdmin(String userName, String password);

    int addNewStudent(Student student);

    List<Student> studentsWithoutCard();

    int issueStudentWithCard(int stdId);

    int updateStudentCardStatus(int stId);

    int updateAccountBalance(Student student, double amount);

    List<Product> getAvailableProducts();

    double getAvailableCardBalance(int studentId);

    Card getPurchaseCard(int studentId);
    int updateSale(Sale sale);

    List<Product> getAllSoldItems();

    List<Sale> getSaleProduct(int id);
}
