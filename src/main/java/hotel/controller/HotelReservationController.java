package hotel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hotel.services.ReservationService;
import hotel.services.UserService;
import hotel.temp.CurrentReservation;
import hotel.temp.CurrentUser;

@Controller
public class HotelReservationController {

	private UserService userService;

	private ReservationService reservationService;
	  
	 @ Autowired
	public HotelReservationController(UserService userService, ReservationService reservationService) {
		this.userService = userService;
		this.reservationService = reservationService;
	}

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@RequestMapping("/")
	public String homePage() {

		return "home-page";
	}


	@GetMapping("/login-form-page")
	public String loginPage(Model model) {

		// if user is already login, redirect to home
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			return "redirect:/";
		}
		
		// new user attribute for sign up page
		model.addAttribute("newUser", new CurrentUser());

		return "login";
	}

	// trang  đăng ký
	@PostMapping("/processRegistration")
	public String processRegistrationForm(@Valid @ModelAttribute("newUser") CurrentUser currentUser,
			BindingResult theBindingResult, Model model) {

		// check the database if user already exists
		if (userService.findUserByEmail(currentUser.getEmail()) != null) {
			model.addAttribute("newUser", new CurrentUser());
			model.addAttribute("registrationError", "Email already exists.");

			return "login";
		}

		// create user account
		userService.saveUser(currentUser);
		model.addAttribute("registrationSuccess", "registration Success.");

		return "redirect:/login-form-page";

	}

	// trang đặt phòng
	@GetMapping("/new-reservation")
	public String newReservation(Model model) {
		// reservation attribute
		model.addAttribute("newRes", new CurrentReservation());

		return "reservation-page";
	}

	// lưu đặt phòng
	@PostMapping("/proceed-reservation")
	public String proceedReservation(@Valid @ModelAttribute("newRes") CurrentReservation currentReservation,
			BindingResult theBindingResult, Model model) {
		
		// send reservation to services to save it in database
		reservationService.saveOrUpdateReservation(currentReservation);

		return "redirect:/your-reservations";
	}

	// phòng của người dùng
	@GetMapping("/your-reservations")
	public String reservationsList(Model model) {
		
		// danh sách đặt chỗ cho người dùng đã đăng nhập
		model.addAttribute("resList", reservationService.getReservationsForLoggedUser());

		return "your-reservations";
	}
	
	// cập nhật đặt chỗ
	@PostMapping("/reservation-update")
	public String updateReservation(@RequestParam("resId") int resId, Model model) {
		
		// cập nhật mới đặt trước lưu nó trong cơ sở dữ liệu
		model.addAttribute("newRes", reservationService.reservationToCurrentReservationById(resId));
		
		return "reservation-page";
	}
	

	// xóa đặt chỗ
	@PostMapping("/reservation-delete")
	public String deleteReservation(@RequestParam("resId") int resId) {
		
		// xóa đặt chỗ  khỏi cơ sở dữ liệu
		reservationService.deleteReservation(resId);
		
		return "redirect:/your-reservations";
	}
	
	// đăng xuất
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		
		// xử lý đăng xuất cho người dùng đã đăng nhập lại 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/login-form-page?logout";
	}
	
	

}