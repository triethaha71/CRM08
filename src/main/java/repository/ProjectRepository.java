package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MysqlConfig;
import entity.ProjectEntity;

public class ProjectRepository {

    public List<ProjectEntity> findAll() {
        List<ProjectEntity> list = new ArrayList<>();
        String query = "SELECT id, name, start_date, end_date FROM jobs";  // Sửa nếu cần

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ProjectEntity project = new ProjectEntity();
                project.setId(resultSet.getInt("id"));
                project.setName(resultSet.getString("name"));
                project.setStartDate(resultSet.getString("start_date"));
                project.setEndDate(resultSet.getString("end_date"));
                list.add(project);
            }

        } catch (SQLException e) {
            System.err.println("ProjectRepository.findAll: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public ProjectEntity findById(int id) {
        ProjectEntity project = null;
        String query = "SELECT id, name, start_date, end_date FROM jobs WHERE id = ?"; // Sửa nếu cần
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                project = new ProjectEntity();
                project.setId(resultSet.getInt("id"));
                project.setName(resultSet.getString("name"));
                project.setStartDate(resultSet.getString("start_date"));
                project.setEndDate(resultSet.getString("end_date"));
            }
        } catch (SQLException e) {
            System.err.println("ProjectRepository.findById: " + e.getMessage());
            e.printStackTrace();
        }
        return project;
    }

    public void insert(ProjectEntity project) {
        String query = "INSERT INTO jobs (name, start_date, end_date) VALUES (?, ?, ?)";  // Sửa nếu cần
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getStartDate());
            statement.setString(3, project.getEndDate());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new project was inserted successfully!");
            }

        } catch (SQLException e) {
            System.err.println("ProjectRepository.insert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(ProjectEntity project) {
        String query = "UPDATE jobs SET name = ?, start_date = ?, end_date = ? WHERE id = ?"; // Sửa nếu cần
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, project.getName());
            statement.setString(2, project.getStartDate());
            statement.setString(3, project.getEndDate());
            statement.setInt(4, project.getId());
            statement.executeUpdate();
            System.out.println("Project updated successfully!");

        } catch (SQLException e) {
            System.err.println("ProjectRepository.update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM jobs WHERE id = ?"; // Sửa nếu cần
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Project deleted successfully!");

        } catch (SQLException e) {
            System.err.println("ProjectRepository.delete: " + e.getMessage());
            e.printStackTrace();
        }
    }
}