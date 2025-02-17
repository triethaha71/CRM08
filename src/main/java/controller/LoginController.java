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
import javax.servlet.http.HttpSession;

import config.MysqlConfig;
import entity.RoleEntity;
import entity.UserEntity;
import repository.UserRepository;
import services.UserServices;

@WebServlet(name = "loginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private UserServices userService = new UserServices();
    private UserRepository userRepository = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy cookie và kiểm tra nếu có lưu email và password
        Cookie[] cookies = req.getCookies();
        String email = "";
        String password = "";

        if (cookies != null) {
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

        // B1: Chuẩn bị câu truy vấn - LẤY ĐẦY ĐỦ THÔNG TIN USER
        String query = "SELECT u.id, u.fullname, u.email, u.phone, r.id AS role_id, r.name AS role_name "
                + "FROM users u "
                + "JOIN roles r ON u.role_id = r.id "
                + "WHERE u.email = ? AND u.password = ?";

        // B2: Mở kết nối tới CSDL và thực thi truy vấn
        Connection connection = MysqlConfig.getConnection();
        try {
            // Mã hóa password trước khi truy vấn
            String passwordEncode = UserServices.getMd5(password); //Sử dụng mã hóa MD5 từ UserServices
            PreparedStatement statement = connection.prepareStatement(query);
            //Truyền các giá trị dấu ? ở câu truy vấn
            statement.setString(1, email);
            statement.setString(2, passwordEncode); // Sử dụng password đã mã hóa

            //excuteQuery: SELECT

            //excuteUpdate: Không phải câu SELECT
            ResultSet result = statement.executeQuery();

            // Duyệt kết quả và thêm vào danh sách người dùng
            while (result.next()) {
                UserEntity entity = new UserEntity();
                entity.setId(result.getInt("id"));  // Lấy ID
                entity.setFullname(result.getString("fullname")); // Lấy Fullname
                entity.setEmail(result.getString("email"));
                entity.setPhone(result.getString("phone"));

                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setId(result.getInt("role_id")); // Lấy role ID
                roleEntity.setName(result.getString("role_name"));  // Lấy Role Name
                entity.setRole(roleEntity);

                listUser.add(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Kiểm tra kết quả và xử lý
        if (listUser.size() > 0) {
            // Lấy user đầu tiên từ list
            UserEntity loggedInUser = listUser.get(0); //Không cần thiết phải lấy user đầu tiên nữa, đã check listUser.size() > 0 rồi

            if (remember != null) {
                Cookie tkEmail = new Cookie("email", email);
                Cookie tkPassword = new Cookie("password", password); // Lưu password gốc chứ không phải đã mã hóa
                Cookie ckRole = new Cookie("role", loggedInUser.getRole().getName());

                // Đặt thời gian sống cho cookie (7 ngày)
                tkEmail.setMaxAge(60 * 60 * 24 * 7);
                tkPassword.setMaxAge(60 * 60 * 24 * 7);
                ckRole.setMaxAge(60 * 60 * 24 * 7);

                resp.addCookie(tkEmail);
                resp.addCookie(tkPassword);
                resp.addCookie(ckRole);
            }

            // Lưu thông tin người dùng vào session
            HttpSession session = req.getSession();
            session.setAttribute("loggedInUser", loggedInUser);

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