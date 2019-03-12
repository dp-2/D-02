
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;
import domain.Brotherhood;
import domain.Parade;
import domain.Proclaim;

@Repository
public interface ProclaimRepository extends JpaRepository<Proclaim, Integer> {

	@Query("select a from Area a where a.chapter.id = ?1")
	Collection<Area> findAreaByChapter(int chapterId);

	@Query("select a.brotherhood from Area a where a.chapter.id = ?1")
	Collection<Brotherhood> findBrotherhoodByChapter(int chapterId);

	@Query("select a.brotherhood.parade from Area a where a.chapter.id = ?1")
	Collection<Parade> findParadeByChapter(int chapterId);

	@Query("select p from Proclaim p where p.chapter.id = ?1")
	Collection<Proclaim> findProclaimByChapter(int chapterId);
}
