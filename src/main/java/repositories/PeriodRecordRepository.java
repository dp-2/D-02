
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PeriodRecord;

@Repository
public interface PeriodRecordRepository extends JpaRepository<PeriodRecord, Integer> {

	@Query("select p from PeriodRecord p where p.history.id = ?1")
	Collection<PeriodRecord> findPeriodRecordsByHistoryId(Integer historyId);

	//Queries Dashboard PARADE-------------------------------------------------------------

	//C1-Period

	@Query("select avg(1.0*(select count(p.history) from PeriodRecord p where p.history.id= h.id) ) from History h")
	Double avgQueryC1();

	@Query("select max(1.0*(select count(p.history) from PeriodRecord p where p.history.id= h.id) ) from History h")
	Double maxQueryC1();

	@Query("select min(1.0*(select count(p.history) from PeriodRecord p where p.history.id= h.id) ) from History h")
	Double minQueryC1();

	@Query("select stddev(1.0*(select count(p.history) from PeriodRecord p where p.history.id= h.id) ) from History h")
	Double stddevQueryC1();

}
