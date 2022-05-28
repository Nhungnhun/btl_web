package hotel.controller;


import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hotel.dao.ReservationDao;
import hotel.dao.UserDao;
import hotel.entity.User;
import hotel.services.ReservationService;
import hotel.services.UserService;

import hotel.entity.Reservation;



@Controller
@RequestMapping("/")
public class AdminUserController {
	
	
	@ Autowired
	private UserService userService;
	private UserDao userDao;
	private ReservationDao reservationDao;
	private ReservationService reservationService;
	public AdminUserController(UserDao userDao, ReservationService reservationService, ReservationDao reservationDao, UserService userService) {
			this.userDao = userDao;
			this.reservationDao = reservationDao;
			this.reservationService = reservationService;
			this.userService = userService;
		}
	@GetMapping("/adminController")
	public String homePage() {
		
		return "loginAdmin";
	}
	@PostMapping("/adminController")
	public String check(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
		if(username.equals("admin")&&password.equals("admin")) {
			List<User> list = userDao.findAllUser();
			model.addAttribute("list",list);
			return "user";
			
		}
		
		return "loginAdmin";
		
		
}
	@GetMapping("/deleteUser")
	public String deleteUser(Model model,@RequestParam(value= "id") String id) {
		User user = userDao.findUserById(id);
		userDao.deleteRoleUser(id);
		int id1 = Integer.parseInt(id);
		userService.deleteUser(id1);
		List<User> list = userDao.findAllUser();
		model.addAttribute("list",list);
		return "user";
	}
	@GetMapping("/reservation")
	public String Reservation(Model model) {
		Collection<Reservation> list = reservationDao.getReservations();
		model.addAttribute("list",list);
		return "reservation";
	}
	@GetMapping("/deleteRe")
	public String deleteRe(Model model,@RequestParam(value= "id") int id) {
		reservationService.deleteReservation(id);
		Collection<Reservation> list = reservationDao.getReservations();
		model.addAttribute("list",list);
		return "reservation";
	}
	@GetMapping("/userAdminController")
	public String user(Model model) {
		List<User> list = userDao.findAllUser();
		model.addAttribute("list",list);
		return "user";
	}
}
