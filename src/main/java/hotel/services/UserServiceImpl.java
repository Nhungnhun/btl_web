package hotel.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hotel.entity.Role;
import hotel.entity.User;
import hotel.repository.RoleRep;
import hotel.repository.UserRep;
import hotel.temp.CurrentUser;

@Service
public class UserServiceImpl implements UserService {
	
	// service pattern to manage transactionals  
	//	and handel services for user between server and client

	private UserRep userRepository;
	
	private RoleRep roleRepository;

	@Autowired
	public UserServiceImpl(UserRep userRepository, RoleRep roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		
	}


	@Autowired
//	chạy được ở nhiều máy chủ
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	// kiểm tra người dùng qua email
	@Override
	@Transactional
	public User findUserByEmail(String email) {

		return userRepository.findByEmail(email);
	}

	// chuyển dữ liệu giữa Người dùng tạm thời và lớp Người dùng sau khi kiểm tra để lưu lại
	@Override
	@Transactional
	public void saveUser(CurrentUser currentUser) {
		User user = new User();

		// mật khẩu lưu trong cơ sở dữ liệu
		user.setPassword(passwordEncoder().encode(currentUser.getPassword()));
		
		user.setUsername(currentUser.getUsername());
		user.setEmail(currentUser.getEmail());

		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_EMPLOYEE")));

		userRepository.save(user);
	}

	// lấy id người dùng đã đăng nhập bằng email
	@Override
	@Transactional
	public int getLoggedUserId() {
		User user = userRepository.findByUsername(loggedUserEmail());
		return user.getId();
	}

	// đăng nhập bảo mật kiểm tra hợp lệ
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	// quyền hạn người dùng
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	// nhận email người dùng đã đăng nhập 
	private String loggedUserEmail() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}

		return principal.toString();
	}

	@Override
	public void deleteUser(int resId) {
		userRepository.deleteById(resId);
		
	}

}
