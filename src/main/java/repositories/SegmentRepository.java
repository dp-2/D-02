
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Segment;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Integer> {

	@Query("select s from Segment s where ((s.timeOrigin < ?1 and s.timeDestination > ?1) or (s.timeOrigin < ?2 and s.timeDestination > ?2)) and s.path.id = ?3 ")
	Collection<Segment> isTimeRangeOccupied(Date start, Date end, Integer pathId);

}
