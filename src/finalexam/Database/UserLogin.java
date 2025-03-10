package finalexam.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UserLogin {
    private static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/java?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";

    public static void main(String[] args) throws Exception {
        Class.forName(DBDRIVER);
        Connection conn = DriverManager.getConnection(DBURL, "root", "123456");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner sc = new Scanner(System.in);
        int attempts = 0;
        boolean loginSuccess = false;
        while (attempts < 3 && !loginSuccess) {
            System.out.println("欢迎使用阳光超市收银系统，请登陆");
            System.out.print("请输入用户名: ");
            String username = sc.nextLine();
            System.out.print("请输入密码: ");
            String password = sc.nextLine();
            String sql = "SELECT xingming FROM t_yong WHERE yonghuming=? AND mima=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, Encrypt.MD5(password));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                loginSuccess = true;
                String xingming = rs.getString("xingming");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                String currentTime = formatter.format(new Date());
                System.out.println("欢迎使用阳光超市收银系统");
                System.out.println("当前收银员：" + xingming);
                System.out.println("当前时间：" + currentTime);
            } else {
                System.out.println("用户名或密码不正确，请重新输入");
                attempts++;
            }
        }
        if (!loginSuccess)
            System.out.println("最多只能尝试3次");
        rs.close();
        pstmt.close();
        conn.close();
    }
}
