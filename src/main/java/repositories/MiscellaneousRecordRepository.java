
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MiscellaneousRecord;

@Repository
public interface MiscellaneousRecordRepository extends JpaRepository<MiscellaneousRecord, Integer> {

	@Query("select m from MiscellaneousRecord m where m.history.id = ?1")
	Collection<MiscellaneousRecord> findMiscellaneousRecordsByHistoryId(Integer historyId);

	//C1-Miscellaneous

	@Query("select avg(1.0*(select count(p.history) from MiscellaneousRecord p where p.history.id= h.id) ) from History h")
	Double avgQueryC1();

	@Query("select max(1.0*(select count(p.history) from MiscellaneousRecord p where p.history.id= h.id) ) from History h")
	Double maxQueryC1();

	@Query("select min(1.0*(select count(p.history) from MiscellaneousRecord p where p.history.id= h.id) ) from History h")
	Double minQueryC1();

	@Query("select stddev(1.0*(select count(p.history) from MiscellaneousRecord p where p.history.id= h.id) ) from History h")
	Double stddevQueryC1();
}
