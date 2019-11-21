public class ProductModel {
    int UPC; // Primary key
    String name;
    double price;
    String vendor;
    double taxSch;
    int qty;
    String description;

    public String toString() {
        return "(" + UPC + ", " +
                "\"" + name + "\", " +
                price + ", " +
                "\"" + vendor + "\", " +
                taxSch + ", " +
                qty + ", " +
                "\"" + description + "\")";
    }
}
