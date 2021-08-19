package models;

public class Service {
    private int serviceCode;
    private String serviceName;

    public Service() {
    }

    public Service(int serviceCode, String serviceName) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
    }

    public int getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(int serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
