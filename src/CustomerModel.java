class CustomerModel {
    int UUID; // Primary key
    String name;
    String address;
    String payment;
    String phone;

    public String toString() {
        return "(" + UUID + ", " +
                "\"" + name  + "\", " +
                "\"" + address  + "\", " +
                "\"" + payment  + "\", " +
                "\"" + phone  + "\")";
    }
}
