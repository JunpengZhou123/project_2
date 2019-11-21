interface ReceiptPrinter {
    void print(String header, CustomerModel c, ProductModel p,
               TransactionModel t, String footer);
}
