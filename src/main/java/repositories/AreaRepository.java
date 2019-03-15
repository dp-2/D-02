
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	//QUERY B1

	@Query("select a from Area a where a.brotherhood.id = ?1")
	Area findAreaByBrotherhoodId(int brotherhoodId);

	//	@Query("(select count(a.brotherhood) from Area a)/(select count(*) from Area a)")  GUARDAR POR SI ACASO
	@Query("select avg(1.0 * (select count(*) from Area a where a.brotherhood = b.id)) from Brotherhood b")
	Double avgHermandadesPorArea();

	@Query("select min(1.0 * (select count(*) from Area a where a.brotherhood = b.id)) from Brotherhood b")
	Double minHermandadesPorArea();

	@Query("select max(1.0 * (select count(*) from Area a where a.brotherhood = b.id)) from Brotherhood b")
	Double maxHermandadesPorArea();

	@Query("select stddev(1.0 * (select count(*) from Area a where a.brotherhood = b.id)) from Brotherhood b")
	Double stddevHermandadesPorArea();

	@Query("select count(a.brotherhood) from Area a)/(select count(*) from Area a)")
	Double countHermandadesPorArea();

	//Ratios of areas not co-ordinated
	@Query("select 1.0*count(a)/(select count(aa) from Area aa) from Area a where a.chapter=null")
	Double ratioAreasNoCoordinated();

	@Query("select a from Area a where a.chapter.id = ?1")
	List<Area> findAreaByChapterId(int chapterId);

	//	@Query("select c.area.brotherhood from Chapter c where c.id = ?1")
	//	List<Brotherhood> findBrotherhoodByChapterId(int chapterId);
	//
	//	@Query("select c.area.brotherhood.parade from Chapter c where c.id = ?1")
	//	List<Parade> findParadeByChapterId(int chapterId);

}
