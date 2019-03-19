
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	//	@Query("select a.brotherhood from Area a where a.chapter.id = ?1")
	//	Brotherhood findBrotherhoodByChapterArea(int chapterId);

	@Query("select c from Chapter c where c.id = ?1")
	Chapter findChapterByUserAcountId(int userAccountId);

	//Queries Dashboard----------------------------------------------------------------------------

	//B2

}
