
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	//	@Query("select h from History h where h.Brotherhood.id = ?1")
	//	History findHistoryByBrotherhoodId(int brotherhoodId);

}
