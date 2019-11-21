public final class Driver {
    static final DBClient db = new DBClient();
    static final ReceiptPrinter printer = new StringReceiptPrinter();

    public static void main(String[] args) {
//        new LoginUI();
        new AdminUI();
//        new CustomerUI();
//        new AddAdminUI();
//        new ListTransactionUI();
//        new ModAdminUI();
    }
}
