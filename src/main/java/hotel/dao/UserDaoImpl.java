package hotel.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hotel.entity.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	//truy xuất và gửi dữ liệu đến từ cơ sở dữ liệu cho người dùng


	@Autowired
	private EntityManager entityManager;

	// lấy người dùng từ cơ sở dữ liệu bằng email
	@Override
	public User findUserByEmail(String email) {
		
		Query<User> query = currentSession().createQuery("from User where user_email=:uEmail", User.class);
		query.setParameter("uEmail", email);

		// kiểm tra  người dùng
		User user = null;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}
		
	// lấy người dùng từ database bằng 
	@Override
	public User findUserByUsername(String username) {
		
		Query<User> query = currentSession().createQuery("from User where user_username=:uName", User.class);
		query.setParameter("uName", username);

		User user = null;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}
	
	// tạo người dùng và cập nhật nếu tồn tại
	@Override
	public void saveUser(User theUser) {
		currentSession().saveOrUpdate(theUser);
	}

	private Session currentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public List<User> findAllUser() {
		List<User> list = new ArrayList<>();
		User user = null;
		Query<User> query = currentSession().createQuery("from User", User.class);
		
			list = (List<User>) query.getResultList();
		
		return list;
	}

	@Override
	public void deleteUser(User user) {
		currentSession().delete(user);;
		
	}

	@Override
	public User findUserById(String id) {
		Query<User> query = currentSession().createQuery("from User where user_id=:uEmail", User.class);
		query.setParameter("uEmail", id);

		// kiểm tra xem người dùng hợp lệ và tồn tại hay không
		User user = null;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;

	}

	@Override
	public void deleteRoleUser(String id) {
		currentSession().createQuery("delete from Users_roles where user_id=:id").setParameter("id", id);
		
	}

	
	
}