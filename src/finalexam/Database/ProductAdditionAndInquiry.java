package finalexam.Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductAdditionAndInquiry {
    private static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/java?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";

    public static void main(String[] args) throws Exception {
        Class.forName(DBDRIVER);
        Connection conn = DriverManager.getConnection(DBURL,"root","123456");
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("===超市商品管理维护====");
            System.out.println("1、输入商品");
            System.out.println("2、查询商品");
            System.out.println("3、退出");
            System.out.print("请选择（1-3）：");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    addProduct(sc, conn);
                    break;
                case 2:
                    queryProduct(sc, conn);
                    break;
                case 3:
                    System.out.println("退出系统");
                    break;
                default:
                    System.out.println("无效选择，请重新选择（1-3）：");
            }
        } while (choice != 3);
        conn.close();
    }

    private static void addProduct(Scanner sc, Connection conn) throws SQLException {
        System.out.print("请输入商品信息（格式：条形码，商品名称，单价，供应商）：");
        String input = sc.nextLine();
        String[] productInfo = input.split("[,，]");
        String tiaoma = productInfo[0];
        PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM t_shangping WHERE tiaoma = ?");
        checkStmt.setString(1, tiaoma);
        ResultSet rs = checkStmt.executeQuery();
        rs.next();
        if (rs.getInt(1) > 0) {
            System.out.println("条形码不能重复，请重新输入");
            return;
        }
        PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO t_shangping (tiaoma, mingcheng, danjia, gongyingshang) VALUES (?, ?, ?, ?)");
        insertStmt.setString(1, tiaoma);
        insertStmt.setString(2, productInfo[1]);
        insertStmt.setBigDecimal(3, new BigDecimal(productInfo[2]));
        insertStmt.setString(4, productInfo[3]);
        int count = insertStmt.executeUpdate();
        if(count > 0)
            System.out.println("增加成功");
        else
            System.out.println("增加失败");
    }

    private static void queryProduct(Scanner sc, Connection conn) throws SQLException {
        System.out.print("请输入查询的商品名称：");
        String nameQuery = sc.nextLine();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT * FROM t_shangping WHERE mingcheng LIKE ?");
        queryStmt.setString(1, "%" + nameQuery + "%");
        ResultSet rs = queryStmt.executeQuery();
        int recordCount = 0;
        while(rs.next()) {
            recordCount++;
        }
        System.out.println("满足条件的记录总共" + recordCount + "条，信息如下：");
        rs = queryStmt.executeQuery();
        System.out.println("序号\t条形码\t商品名称\t单价\t供应商");
        int count = 1;
        while (rs.next()) {
            System.out.println(count++ + "\t" +
                    rs.getString("tiaoma") + "\t" +
                    rs.getString("mingcheng") + "\t" +
                    rs.getBigDecimal("danjia") + "\t" +
                    rs.getString("gongyingshang"));
        }
    }
}
