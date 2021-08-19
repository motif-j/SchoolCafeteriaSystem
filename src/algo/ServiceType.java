package algo;

public enum ServiceType {
    ENROLL_STUDENT(1), //admin
    ISSUE_CARD(2), //admin
    TOP_UP(3), //admin
    CHECK_BALANCE(4), //student
    BUY_ITEM(5), //student
    GENERATE_REPORT(6), //admin
    UNKNOWN(0);

    private final int getServiceNo;

    ServiceType(int getServiceNo) {
        this.getServiceNo = getServiceNo;
    }

    public int getGetServiceNo() {
        return getServiceNo;
    }
}
