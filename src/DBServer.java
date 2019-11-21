import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class DBServer {
    static final int ERR_NO_SUCH_ITEM = 1;
    static final int ERR_INVALID_COMMAND = 2;
    static final int ERR_INVALID_UUID = 3;
    static final int ERR_SAV_DUPLICATE = 4;
    static final int ERR_INTERNAL = 5;
    static final int GET_OK = 300;
    static final int SAV_OK_NEW = 301;
    static final int SAV_OK_OVR = 302;

    // SQL function return codes
    private static final int CONNECT_OK = 100;
    private static final int DISCONNECT_OK = 101;
    private static final int SAVE_OK = 102;
    private static final int ERR_CONNECT = 200;
    private static final int ERR_DISCONNECT = 201;
    private static final int ERR_DUPLICATE = 202;
    private static final int ERR_UNKNOWN = 203;

    static final int PORT = 1024;
    private static final String SQL_PATH = "jdbc:sqlite:" +
            "C:/Users/cdmoomaw/comp3700/project_2_intellij/store.db";

    private static Connection conn = null;
    private static Gson gson = null;

    public static void main (String[] args) {
        int connStatus = connect();
        if (connStatus == ERR_CONNECT) {
            System.err.println("[ERR] Cannot connect to SQL. Exiting...");
            return;
        }
        gson = new GsonBuilder().create();
        runLoop();
        connStatus = disconnect();
        if (connStatus == ERR_DISCONNECT) {
            System.err.println("[ERR] Cannot disconnect from SQL. DB may be " +
                    "corrupt.");
        }
    }

    private static int connect() {
        try {
            conn = DriverManager.getConnection(SQL_PATH);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return ERR_CONNECT;
        }
        System.out.println("[INFO] Connected to database");
        return CONNECT_OK;
    }

    private static int disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return ERR_DISCONNECT;
        }
        System.out.println("[INFO] Disconnected from database");
        return DISCONNECT_OK;
    }

    private static void runLoop() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket pipe = server.accept();
                System.out.println("[INFO] Accepted connection");
                PrintWriter out = new PrintWriter(pipe.getOutputStream(),
                        true);
                Scanner in = new Scanner(pipe.getInputStream());

                String command = in.nextLine();
                System.out.println("[DEBUG] got command: " + command);
                boolean put_ovr = false; // default to not overwrite products
                switch (command) {
                    case "GET_PROD":
                        int UPC;
                        try {
                            UPC = Integer.parseInt(in.nextLine());
                        } catch (NumberFormatException e) {
                            out.println(ERR_INVALID_UUID);
                            break;
                        }
                        ProductModel p = loadProduct(UPC);
                        if (p == null) {
                            out.println(ERR_NO_SUCH_ITEM);
                            break;
                        }
                        out.println(GET_OK); // Tell the client data is coming
                        String sJSON_out = gson.toJson(p);
                        System.out.println("[DEBUG] Generated JSON: " +
                                sJSON_out);
                        out.println(sJSON_out);
                        break;
                    case "GET_CUST":
                        int UUID;
                        try {
                            UUID = Integer.parseInt(in.nextLine());
                        } catch (NumberFormatException e) {
                            out.println(ERR_INVALID_UUID);
                            break;
                        }
                        CustomerModel c = loadCustomer(UUID);
                        if (c == null) {
                            out.println(ERR_NO_SUCH_ITEM);
                            break;
                        }
                        out.println(GET_OK);
                        sJSON_out = gson.toJson(c);
                        System.out.println("[DEBUG] Generated JSON: " +
                                sJSON_out);
                        out.println(sJSON_out);
                        break;
                    case "GET_TRAN":
                        // UUID declared int above
                        try {
                            UUID = Integer.parseInt(in.nextLine());
                        } catch (NumberFormatException e) {
                            out.println(ERR_INVALID_UUID);
                            break;
                        }
                        TransactionModel t = loadTransaction(UUID);
                        if (t == null) {
                            out.println(ERR_NO_SUCH_ITEM);
                            break;
                        }
                        out.println(GET_OK);
                        sJSON_out = gson.toJson(t);
                        System.out.println("[DEBUG] Generated JSON: " +
                                sJSON_out);
                        out.println(sJSON_out);
                        break;
                    case "PUT_PROD": // fall-through intentional
                        put_ovr = true;
                    case "PUT_PROD_NEW":
                        String sJSON = in.nextLine();
                        int status = saveProduct(gson.fromJson(sJSON,
                                ProductModel.class), put_ovr);
                        switch (status) {
                            case ERR_DUPLICATE:
                                out.println(ERR_SAV_DUPLICATE);
                                break;
                            case ERR_UNKNOWN:
                                out.println(ERR_INTERNAL);
                                break;
                            case SAVE_OK:
                                if (put_ovr) {
                                    out.println(SAV_OK_OVR);
                                    break;
                                }
                                out.println(SAV_OK_NEW);
                        }
                        break;
                    case "PUT_CUST":
                        put_ovr = true;
                    case "PUT_CUST_NEW":
                        sJSON = in.nextLine();
                        status = saveCustomer(gson.fromJson(sJSON,
                                CustomerModel.class), put_ovr);
                        switch (status) {
                            case ERR_DUPLICATE:
                                out.println(ERR_SAV_DUPLICATE);
                                break;
                            case ERR_UNKNOWN:
                                out.println(ERR_INTERNAL);
                                break;
                            case SAVE_OK:
                                if (put_ovr) {
                                    out.println(SAV_OK_OVR);
                                    break;
                                }
                                out.println(SAV_OK_NEW);
                        }
                        break;
                    case "PUT_TRAN":
                        put_ovr = true;
                    case "PUT_TRAN_NEW":
                        sJSON = in.nextLine();
                        status = saveTransaction(gson.fromJson(sJSON,
                                TransactionModel.class), put_ovr);
                        switch(status) {
                            case ERR_DUPLICATE:
                                out.println(ERR_SAV_DUPLICATE);
                                break;
                            case ERR_UNKNOWN:
                                out.println(ERR_INTERNAL);
                            case SAVE_OK:
                                if (put_ovr) {
                                    out.println(SAV_OK_OVR);
                                    break;
                                }
                                out.println(SAV_OK_NEW);
                        }
                        break;
                    case "STOP":
                        return;
                    default:
                        out.println(ERR_INVALID_COMMAND);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ProductModel loadProduct(int UPC) {
        ProductModel p = new ProductModel();
        try {
            String query = "SELECT UPC, Name, Price, Vendor, TaxSch, Qty, " +
                    "Description FROM Products WHERE UPC = " + UPC;
            ResultSet r = conn.createStatement().executeQuery(query);
            p.UPC = r.getInt("UPC");
            p.name = r.getString("Name");
            p.price = r.getDouble("Price");
            p.vendor = r.getString("Vendor");
            p.taxSch = r.getDouble("TaxSch");
            p.qty = r.getInt("Qty");
            p.description = r.getString("Description");
            r.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return p;
    }

    private static CustomerModel loadCustomer(int UUID) {
        CustomerModel c = new CustomerModel();
        try {
            String query = "SELECT UUID, Name, Address, Payment, Phone " +
                    "FROM Customers WHERE UUID = " + UUID;
            ResultSet r = conn.createStatement().executeQuery(query);
            c.UUID = r.getInt("UUID");
            c.name = r.getString("Name");
            c.address = r.getString("Address");
            c.payment = r.getString("Payment");
            c.phone = r.getString("Phone");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return c;
    }

    private static TransactionModel loadTransaction(int UUID) {
        TransactionModel t = new TransactionModel();
        try {
            String query = "SELECT UUID, CustUUID, ProdUPC, Price, Qty, " +
                    "Subtotal, Tax, Total, Date FROM Transactions WHERE " +
                    "UUID = " + UUID;
            ResultSet r = conn.createStatement().executeQuery(query);
            t.UUID = r.getInt("UUID");
            t.custUUID = r.getInt("CustUUID");
            t.prodUPC = r.getInt("ProdUPC");
            t.price = r.getDouble("Price");
            t.qty = r.getInt("Qty");
            t.subtotal = r.getDouble("Subtotal");
            t.tax = r.getDouble("Tax");
            t.total = r.getDouble("Total");
            t.date = r.getString("Date");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return t;
    }

    private static int saveProduct(ProductModel p, boolean overwrite) {
        try {
            String insertion = (overwrite ? "REPLACE" : "INSERT") +
                    " INTO Products (UPC, Name, Price, Vendor, TaxSch, Qty, " +
                    "Description) VALUES " + p;
            conn.createStatement().executeUpdate(insertion);
        } catch (SQLException e) {
            String m = e.getMessage();
            System.err.println(m);
            if (m.contains("UNIQUE constraint failed")) {
                return ERR_DUPLICATE;
            }
            return ERR_UNKNOWN;
        }
        return SAVE_OK;
    }

    private static int saveCustomer(CustomerModel c, boolean overwrite) {
        try {
            String insertion = (overwrite ? "REPLACE" : "INSERT") +
                    " INTO Customers (UUID, Name, Address, Payment, Phone) " +
                    "VALUES " + c;
            conn.createStatement().executeUpdate(insertion);
        } catch (SQLException e) {
            String m = e.getMessage();
            System.err.println(m);
            if (m.contains("UNIQUE constraint failed")) {
                return ERR_DUPLICATE;
            }
            return ERR_UNKNOWN;
        }
        return SAVE_OK;
    }

    private static int saveTransaction(TransactionModel t, boolean overwrite) {
        try {
            String insertion = (overwrite ? "REPLACE" : "INSERT") +
                    " INTO Transactions (UUID, CustUUID, ProdUPC, Price, " +
                    "Qty, Subtotal, Tax, Total, Date) VALUES " + t;
            conn.createStatement().executeUpdate(insertion);
        } catch (SQLException e) {
            String m = e.getMessage();
            System.err.println(m);
            if (m.contains("UNIQUE constraint failed")) {
                return ERR_DUPLICATE;
            }
            return ERR_UNKNOWN;
        }
        return SAVE_OK;
    }
}
