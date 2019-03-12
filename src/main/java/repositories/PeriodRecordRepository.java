
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PeriodRecord;

@Repository
public interface PeriodRecordRepository extends JpaRepository<PeriodRecord, Integer> {

	@Query("select p from PeriodRecord p where p.history.id = ?1")
	Collection<PeriodRecord> findLegalRecordsByHistoryId(Integer historyId);
}
