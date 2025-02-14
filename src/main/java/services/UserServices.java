package services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import entity.RoleEntity;
import entity.UserEntity;
import entity.TaskEntity; // Sửa đổi: Import TaskEntity
import repository.RoleRepository;
import repository.UserRepository;

//Xử lí logic code của User Controller
public class UserServices {
	//tên hàm services xử lí gì thì tên đó
	
	//Tạo 1 cái repository
	private RoleRepository roleRepository = new RoleRepository();
	private UserRepository userRepository = new UserRepository();
	
	
	public List<RoleEntity> getRole(){
		List<RoleEntity> roles = roleRepository.findAll();
		
		return roles;
	}
	
	public  boolean insertUser(String fullname, String email, String password, String phone, int roleid) {
		String passwordEncode = getMd5(password);
		return userRepository.insertUser(fullname, email, passwordEncode, phone, roleid)>0; 
	}
	
	public static String getMd5(String input)
    {
        try {
 
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
 
            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
 
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
 
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
 
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

	public List<UserEntity> getAllUsers() {
	    return userRepository.findAll();
	}

	public boolean deleteUser(int id) {
		return userRepository.deleteUser(id) > 0;
	}
	
	public UserEntity getUserById(int id) {
		return userRepository.findById(id);
	}

	public boolean updateUser(int id, String fullname, String email, String phone, int roleId) {
		return userRepository.updateUser(id, fullname, email, phone, roleId) > 0;
	}

    // Sửa đổi: Thêm phương thức lấy danh sách tasks từ repository
    public List<TaskEntity> getTasksByUserId(int userId) {
        return userRepository.findTasksByUserId(userId);
    }
	
}