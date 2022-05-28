package hotel.dao;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hotel.entity.Reservation;

@Repository
public class ReservationDaoImpl implements ReservationDao {
	
	// truy cập và gửi dữ liệu đến và đi từ cơ sở dữ liệu

	@Autowired
	private EntityManager entityManager;

	// truy cập tất cả các đặt chỗ cho người dùng đã đăng nhập từ cơ sở dữ liệu
	@Override
	public Collection<Reservation> getReservationsByUserId(int userId) {
		
		// tạo truy vấn  để nhận danh sách đặt chỗ
		Query<Reservation> query = currentSession().createQuery("from Reservation where reservation_user_id=:userId",
				Reservation.class);
		query.setParameter("userId", userId);

		return query.getResultList();
	}
	
	// truy xuất đặt chỗ cụ thể theo id 
	@Override
	public Reservation getReservationForLoggedUserById(int resId) {
		
		Query<Reservation> query = currentSession().createQuery("from Reservation where reservation_id=:resId",
				Reservation.class);
		query.setParameter("resId", resId);

		return query.getSingleResult();
	}

	// lưu hoặc cập nhật đặt chỗ trong cơ sở dữ liệu
	@Override
	public void saveOrUpdateReservation(Reservation reservation) {
		currentSession().saveOrUpdate(reservation);
	}

	// xóa đặt chỗ được lưu trữ trong cơ sở dữ liệu
	@Override
	public void deleteReservation(Reservation reservation) {
		currentSession().delete(reservation);
	}

	private Session currentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public Collection<Reservation> getReservations() {
		Query<Reservation> query = currentSession().createQuery("from Reservation",
				Reservation.class);
		

		return query.getResultList();
	}

}
