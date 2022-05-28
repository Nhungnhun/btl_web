package hotel.services;

import java.util.Collection;

import hotel.entity.Reservation;
import hotel.temp.CurrentReservation;

//Mẫu dịch vụ để đặt chỗ
public interface ReservationService {
	
	public Reservation getReservationForLoggedUserById(int resId);

	public Collection<Reservation> getReservationsForLoggedUser();
	
	public void saveOrUpdateReservation(CurrentReservation currentReservation);
	
	public void deleteReservation(int resId);

	public CurrentReservation reservationToCurrentReservationById(int resId);

}