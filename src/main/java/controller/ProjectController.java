package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ProjectEntity;
import services.ProjectServices;

@WebServlet(urlPatterns = {"/groupwork", "/groupwork-add", "/groupwork-edit", "/groupwork-delete", "/groupwork-details"})
public class ProjectController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectServices projectService = new ProjectServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/groupwork".equals(path)) {
            // Hiển thị danh sách dự án
            List<ProjectEntity> projects = projectService.getAllProjects();
            request.setAttribute("projects", projects);
            request.getRequestDispatcher("groupwork.jsp").forward(request, response);
        } else if ("/groupwork-add".equals(path)) {
            // Hiển thị trang thêm mới dự án
            request.getRequestDispatcher("groupwork-add.jsp").forward(request, response);
        } else if ("/groupwork-edit".equals(path)) {
            // Hiển thị trang sửa dự án
            int id = Integer.parseInt(request.getParameter("id"));
            ProjectEntity project = projectService.getProjectById(id);

            // Định dạng lại ngày tháng trước khi hiển thị
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat userFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                Date startDate = dbFormat.parse(project.getStartDate());
                Date endDate = dbFormat.parse(project.getEndDate());

                project.setStartDate(userFormat.format(startDate));
                project.setEndDate(userFormat.format(endDate));

            } catch (ParseException e) {
                System.err.println("Error parsing date (doGet): " + e.getMessage());
                e.printStackTrace();
            }

            request.setAttribute("project", project);
            request.getRequestDispatcher("groupwork-edit.jsp").forward(request, response);
        } else if ("/groupwork-delete".equals(path)) {
            // Xóa dự án
            int id = Integer.parseInt(request.getParameter("id"));
            projectService.deleteProject(id);
            response.sendRedirect("groupwork");
        } else if ("/groupwork-details".equals(path)) {
            // Hiển thị chi tiết dự án
            int id = Integer.parseInt(request.getParameter("id"));
            ProjectEntity project = projectService.getProjectById(id);

            if (project == null) {
                // Xử lý trường hợp không tìm thấy project
                response.sendRedirect("groupwork"); // Hoặc chuyển hướng đến trang lỗi
                return;
            }

            // Định dạng lại ngày tháng trước khi hiển thị
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat userFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                if (project.getStartDate() != null && !project.getStartDate().isEmpty()) {
                    Date startDate = dbFormat.parse(project.getStartDate());
                    project.setStartDate(userFormat.format(startDate));
                }

                if (project.getEndDate() != null && !project.getEndDate().isEmpty()) {
                    Date endDate = dbFormat.parse(project.getEndDate());
                    project.setEndDate(userFormat.format(endDate));
                }

            } catch (ParseException e) {
                System.err.println("Error parsing date (doGet): " + e.getMessage());
                e.printStackTrace();
            }

            request.setAttribute("project", project);
            request.getRequestDispatcher("groupwork-details.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/groupwork-add".equals(path)) {
            // Xử lý thêm mới dự án
            String name = request.getParameter("name");
            String startDateString = request.getParameter("startDate");
            String endDateString = request.getParameter("endDate");

            try {
                // Chuyển đổi định dạng ngày tháng
                SimpleDateFormat userFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date startDate = userFormat.parse(startDateString);
                Date endDate = userFormat.parse(endDateString);

                String startDateDB = dbFormat.format(startDate);
                String endDateDB = dbFormat.format(endDate);

                ProjectEntity project = new ProjectEntity();
                project.setName(name);
                project.setStartDate(startDateDB);
                project.setEndDate(endDateDB);

                projectService.addProject(project);
                response.sendRedirect("groupwork"); // Chuyển hướng về trang danh sách dự án

            } catch (ParseException e) {
                System.err.println("Error parsing date (doPost): " + e.getMessage());
                e.printStackTrace();
                // Xử lý lỗi, ví dụ: hiển thị thông báo lỗi cho người dùng
                request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ (dd/MM/yyyy)");
                request.getRequestDispatcher("groupwork-add.jsp").forward(request, response);
                return; // Dừng xử lý nếu có lỗi
            }
        } else if ("/groupwork-edit".equals(path)) {
            // Xử lý sửa dự án
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String startDateString = request.getParameter("startDate");
            String endDateString = request.getParameter("endDate");

            try {
                // Chuyển đổi định dạng ngày tháng
                SimpleDateFormat userFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date startDate = userFormat.parse(startDateString);
                Date endDate = userFormat.parse(endDateString);

                String startDateDB = dbFormat.format(startDate);
                String endDateDB = dbFormat.format(endDate);

                ProjectEntity project = new ProjectEntity();
                project.setId(id);
                project.setName(name);
                project.setStartDate(startDateDB);
                project.setEndDate(endDateDB);

                projectService.updateProject(project);
                response.sendRedirect("groupwork"); // Chuyển hướng về trang danh sách dự án

            } catch (ParseException e) {
                System.err.println("Error parsing date (doPost): " + e.getMessage());
                e.printStackTrace();
                // Xử lý lỗi, ví dụ: hiển thị thông báo lỗi cho người dùng
            }
        }
    }
}