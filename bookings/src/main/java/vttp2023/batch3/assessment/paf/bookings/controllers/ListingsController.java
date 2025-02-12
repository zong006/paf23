package vttp2023.batch3.assessment.paf.bookings.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp2023.batch3.assessment.paf.bookings.models.Booking;
import vttp2023.batch3.assessment.paf.bookings.models.Search;
import vttp2023.batch3.assessment.paf.bookings.services.ListingsService;

@Controller
public class ListingsController {

	@Autowired
	private ListingsService listingsService;

	//TODO: Task 2
	@GetMapping("/")
	public String showView1(Model model, HttpSession session){
		session.setAttribute("hasNoResults", true);
		List<String> countries = listingsService.getCountries(); // make commaand line runner if possible
		
		model.addAttribute("search", new Search());
		model.addAttribute("countries", countries);

		return "view1";
	}

	//TODO: Task 3

	@GetMapping("/search")
	public String submitForm(@Valid @ModelAttribute(value="search") Search search, 
							BindingResult bindingResult,
							Model model,
							HttpSession session){

		if ((boolean) session.getAttribute("hasNoResults")){
			if (bindingResult.hasErrors() ){
				List<String> countries = listingsService.getCountries();
				model.addAttribute("countries", countries);
				return "view1";
			}
			if (search.getMaxPrice() < search.getMinPrice()){
				// System.out.println("max less than min");
				String errorMessage = "maximum price must be more than the minimum price..";
				model.addAttribute("errorMessage", errorMessage);
				return "errorPage";
			}
			List<Document> results = listingsService.getSearchResults(search);
			
			session.setAttribute("results", results);
			session.setAttribute("country", search.getCountry());
			session.setAttribute("hasNoResults", false);
		}
		@SuppressWarnings("unchecked")
		List<Document> results = (List<Document>) session.getAttribute("results");
		String country = (String) session.getAttribute("country");

		model.addAttribute("results", results);
		model.addAttribute("country", country);	
		session.setAttribute("hasNoResults", false);

		return "view2";
	}	

	//TODO: Task 4
	
	@GetMapping("/details")
	public String getListingDetails(@RequestParam(required = false) String _id, Model model, HttpSession session){
		String accId;
		// System.out.println(_id);
		
		if (_id!=null){
			accId = _id;
			// System.out.println("not null: "+accId);
			session.setAttribute("accId", _id);
		}
		else {
			accId = (String) session.getAttribute("accId");
		}
		
		Document result = listingsService.getListingDetails(accId);
		session.setAttribute("result", result);
		
		model.addAttribute("booking", new Booking());
		
		
		
		return "view3";
	}

	//TODO: Task 5

	@PostMapping("/booking")
	public String createBooking(@ModelAttribute Booking booking, HttpSession session, Model model){
		
		String accId = (String) session.getAttribute("accId");
		String resvId = listingsService.generateReservation(booking, accId);

		model.addAttribute("resvId", resvId);
		return "view4";	
	}
}
