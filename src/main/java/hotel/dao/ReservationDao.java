package hotel.dao;

import java.util.Collection;

import hotel.entity.Reservation;


public interface ReservationDao {
	
	public Reservation getReservationForLoggedUserById(int resId);

	public Collection<Reservation> getReservationsByUserId(int userId);
	public Collection<Reservation> getReservations();
	
	public void saveOrUpdateReservation(Reservation reservation);
	
	public void deleteReservation(Reservation reservation);

}
