package hotel.services;

import hotel.entity.User;
import hotel.temp.CurrentUser;

import org.springframework.security.core.userdetails.UserDetailsService;

//dịch vụ cho người dùng
public interface UserService extends UserDetailsService {

	public User findUserByEmail(String email);

	public void saveUser(CurrentUser currentUser);

	public int getLoggedUserId();
	public void deleteUser(int resId);
}
