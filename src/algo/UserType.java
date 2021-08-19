package algo;

public enum UserType {
    USER_STUDENT("student"),
    USER_ADMIN("admin"),
    USER_UNKNOWN("unknown");
    private final String getUserType;

    UserType(String getUserType) {
        this.getUserType = getUserType;
    }

    public String getGetUserType() {
        return getUserType;
    }
}
