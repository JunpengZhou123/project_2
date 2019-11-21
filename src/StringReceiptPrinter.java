class StringReceiptPrinter implements ReceiptPrinter {
    @Override
    public void print(String header, CustomerModel c, ProductModel p,
                      TransactionModel t, String footer) {
        String r = header + "\n" +
                t.date + "\n" +
                "Customer UUID: " + c.UUID + "\n" +
                "Customer Name: " + c.name + "\n" +
                "\n" +
                "Product UPC:   " + p.UPC + "\n" +
                "Product Name:  " + p.name + "\n" +
                "Unit Price:    " + String.format("$%.2f", p.price) + "\n" +
                "\n" +
                "Subtotal: " + String.format("$%.2f", t.subtotal) + "\n" +
                "Tax:      " + String.format("$%.2f", t.tax) + "\n" +
                "Total:    " + String.format("$%.2f", t.total) + "\n";
        if (footer.length() != 0) {
            r += "\n" + footer + "\n";
        }
        System.out.println(r);
    }
}
