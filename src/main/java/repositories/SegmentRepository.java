
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Segment;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Integer> {

	@Query("select s from Segment s where s.previousSegment.id = ?1 and s.path.id = ?2")
	Segment findNextSegment(Integer segmentId, Integer pathId);

}
