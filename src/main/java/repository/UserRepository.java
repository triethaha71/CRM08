package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MysqlConfig;
import entity.RoleEntity;
import entity.UserEntity;
import entity.TaskEntity; // Import TaskEntity

public class UserRepository {

	public int insertUser(String fullname, String email, String password, String phone, int roleid) {
		String query="INSERT INTO users(fullname,email,password,phone, role_id) VALUES (?,?,?,?,?)";

		int count = 0;
		try {
			// Mở kết nối CSDL
			 Connection connection = MysqlConfig.getConnection();
			 // Truyền câu truy vấn vào CSDL để kết nối
			 PreparedStatement statement = connection.prepareStatement(query);
			 //Xuất các kết quả truy vấn của mình về mảng

			 statement.setString(1, fullname);
			 statement.setString(2, email);
			 statement.setString(3,password);
			 statement.setString(4, phone);
			 statement.setInt(5, roleid);


			 count = statement.executeUpdate();


		 }catch (Exception e) {
			 //Đặt tên hàm sau này lỗi dễ research để biết lỗi nó nằm ở đâu
			System.out.println("findAll"+ e.getLocalizedMessage());
		}
		return count;
	}

	public List<UserEntity> findAll() {
		List<UserEntity> listUser = new ArrayList<UserEntity>();

		String query = "SELECT u.id, u.fullname, u.email, u.phone  , r.name, r.description, r.id as role_id\r\n" +
				"FROM users u\r\n" +
				"JOIN roles r ON r.id = u.role_id";

		Connection connection = MysqlConfig.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				UserEntity entity = new UserEntity();
				entity.setEmail(result.getString("email"));
				entity.setId(result.getInt("id"));
				entity.setFullname(result.getString("fullname"));
				entity.setPhone(result.getString("phone"));

				RoleEntity roleEntity = new RoleEntity();

				roleEntity.setName(result.getString("name"));
				roleEntity.setDescription(result.getString("description"));
				roleEntity.setId(result.getInt("role_id"));

				entity.setRole(roleEntity);

				listUser.add(entity);
			}

		} catch (Exception e) {
			System.out.println("findAll"+ e.getLocalizedMessage());
		}

		return listUser;
	}

	public int deleteUser(int id) {
		int count = 0;
		String query = "DELETE FROM users WHERE id = ?";
		Connection connection = MysqlConfig.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);

			count = statement.executeUpdate();

		} catch (Exception e) {
			System.out.println("deleteUser"+ e.getLocalizedMessage());
		}

		return count;
	}

	public UserEntity findById(int id) {
		UserEntity entity = null;
		String query = "SELECT u.id, u.fullname, u.email, u.phone  , r.name, r.description, r.id as role_id\r\n" +
				"FROM users u\r\n" +
				"JOIN roles r ON r.id = u.role_id WHERE u.id = ?";

		Connection connection = MysqlConfig.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				entity = new UserEntity();
				entity.setEmail(result.getString("email"));
				entity.setId(result.getInt("id"));
				entity.setFullname(result.getString("fullname"));
				entity.setPhone(result.getString("phone"));

				RoleEntity roleEntity = new RoleEntity();

				roleEntity.setName(result.getString("name"));
				roleEntity.setDescription(result.getString("description"));
				roleEntity.setId(result.getInt("role_id"));

				entity.setRole(roleEntity);
			}

		} catch (Exception e) {
			System.out.println("findById"+ e.getLocalizedMessage());
		}

		return entity;
	}

	public int updateUser(int id, String fullname, String email, String phone, int roleId) {
		int count = 0;
		String query = "UPDATE users SET fullname = ?, email = ?, phone = ?, role_id = ? WHERE id = ?";
		Connection connection = MysqlConfig.getConnection();

		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, fullname);
			statement.setString(2, email);
			statement.setString(3, phone);
			statement.setInt(4, roleId);
			statement.setInt(5, id);

			count = statement.executeUpdate();

		} catch (Exception e) {
			System.out.println("updateUser"+ e.getLocalizedMessage());
		}

		return count;
	}

    public List<TaskEntity> findTasksByUserId(int userId) {
        List<TaskEntity> tasks = new ArrayList<>();
        String query = "SELECT t.id, t.name, t.start_date, t.end_date, t.user_id, t.job_id, t.status_id, s.name AS status_name " +
                       "FROM tasks t " +
                       "JOIN status s ON t.status_id = s.id " +
                       "WHERE t.user_id = ?";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) { // Auto-close ResultSet

                while (resultSet.next()) {
                    TaskEntity task = new TaskEntity();
                    task.setId(resultSet.getInt("id"));
                    task.setName(resultSet.getString("name"));
                    task.setStartDate(resultSet.getString("start_date"));
                    task.setEndDate(resultSet.getString("end_date"));
                    task.setUserId(resultSet.getInt("user_id"));
                    task.setJobId(resultSet.getInt("job_id"));
                    task.setStatusId(resultSet.getInt("status_id"));
                    task.setStatusName(resultSet.getString("status_name")); // Lấy tên status
                    tasks.add(task);
                }
            }

        } catch (SQLException e) { // Sửa đổi: Bắt SQLException cụ thể
            System.out.println("findTasksByUserId: " + e.getMessage()); // In ra chi tiết lỗi
            e.printStackTrace(); // In ra stack trace để debug dễ hơn
        }
        return tasks;
    }
}