
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.Parade;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	//	@Query("select a.brotherhood from Area a where a.chapter.id = ?1")
	//	Brotherhood findBrotherhoodByChapterArea(int chapterId);

	@Query("select c from Chapter c where c.id = ?1")
	Chapter findChapterByUserAcountId(int userAccountId);

	@Query("select c.area from Chapter c where c.id = ?1")
	List<Area> findAreaByChapterId(int chapterId);

	@Query("select c.area.brotherhood from Chapter c where c.id = ?1")
	List<Brotherhood> findBrotherhoodByChapterId(int chapterId);

	@Query("select c.area.brotherhood.parade from Chapter c where c.id = ?1")
	List<Parade> findParadeByChapterId(int chapterId);
}
