
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.LinkRecord;

@Repository
public interface LinkRecordRepository extends JpaRepository<LinkRecord, Integer> {

	@Query("select l from LinkRecord l where l.history.id = ?1")
	Collection<LinkRecord> findLinkRecordsByHistoryId(Integer historyId);

	//C1-Link

	@Query("select avg(1.0*(select count(p.history) from LinkRecord p where p.history.id= h.id) ) from History h")
	Double avgQueryC1();

	@Query("select max(1.0*(select count(p.history) from LinkRecord p where p.history.id= h.id) ) from History h")
	Double maxQueryC1();

	@Query("select min(1.0*(select count(p.history) from LinkRecord p where p.history.id= h.id) ) from History h")
	Double minQueryC1();

	@Query("select stddev(1.0*(select count(p.history) from LinkRecord p where p.history.id= h.id) ) from History h")
	Double stddevQueryC1();
}
