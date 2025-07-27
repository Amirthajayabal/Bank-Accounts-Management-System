import java.sql.*;
import java.util.*;

public class BankApp {
    static final String URL = "jdbc:mysql://localhost:3306/bank_db";
    static final String USER = "root";
    static final String PASS = ""; // your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);

            while (true) {
                System.out.println("\n--- Bank Account Management System ---");
                System.out.println("1. Create Account");
                System.out.println("2. View All Accounts");
                System.out.println("3. Deposit");
                System.out.println("4. Withdraw");
                System.out.println("5. Delete Account");
                System.out.println("6. Exit");
                System.out.print("Choose: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Acc No: ");
                        int accNo = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Initial Balance: ");
                        double bal = sc.nextDouble();

                        PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?)");
                        ps.setInt(1, accNo);
                        ps.setString(2, name);
                        ps.setDouble(3, bal);
                        ps.executeUpdate();
                        System.out.println("✅ Account Created.");
                        break;

                    case 2:
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
                        System.out.println("\nAccount No | Name | Balance");
                        while (rs.next()) {
                            System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getDouble(3));
                        }
                        break;

                    case 3:
                        System.out.print("Enter Acc No to Deposit: ");
                        int depAcc = sc.nextInt();
                        System.out.print("Enter Amount: ");
                        double depAmt = sc.nextDouble();
                        ps = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE acc_no = ?");
                        ps.setDouble(1, depAmt);
                        ps.setInt(2, depAcc);
                        ps.executeUpdate();
                        System.out.println("✅ Deposited Successfully.");
                        break;

                    case 4:
                        System.out.print("Enter Acc No to Withdraw: ");
                        int wAcc = sc.nextInt();
                        System.out.print("Enter Amount: ");
                        double wAmt = sc.nextDouble();

                        ps = conn.prepareStatement("SELECT balance FROM accounts WHERE acc_no = ?");
                        ps.setInt(1, wAcc);
                        rs = ps.executeQuery();
                        if (rs.next()) {
                            double currentBal = rs.getDouble("balance");
                            if (wAmt <= currentBal) {
                                ps = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE acc_no = ?");
                                ps.setDouble(1, wAmt);
                                ps.setInt(2, wAcc);
                                ps.executeUpdate();
                                System.out.println("✅ Withdrawn Successfully.");
                            } else {
                                System.out.println("❌ Insufficient Balance.");
                            }
                        } else {
                            System.out.println("❌ Account Not Found.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter Acc No to Delete: ");
                        int delAcc = sc.nextInt();
                        ps = conn.prepareStatement("DELETE FROM accounts WHERE acc_no = ?");
                        ps.setInt(1, delAcc);
                        ps.executeUpdate();
                        System.out.println("✅ Account Deleted.");
                        break;

                    case 6:
                        conn.close();
                        System.out.println("👋 Exiting. Thank you!");
                        return;

                    default:
                        System.out.println("❌ Invalid Option.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();
    }
}