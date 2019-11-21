# Database test initialization data

## Prepare database tables
```SQL
CREATE TABLE Customers (
	UUID int NOT NULL PRIMARY KEY,
	Name varchar(255) NOT NULL,
	Address varchar(255),
	Payment varchar(255),
	Phone char(10)
);
CREATE TABLE Products (
	UPC int NOT NULL PRIMARY KEY,
	Name varchar(255) NOT NULL,
	Price decimal(16,2),
	Qty int,
	Vendor varchar(255),
	Description varchar(8191),
	TaxSch decimal(5,2)
);
CREATE TABLE Transactions (
UUID int NOT NULL PRIMARY KEY,
CustUUID int NOT NULL,
ProdUPC int NOT NULL,
Price decimal(16,2),
Qty int,
Subtotal decimal(16,2),
Tax decimal(16,2),
Total decimal(16,2),
Date varchar(31),
FOREIGN KEY(CustUUID) REFERENCES Customers(UUID),
FOREIGN KEY(ProdUPC) REFERENCES Products(UPC)
);
```

## Insert data
```SQL
INSERT INTO Customers (UUID, Name, Address, Payment, Phone) VALUES (1, "Alpha", "Via Po, 23", "8956901052546734", "2053346078");
INSERT INTO Customers (UUID, Name, Address, Payment, Phone) VALUES (2, "Beta", "Via delle Rosine, 3", "7338063739430719", "2053345543");
INSERT INTO Customers (UUID, Name, Address, Payment, Phone) VALUES (3, "Gamma", "37 Nacagdoches Blvd", "8490731076093436", "2053349908");
INSERT INTO Customers (UUID, Name, Address, Payment, Phone) VALUES (4, "Delta", "20554 County Road 8", "4264566737024490", "2053342200");
INSERT INTO Customers (UUID, Name, Address, Payment, Phone) VALUES (5, "Epsilon", "PO Box 419b", "8553221328320811", "2053343340");
INSERT INTO Products (UPC, Name, Price, Qty, Vendor, Description, TaxSch) VALUES (1, "Apple", 0.50, 250, "Sysco", "Honeycrisp apples, bulk", 9.0);
INSERT INTO Products (UPC, Name, Price, Qty, Vendor, Description, TaxSch) VALUES (2, "Banana", 0.40, 525, "Sysco", "Bananas, bulk", 9.0);
INSERT INTO Products (UPC, Name, Price, Qty, Vendor, Description, TaxSch) VALUES (3, "Grapes", 4.00, 30, "Sysco", "Red table grapes, 24 oz pkg", 9.0);
INSERT INTO Products (UPC, Name, Price, Qty, Vendor, Description, TaxSch) VALUES (4, "Date", 6.00, 24, "Sysco", "Pitted dates, 16 oz pkg", 9.0);
INSERT INTO Products (UPC, Name, Price, Qty, Vendor, Description, TaxSch) VALUES (5, "Eggplant", 4.50, 60, "Sysco", "Eggplant, bulk", 9.0);
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (1,4,2,0.40,20,8.00,0.72,8.72,"Thu Oct 03 10:09:30 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (2,5,1,0.50,6,3.00,0.27,3.27,"Thu Oct 03 10:20:20 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (3,4,1,0.50,32,16.00,1.44,17.44,"Thu Oct 03 10:20:32 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (4,3,2,0.40,3,1.20,0.11,1.31,"Thu Oct 03 10:25:01 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (5,5,3,4.00,1,4.00,0.36,4.36,"Thu Oct 03 10:40:35 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (6,4,5,4.50,4,18.00,1.62,19.62,"Thu Oct 03 10:44:45 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (7,3,2,0.40,50,20.00,1.80,21.80,"Thu Oct 03 10:58:12 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (8,3,4,6.00,2,12.00,1.08,13.08,"Thu Oct 03 11:08:03 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (9,4,4,6.00,1,6.00,0.54,6.54,"Thu Oct 03 11:30:06 CDT 2019");
INSERT INTO Transactions (UUID, CustUUID, ProdUPC, Price, Qty, Subtotal, Tax, Total, Date) VALUES (10,1,5,4.50,2,9.00,0.81,9.81,"Thu Oct 03 11:40:00 CDT 2019");
```
