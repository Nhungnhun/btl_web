package hotel.dao;

import java.util.List;

import hotel.entity.User;


public interface UserDao {

    public User findUserByEmail(String email);

    public User findUserByUsername(String username);
    public User findUserById(String id);
    public void saveUser(User user);
    public void deleteUser(User user);
    public void deleteRoleUser(String id);
    public List<User> findAllUser();
    
}
