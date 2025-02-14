package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.MysqlConfig;
import entity.RoleEntity;
import entity.UserEntity;

@WebServlet(name = "loginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy cookie và kiểm tra nếu có lưu email và password
        Cookie[] cookies = req.getCookies();
        String email = "";
        String password = "";

        if (cookies != null) {  // Kiểm tra nếu cookies không phải là null
            for (Cookie item : cookies) {
                if ("email".equals(item.getName())) {
                    email = item.getValue();
                }
                if ("password".equals(item.getName())) {
                    password = item.getValue();
                }
            }
        }		

        // Gửi email và password tới trang login.jsp
        req.setAttribute("email", email);
        req.setAttribute("password", password);

        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember-me"); // Null nếu không được chọn, không null nếu được chọn

        List<UserEntity> listUser = new ArrayList<>();

        // B1: Chuẩn bị câu truy vấn
        String query = "SELECT r.name, u.email, u.password "
                + "FROM users u "
                + "JOIN roles r ON u.role_id = r.id "
                + "WHERE u.email = ? AND u.password = ?";

        // B2: Mở kết nối tới CSDL và thực thi truy vấn
        Connection connection = MysqlConfig.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            //Truyền các giá trị dấu ? ở câu truy vấn
            statement.setString(1, email);
            statement.setString(2, password);
            
            //excuteQuery: SELECT
            
            //excuteUpdate: Không phải câu SELECT
            ResultSet result = statement.executeQuery();

            // Duyệt kết quả và thêm vào danh sách người dùng
            while (result.next()) {
                UserEntity entity = new UserEntity();
                entity.setEmail(result.getString("email"));

                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setName(result.getString("name"));

                entity.setRole(roleEntity);
                listUser.add(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Kiểm tra kết quả và xử lý
        if (listUser.size() > 0) {
            if (remember != null) {
                Cookie tkEmail = new Cookie("email", email);
                Cookie tkPassword = new Cookie("password", password);
                Cookie ckRole = new Cookie("role", listUser.get(0).getRole().getName());
                						
                // Đặt thời gian sống cho cookie (7 ngày)
                tkEmail.setMaxAge(60 * 60 * 24 * 7);
                tkPassword.setMaxAge(60 * 60 * 24 * 7);
                ckRole.setMaxAge(60 * 60 * 24 * 7);

                resp.addCookie(tkEmail);
                resp.addCookie(tkPassword);
                resp.addCookie(ckRole);
            }

            // Điều hướng đến trang chủ
            String contextPath = req.getContextPath();
            resp.sendRedirect(contextPath + "/");
        } else {
            // Đăng nhập thất bại
            req.setAttribute("message", "Đăng nhập thất bại. Vui lòng kiểm tra lại email và mật khẩu.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
