package hotel.services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotel.entity.Reservation;
import hotel.repository.ReservationRep;
import hotel.temp.CurrentReservation;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	
	private UserService userService;

	private ReservationRep reservationRepository;
	
	@Autowired
	public ReservationServiceImpl( UserService userService, ReservationRep reservationRepository) {
		this.userService = userService;
		this.reservationRepository = reservationRepository;
	}
	
	// nhận đặt chỗ cho người dùng đã đăng nhập
	@Override
	@Transactional
	public Reservation getReservationForLoggedUserById(int resId) {
		
		return reservationRepository.findById(resId);
	}

	// nhận tất cả các yêu cầu đặt chỗ cho người dùng 
	@Override
	@Transactional
	public Collection<Reservation> getReservationsForLoggedUser() {
		return reservationRepository.findAllByUserId((userService.getLoggedUserId()));
	}


	@Override
	@Transactional
	public void saveOrUpdateReservation(CurrentReservation currentReservation) {
		Reservation reservation = new Reservation();

		// nhận được id người dùng bằng cách sử dụng dịch vụ người dùng
		reservation.setUserId(userService.getLoggedUserId());

		reservation.setArrivalDate(currentReservation.getArrivalDate());
		reservation.setOpenBuffet(currentReservation.getOpenBuffet());
		reservation.setStayDays(currentReservation.getStayPeriod());
		reservation.setChildren(currentReservation.getChildren());
		reservation.setPersons(currentReservation.getPersons());
		reservation.setPrice(currentReservation.getPrice());
		reservation.setRooms(currentReservation.getRooms());
		reservation.setRoom(currentReservation.getRoom());
		reservation.setId(currentReservation.getId());

		reservationRepository.save(reservation);
	}
	
	// chuyển dữ liệu giữa lớp Đặt chỗ và Đặt chỗ tạm thời để cập nhật yêu cầu
	@Override
	public CurrentReservation reservationToCurrentReservationById(int resId) {
		Reservation reservation = getReservationForLoggedUserById(resId);
		CurrentReservation currentReservation = new CurrentReservation();
		
		currentReservation.setArrivalDate(reservation.getArrivalDate());
		currentReservation.setOpenBuffet(reservation.getOpenBuffet());
		currentReservation.setStayPeriod(reservation.getStayDays());
		currentReservation.setChildren(reservation.getChildren());
		currentReservation.setPersons(reservation.getPersons());
		currentReservation.setUsertId(reservation.getUserId());
		currentReservation.setRooms(reservation.getRooms());
		currentReservation.setPrice(reservation.getPrice());
		currentReservation.setRoom(reservation.getRoom());
		currentReservation.setId(reservation.getId());
		
		return currentReservation;
	}
	
	// xóa đặt chỗ
	@Override
	@Transactional
	public void deleteReservation(int resId) {
		
		reservationRepository.deleteById(resId);
	}
}
