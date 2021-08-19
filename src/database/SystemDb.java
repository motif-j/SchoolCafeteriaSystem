package database;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemDb implements IDbHelper {

    private static SystemDb systemDb;

    private SystemDb() {
        getConnection();
    }

    private Connection getConnection() {
        Connection cnn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            cnn = DriverManager.getConnection("jdbc:sqlite:Cafeteria.db");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return cnn;
    }

    public static SystemDb getInstance() {
        if (systemDb == null) {
            systemDb = new SystemDb();
        }
        return systemDb;
    }


    @Override
    public Student validateStudent(int studentId,String passWord) {
        Connection connection = getConnection();
        Student student = new Student();
        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("select * from students where std_id=?1 and password=?2");
                pst.setInt(1, studentId);
                pst.setString(2,passWord);
                ResultSet set = pst.executeQuery();
                while (set.next()) {
                    student.setStudentId(set.getInt(1));
                    student.setFirstName(set.getString(2));
                    student.setLastName(set.getString(3));
                    student.setValidated(true);
                }
                pst.close();
                set.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                student.setValidated(false);
            }
        }
        return student;
    }

    @Override
    public Student validateStudent(int studentId) {
        Connection connection = getConnection();
        Student student = new Student();
        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("select * from students where std_id=?1");
                pst.setInt(1, studentId);
                ResultSet set = pst.executeQuery();
                while (set.next()) {
                    student.setStudentId(set.getInt(1));
                    student.setFirstName(set.getString(2));
                    student.setLastName(set.getString(3));
                    student.setValidated(true);
                }
                pst.close();
                set.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                student.setValidated(false);
            }
        }
        return student;
    }

    @Override
    public Admin validateAdmin(String userName, String password) {
        Connection connection = getConnection();
        Admin admin = new Admin();
        if (connection != null) {

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select user_name from admin where user_name=?1 and password=?2");

                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);
                ResultSet set = preparedStatement.executeQuery();

                while (set.next()) {
                    admin.setUserName(set.getString(1));
                    admin.setValidated(true);
                }
                connection.close();
                set.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                admin.setUserName("Invalid user");
                admin.setValidated(false);
            }

        }

        return admin;
    }

    @Override
    public int addNewStudent(Student student) {
        int result = 0;
        Connection connection = getConnection();
        if (connection != null) {
            PreparedStatement pst = null;
            try {
                pst = connection.prepareStatement("insert  into students (first_name,last_name,password ) values (?1,?2,?3)");
                pst.setString(1, student.getFirstName());
                pst.setString(2, student.getLastName());
                pst.setString(3,student.getPassWord());

                result = pst.executeUpdate();
                connection.close();
                pst.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return result;
            }

        }
        return result;
    }

    @Override
    public List<Student> studentsWithoutCard() {
        List<Student> students = new ArrayList<>();
        Connection connection = getConnection();

        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("select * from students where card_status=0 ");
                ResultSet set = pst.executeQuery();

                while (set.next()) {
                    Student student = new Student(set.getString(2), set.getString(3));
                    student.setCardStatus(4);
                    student.setStudentId(set.getInt(1));
                    students.add(student);
                }
                pst.close();
                set.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
        return students;
    }

    @Override
    public int issueStudentWithCard(int stdId) {
        Connection connection = getConnection();
        int result = 0;
        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("insert into cards (std_id) values (?1)");
                pst.setInt(1, stdId);
                result = pst.executeUpdate();
                connection.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public int updateStudentCardStatus(int stId) {
        Connection connection = getConnection();
        int result = 0;
        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("update  students set card_status=1 where std_id=?1 ");
                pst.setInt(1, stId);
                result = pst.executeUpdate();
                connection.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    @Override
    public int updateAccountBalance(Student student, double amount) {
        Connection connection = getConnection();
        int result = 0;
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("update cards set  balance=?1  where std_id=?2");
                preparedStatement.setDouble(1, amount);
                preparedStatement.setInt(2, student.getStudentId());
                result = preparedStatement.executeUpdate();
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<Product> getAvailableProducts() {
        List<Product> products = new ArrayList<>();

        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("select* from products");
                ResultSet set = pst.executeQuery();
                while (set.next()) {
                    Product product = new Product(set.getString(2), set.getInt(1), set.getDouble(3));
                    products.add(product);
                }
                set.close();
                pst.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return products;
    }

    @Override
    public double getAvailableCardBalance(int studentId) {
        Connection connection = getConnection();
        double amount = 0.0;
        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("select balance from cards where std_id=?1");
                pst.setInt(1, studentId);
                ResultSet set = pst.executeQuery();
                while (set.next()) {
                    amount = set.getDouble(1);
                }

                connection.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return amount;
    }

    @Override
    public Card getPurchaseCard(int studentId) {
        Connection connection = getConnection();
        Card card = new Card();

        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("select card_no from cards where std_id=?1 ");
                pst.setInt(1, studentId);

                ResultSet set = pst.executeQuery();
                while (set.next()) {

                    card.setCardNo(set.getInt(1));

                }
                set.close();
                pst.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return card;
    }

    @Override
    public int updateSale(Sale sale) {
        int result = 0;
        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement pst = connection.prepareStatement("insert into sales (item_id,quantity,card_no,total_cost) values (?1,?2,?3,?4) ");
                pst.setInt(1, sale.getItemId());
                pst.setInt(2, sale.getQuantity());
                pst.setInt(3, sale.getCardNo());
                pst.setDouble(4, sale.getTotalCost());

                result = pst.executeUpdate();
                pst.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public List<Product> getAllSoldItems() {

        return null;
    }

    @Override
    public List<Sale> getSaleProduct(int itemId) {
        Connection connection=getConnection();
        List<Sale> sales=new ArrayList<>();
        if (connection!=null){
            try {
                PreparedStatement pst=connection.prepareStatement("select * from sales where item_id=?1");
                pst.setInt(1,itemId);

                ResultSet set= pst.executeQuery();
                while (set.next()){
                    Sale sale=new Sale();
                    sale.setTotalCost(set.getDouble(5));
                    sale.setQuantity(set.getInt(3));
                    sales.add(sale);
                }
                pst.close();
                set.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sales;
    }
}
