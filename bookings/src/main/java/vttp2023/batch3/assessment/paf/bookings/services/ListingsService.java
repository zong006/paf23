package vttp2023.batch3.assessment.paf.bookings.services;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp2023.batch3.assessment.paf.bookings.models.Booking;
import vttp2023.batch3.assessment.paf.bookings.models.Search;
import vttp2023.batch3.assessment.paf.bookings.repositories.ListingsRepository;

@Service
public class ListingsService {
	
	@Autowired
	private ListingsRepository listingsRepository;

	//TODO: Task 2
	public List<String> getCountries(){
		return listingsRepository.getCountryList();
	}
	
	//TODO: Task 3
	public List<Document> getSearchResults(Search search){
		return listingsRepository.getSearchResults(search);
	}

	//TODO: Task 4
	public Document getListingDetails(String id){
		Document result =  listingsRepository.getListingDetails(id).get(0);

		@SuppressWarnings("unchecked")
		List<String> amenitiesList = (List<String>) result.get("amenities");
		StringBuilder amenitiesStringBuilder = new StringBuilder();
		for (String a : amenitiesList){
			amenitiesStringBuilder.append(a).append(", ");
		}
		String amenities = amenitiesStringBuilder.toString();
		amenities = amenities.substring(0, amenities.length()-2);
		
		result.put("amenities", amenities);
		
		return result;
	}

	//TODO: Task 5
	@Transactional
	public Document generateReservation(Booking booking, String accId){
		int vacancy = getVacancy(accId);
		int minNights = (int) listingsRepository.getMinimumNights(accId).get("minimum_nights");
		Document response = new Document();

		if (vacancy >= booking.getDuration()){

			if (booking.getDuration()>=minNights){
				String resvId = UUID.randomUUID().toString().substring(0,8);
			
				listingsRepository.createReservation(booking, resvId, accId);
				listingsRepository.updateVacancy(booking, accId);

				response.put("reservationSuccess", true);
				response.put("resvId", resvId);
			}
			else{
				// response.put("reservtionSuccess", false);
				response.put("durationError", "Booking duration does not meet minimun number of nights.");
			}
			
		}
		else{
			response.put("vacancyError", "Not enough vacancy in the listing: " + accId);
			// response.put("reservtionSuccess", false);
		}
		
		return response;
	}

	private int getVacancy(String accId){
		return listingsRepository.getVacancy(accId);
	}

}
