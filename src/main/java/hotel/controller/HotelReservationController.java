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

	// trang  ????ng k??
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

	// trang ?????t ph??ng
	@GetMapping("/new-reservation")
	public String newReservation(Model model) {
		// reservation attribute
		model.addAttribute("newRes", new CurrentReservation());

		return "reservation-page";
	}

	// l??u ?????t ph??ng
	@PostMapping("/proceed-reservation")
	public String proceedReservation(@Valid @ModelAttribute("newRes") CurrentReservation currentReservation,
			BindingResult theBindingResult, Model model) {
		
		// send reservation to services to save it in database
		reservationService.saveOrUpdateReservation(currentReservation);

		return "redirect:/your-reservations";
	}

	// ph??ng c???a ng?????i d??ng
	@GetMapping("/your-reservations")
	public String reservationsList(Model model) {
		
		// danh s??ch ?????t ch??? cho ng?????i d??ng ???? ????ng nh???p
		model.addAttribute("resList", reservationService.getReservationsForLoggedUser());

		return "your-reservations";
	}
	
	// c???p nh???t ?????t ch???
	@PostMapping("/reservation-update")
	public String updateReservation(@RequestParam("resId") int resId, Model model) {
		
		// c???p nh???t m???i ?????t tr?????c l??u n?? trong c?? s??? d??? li???u
		model.addAttribute("newRes", reservationService.reservationToCurrentReservationById(resId));
		
		return "reservation-page";
	}
	

	// x??a ?????t ch???
	@PostMapping("/reservation-delete")
	public String deleteReservation(@RequestParam("resId") int resId) {
		
		// x??a ?????t ch???  kh???i c?? s??? d??? li???u
		reservationService.deleteReservation(resId);
		
		return "redirect:/your-reservations";
	}
	
	// ????ng xu???t
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		
		// x??? l?? ????ng xu???t cho ng?????i d??ng ???? ????ng nh???p l???i 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/login-form-page?logout";
	}
	
	

}