
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.LegalRecord;

@Repository
public interface LegalRecordRepository extends JpaRepository<LegalRecord, Integer> {

	@Query("select l from LegalRecord l where l.history.id = ?1")
	Collection<LegalRecord> findLegalRecordsByHistoryId(Integer historyId);

	//C1-Legal

	@Query("select avg(1.0*(select count(p.history) from LegalRecord p where p.history.id= h.id) ) from History h")
	Double avgQueryC1();

	@Query("select max(1.0*(select count(p.history) from LegalRecord p where p.history.id= h.id) ) from History h")
	Double maxQueryC1();

	@Query("select min(1.0*(select count(p.history) from LegalRecord p where p.history.id= h.id) ) from History h")
	Double minQueryC1();

	@Query("select stddev(1.0*(select count(p.history) from LegalRecord p where p.history.id= h.id) ) from History h")
	Double stddevQueryC1();
}
