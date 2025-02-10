package vttp2023.batch3.assessment.paf.bookings.repositories;

public class SqlQueries {
    public final static String SQL_GET_VACANCY = """
            select * from acc_occupancy ao 
            where ao.acc_id = ?
            """;
    
    public final static String SQL_CREATE_RESERVATION = """
            insert into reservations (
                resv_id, name_, email, acc_id, arrival_date, duration
            )
            values (
                ?, ?, ?, ?, ?, ?
            )
            """;

    public final static String SQL_UPDATE_VACANCY = """
            update acc_occupancy ao
            set vacancy = ?
            where acc_id = ?
            """;
}
