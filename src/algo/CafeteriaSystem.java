package algo;

import database.SystemDb;
import database.IDbHelper;
import models.Admin;
import models.Service;
import models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CafeteriaSystem {
    private final IDbHelper dbHelper;
    private UserType  userType=UserType.USER_UNKNOWN;
    private Student currentStudent=new Student();
    private Admin currentAdmin=new Admin();
    private AdminServices adminServices;
    private StudentServices studentServices;

    public CafeteriaSystem(){
        dbHelper= SystemDb.getInstance();
        adminServices=new AdminServices(dbHelper);
        studentServices=new StudentServices();


    }

    public void initializeSystem() {
        System.out.println("Welcome to Solfa Cafeteria, Press S for student login and A for staff login: ");
        Scanner scanner=new Scanner(System.in);
        String inChar=scanner.next();
        if(inChar.toLowerCase().equals("a")){
            //request login credentials
            System.out.println("Enter your user name");
            String userName=scanner.next();
            System.out.println("Enter your password");
            String passWord=scanner.next();
            Admin adN=dbHelper.validateAdmin(userName,passWord);

            if (adN.isValidated()){
                currentAdmin=adN;
                userType=UserType.USER_ADMIN;
                System.out.println("Welcome admin "+currentAdmin.getUserName());
                showServices();
            }else {
                System.out.println("Invalid staff data");
            }

        }else if (inChar.toLowerCase().equals("s")){
            //request student login data
            System.out.println("Enter your student id no.");
            try {
                int stdId=scanner.nextInt();
                System.out.println("Enter your password");
                String passWord=scanner.next();
                Student std=dbHelper.validateStudent(stdId,passWord);
                if (std.isValidated()){
                    currentStudent=std;
                    userType=UserType.USER_STUDENT;

                    studentServices.setCurrentStudent(currentStudent);
                    System.out.println("Welcome "+currentStudent.getFirstName()+" "+currentStudent.getLastName());
                    showServices();
                }else {
                    System.out.println("The student does not exists");
                }
            }catch (Exception e){
                System.out.println("Invalid id");
            }

        }else {
            System.out.println("invalid data");


        }

    }

    private void showServices(){
        int sentinel=1;
        Scanner scanner=new Scanner(System.in);
        List<Service> services=getServices(userType);
        while (sentinel==1){

            if (userType==UserType.USER_ADMIN){
                System.out.println("Select the service you would like to offer");

                for(Service service:services){
                    System.out.println(service.getServiceCode()+".   "+service.getServiceName());
                }
                System.out.println("Enter the number of the Service");
                try {
                    int serviceCode=scanner.nextInt();
                    ServiceType serviceType=processServiceCode(serviceCode);
                    adminServices.performAdminServices(serviceType);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }else {
                System.out.println("What would you like to do");
                for(Service service:services){
                    System.out.println(service.getServiceCode()+".   "+service.getServiceName());
                }
                System.out.println("Enter the number :");
                try {
                    int serviceCode=scanner.nextInt();
                    ServiceType serviceType=processServiceCode(serviceCode);
                    studentServices.performStudentServices(serviceType);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            System.out.println("Press 1 to continue to services or any number to exit the application");
            try {
                sentinel= scanner.nextInt();
            }catch (Exception e){
                sentinel=0;
            }
        }


    }

    private List<Service> getServices(UserType userType){
        List<Service> services=new ArrayList<>();

        if (userType==UserType.USER_ADMIN){
            services.add(new Service(ServiceType.ENROLL_STUDENT.getGetServiceNo(), "Enroll Student"));
            services.add(new Service(ServiceType.ISSUE_CARD.getGetServiceNo(),"Issue card"));
            services.add(new Service(ServiceType.TOP_UP.getGetServiceNo(), "Top up card"));
            services.add(new Service(ServiceType.GENERATE_REPORT.getGetServiceNo(), "Generate sales Report"));

        }else if (userType==UserType.USER_STUDENT){
            services.add(new Service(ServiceType.CHECK_BALANCE.getGetServiceNo(), "Check Balance"));
            services.add(new Service(ServiceType.BUY_ITEM.getGetServiceNo(), "Purchase Item"));
        }else {
            services.add(new Service(0,"Access denied"));
        }

        return services;
    }

    private ServiceType processServiceCode(int code){
        ServiceType serviceType=ServiceType.UNKNOWN;
        switch (code){
            case 1:
                serviceType=ServiceType.ENROLL_STUDENT;
                break;
            case 2:
                serviceType=ServiceType.ISSUE_CARD;
                break;
            case 3:
                serviceType=ServiceType.TOP_UP;
                break;
            case 4:
                serviceType=ServiceType.CHECK_BALANCE;
                break;
            case 5:
                serviceType=ServiceType.BUY_ITEM;
                break;
            case 6:
                serviceType=ServiceType.GENERATE_REPORT;
                break;
            default:
                break;
        }
        return serviceType;
    }
}
