
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.InceptionRecord;

@Repository
public interface InceptionRecordRepository extends JpaRepository<InceptionRecord, Integer> {

	@Query("select i from InceptionRecord i where i.history.id = ?1")
	InceptionRecord findInceptionByHistoryId(int historyId);

	//C1-Inception

	@Query("select avg(1.0*(select count(p.history) from InceptionRecord p where p.history.id= h.id) ) from History h")
	Double avgQueryC1();

	@Query("select max(1.0*(select count(p.history) from InceptionRecord p where p.history.id= h.id) ) from History h")
	Double maxQueryC1();

	@Query("select min(1.0*(select count(p.history) from InceptionRecord p where p.history.id= h.id) ) from History h")
	Double minQueryC1();

	@Query("select stddev(1.0*(select count(p.history) from InceptionRecord p where p.history.id= h.id) ) from History h")
	Double stddevQueryC1();
}
