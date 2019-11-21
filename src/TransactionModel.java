class TransactionModel {
    int UUID, custUUID, prodUPC;
    double price;
    int qty;
    double subtotal, tax, total;
    String date;

    public String toString() {
        return "(" + UUID + ", " +  custUUID + ", " + prodUPC + ", " +
                price + ", " + qty + ", " + subtotal + ", " + tax + ", " +
                total + ", \"" + date + "\")";
    }
}
