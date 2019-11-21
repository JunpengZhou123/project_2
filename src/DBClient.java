import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class DBClient {
    static final int ERR_SERVER = 201;
    static final int ERR_DUPLICATE = 202;
    static final int SAVE_OK = 102;
    static final int ERR_CONNECT = 203;
    static final int CONNECT_OK = 101;

    private static final String HOST_NAME = "localhost";

    private Socket link;
    private Scanner in;
    private PrintWriter out;

    private Gson gson;

    DBClient() {
        gson = new GsonBuilder().create();
    }

    private int connect() {
        try {
            link = new Socket(HOST_NAME, DBServer.PORT);
            in = new Scanner(link.getInputStream());
            out = new PrintWriter(link.getOutputStream(),
                    true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ERR_SERVER;
        }
        return CONNECT_OK;
    }

    private int disconnect() {
        try {
            in.close();
            out.close();
            link.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ERR_SERVER;
        }
        return CONNECT_OK;
    }

    ProductModel loadProduct(int UPC) {
        if (connect() == ERR_SERVER) {
            return null;
        }
        out.println("GET_PROD");
        out.println(UPC);
        int response = Integer.parseInt(in.nextLine());
        ProductModel p = null;
        switch (response) {
            case DBServer.ERR_INVALID_UUID:
            case DBServer.ERR_NO_SUCH_ITEM:
                break;
            case DBServer.GET_OK:
                String sJSON = in.nextLine();
                System.out.println("[DEBUG] Got JSON: " + sJSON);
                p = gson.fromJson(sJSON, ProductModel.class);
        }
        disconnect();
        return p;
    }
    CustomerModel loadCustomer(int UUID) {
        if (connect() == ERR_SERVER) {
            return null;
        }
        out.println("GET_CUST");
        out.println(UUID);
        int response = Integer.parseInt(in.nextLine());
        CustomerModel c = null;
        switch (response) {
            case DBServer.ERR_INVALID_UUID:
            case DBServer.ERR_NO_SUCH_ITEM:
                break;
            case DBServer.GET_OK:
                String sJSON = in.nextLine();
                System.out.println("[DEBUG] Got JSON: " + sJSON);
                c = gson.fromJson(sJSON, CustomerModel.class);
        }
        disconnect();
        return c;
    }
    TransactionModel loadTransaction(int UUID) {
        if (connect() == ERR_SERVER) {
            return null;
        }
        out.println("GET_TRAN");
        out.println(UUID);
        int response = Integer.parseInt(in.nextLine());
        TransactionModel t = null;
        switch (response) {
            case DBServer.ERR_INVALID_UUID:
            case DBServer.ERR_NO_SUCH_ITEM:
                break;
            case DBServer.GET_OK:
                String sJSON = in.nextLine();
                System.out.println("[DEBUG] Got JSON: " + sJSON);
                t = gson.fromJson(sJSON, TransactionModel.class);
        }
        disconnect();
        return t;
    }

    private int saveResponses(int serverResponse) {
        switch (serverResponse) {
            case DBServer.ERR_SAV_DUPLICATE:
                return ERR_DUPLICATE; // should never happen
            case DBServer.ERR_INTERNAL:
                return ERR_SERVER;
            case DBServer.SAV_OK_NEW:
            case DBServer.SAV_OK_OVR:
                return SAVE_OK;
        }
        return ERR_SERVER; // should never happen
    }

    int saveProduct(ProductModel p) {
        if (connect() == ERR_SERVER) {
            return ERR_SERVER;
        }
        out.println("PUT_PROD");
        out.println(gson.toJson(p));
        int response = Integer.parseInt(in.nextLine());
        disconnect();
        return saveResponses(response);
    }
    int saveCustomer(CustomerModel c) {
        if (connect() == ERR_SERVER) {
            return ERR_SERVER;
        }
        out.println("PUT_CUST");
        out.println(gson.toJson(c));
        int response = Integer.parseInt(in.nextLine());
        disconnect();
        return saveResponses(response);
    }
    int saveTransaction(TransactionModel t) {
        if (connect() == ERR_SERVER) {
            return ERR_SERVER;
        }
        out.println("PUT_TRAN");
        out.println(gson.toJson(t));
        int response = Integer.parseInt(in.nextLine());
        disconnect();
        return saveResponses(response);
    }
}
