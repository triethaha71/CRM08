package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.RoleEntity;
import entity.UserEntity;
import entity.TaskEntity; // Sửa đổi: Import TaskEntity
import services.UserServices;

@WebServlet(name = "usercController", urlPatterns = { "/user", "/user-add", "/user-edit", "/user-delete", "/user-detail" })
public class UserController extends HttpServlet {

    private UserServices userServices = new UserServices();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/user":
                getUser(req, resp);
                break;

            case "/user-add":
                userAdd(req, resp);
                break;
            case "/user-edit":
                // Xử lý logic cho việc hiển thị form sửa user
                getUserEdit(req, resp);
                break;
            case "/user-delete":
                // Xử lý logic cho việc xóa user
                deleteUser(req, resp);
                break;
            case "/user-detail":
                showUserDetail(req, resp); // Thêm case cho /user-detail
                break;

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getServletPath();
        switch (path) {
            case "/user":

                break;

            case "/user-add":

                String fullname = req.getParameter("fullname");
                String password = req.getParameter("password");
                String email = req.getParameter("email");
                String phone = req.getParameter("phone");
                int idRole = Integer.parseInt(req.getParameter("role"));

                userServices.insertUser(fullname, email, password, phone, idRole);
                userAdd(req, resp);
                break;
            case "/user-edit":
                // Xử lý logic cho việc cập nhật user
                updateUser(req, resp);
                break;

        }
    }

    private void userAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<RoleEntity> roles = userServices.getRole();
        req.setAttribute("roles", roles);
        req.getRequestDispatcher("user-add.jsp").forward(req, resp);
    }

    private void getUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<UserEntity> listUser = userServices.getAllUsers();
        req.setAttribute("listUser", listUser);
        req.getRequestDispatcher("user-table.jsp").forward(req, resp);
    }

    private void getUserEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        UserEntity user = userServices.getUserById(id);
        List<RoleEntity> roles = userServices.getRole();

        req.setAttribute("user", user);
        req.setAttribute("roles", roles);

        req.getRequestDispatcher("user-edit.jsp").forward(req, resp);
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        int roleId = Integer.parseInt(req.getParameter("role"));

        userServices.updateUser(id, fullname, email, phone, roleId);
        resp.sendRedirect(req.getContextPath() + "/user"); // Redirect về trang danh sách user
    }


    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        userServices.deleteUser(id);
        resp.sendRedirect(req.getContextPath() + "/user"); // Redirect về trang danh sách user
    }

    // Sửa đổi: Thêm phương thức để hiển thị chi tiết user và tasks
    private void showUserDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id")); // Lấy ID từ request parameter
        UserEntity user = userServices.getUserById(id); // Lấy user từ service
        List<TaskEntity> tasks = userServices.getTasksByUserId(id); // Sửa đổi: Lấy danh sách tasks

        req.setAttribute("user", user); // Đặt user vào request attribute
        req.setAttribute("tasks", tasks); // Sửa đổi: Đặt danh sách tasks vào request attribute
        req.getRequestDispatcher("/user-detail.jsp").forward(req, resp); // Forward đến trang JSP
    }

}