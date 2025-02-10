package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.assessment.paf.bookings.models.Booking;
import vttp2023.batch3.assessment.paf.bookings.models.Search;
@Repository
public class ListingsRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private JdbcTemplate sqlTemplate;

	//TODO: Task 2
	/*
		db.getCollection("listings").distinct("address.country")
	*/ 

	public List<String> getCountryList(){
		List<String> countries = mongoTemplate.findDistinct(new Query(), "address.country", "listings", String.class);
		return countries;
	}

	
	//TODO: Task 3

	/*
		db.listings.find(
			{
				$and : [
					{
						"address.country":{
							$regex:"Australia",
							$options:"i"
						}
					},
					{
					"accommodates":2
					},
					{
						"price" : {$gte : 10, $lte:90}
					}
				]
			},
			{
				"_id":1, <<--- not in the minimum, but this field is included for task 4 to facilitate querying for details
				"name":1,
				"price":1,
				"images.picture_url":1
			}
		).sort({
			"price":1
		})
	*/ 

	public List<Document> getSearchResults(Search search){

		Criteria accommodateCriteria = Criteria.where("accommodates").is(search.getNumPerson());
		Criteria minPricCriteria = Criteria.where("price").gte(search.getMinPrice());
		Criteria maxPricCriteria = Criteria.where("price").lte(search.getMaxPrice());
		List<Criteria> criterias = Arrays.asList(accommodateCriteria, minPricCriteria, maxPricCriteria);

		Query query = Query.query(Criteria.where("address.country").regex(search.getCountry(),"i")
											.andOperator(criterias)
								)
								.with(Sort.by(Direction.DESC, "price"));
		query.fields().include("name", "price", "images.picture_url");
		List<Document> results = mongoTemplate.find(query, Document.class, "listings");

		return results;
	}

	//TODO: Task 4
	
	/*
		db.getCollection("listings").find(
			{"_id":"19348489"},
			{
				"description":1,
				"address.street":1,
				"address.suburb":1,
				"address.country":1,
				"amenities":1,
				images.picture_url
				
			}
		)

	*/ 

	public List<Document> getListingDetails(String id){
		Query query = Query.query(Criteria.where("_id").is(id));
		query.fields().include("description", 
									"address.street",
									"address.suburb",
									"address.country",
									"amenities",
									"images.picture_url");
		List<Document> results = mongoTemplate.find(query, Document.class, "listings");
		return results;
	}

	//TODO: Task 5

	public int getVacancy(String accId){
		SqlRowSet rs = sqlTemplate.queryForRowSet(SqlQueries.SQL_GET_VACANCY, accId);
		while (rs.next()) {
			return rs.getInt("vacancy");
			
		}
		return -1;
	}

	/*
		<<---- this is for checking if the booking duration meets the required minimum nights stated in the listing ---->>

		db.getCollection("listings").find(
			{"_id":"394428"},
			{"minimum_nights":1}
		)
	*/ 
	public Document getMinimumNights(String accId){
		Query query = Query.query(Criteria.where("_id").is(accId));
		query.fields().include("minimum_nights");
		return mongoTemplate.find(query, Document.class, "listings").get(0);
	}

	public boolean createReservation(Booking booking, String resvId, String accId){
		int rowsUpdated = sqlTemplate.update(SqlQueries.SQL_CREATE_RESERVATION,
														resvId,
														booking.getName(),
														booking.getEmail(),
														accId,
														booking.getBookingDate(),
														booking.getDuration());
		return rowsUpdated>0;
	}

	public boolean updateVacancy(Booking booking, String accId){
		int newVacancy = getVacancy(accId) - booking.getDuration();
		int rowsUpdated = sqlTemplate.update(SqlQueries.SQL_UPDATE_VACANCY, newVacancy, accId);

		return rowsUpdated>0;
	}



}
