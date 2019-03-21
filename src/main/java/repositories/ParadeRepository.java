
package repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Chapter;
import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where p.ffinal = true and p.momentOrganised > CURRENT_DATE")
	List<Parade> findParadesFinal();

	@Query("select p from Parade p where p.status = 'ACCEPTED' and p.momentOrganised > CURRENT_DATE and p.ffinal=true")
	List<Parade> findParadesAccepted();

	@Query("select p from Parade p where p.ffinal = true and p.momentOrganised > CURRENT_DATE and p.brotherhood.id = ?1")
	List<Parade> findParadesFinalByBrotherhoodId(int brotherhoodId);

	@Query("select p from Parade p where p.brotherhood.id = ?1")
	List<Parade> findParadesByBrotherhoodId(int brotherhoodId);

	@Query("select p from Parade p where p.ffinal = true and p.momentOrganised > CURRENT_DATE and p.brotherhood.id = ( select e.brotherhood.id from Enroll e where e.member.id = ?1 ))")
	List<Parade> findParadeOfMember(int memberId);

	@Query("select p from Parade p where p.ffinal = true and p.momentOrganised > CURRENT_DATE and p.brotherhood.id = ( select e.brotherhood.id from Enroll e where e.member.id = ?1 and e.status='APPROVED' ))")
	List<Parade> findParadeOfMemberAPPROVED(int memberId);

	//The parades that are going to be organised in 30 days or less.
	@Query("select p from Parade p where p.momentOrganised BETWEEN CURRENT_DATE and :currentDayPlus30Days")
	List<Parade> findParadesIn30Days(@Param("currentDayPlus30Days") Date date);

	@Query("select p from Parade p, DFloat f where p.brotherhood.id = f.brotherhood.id and p in f.parades and f.id = ?1")
	List<Parade> findParadesForRemove(Integer dfloatId);

	@Query("select p from Parade p, DFloat f where p.brotherhood.id = f.brotherhood.id and p not in f.parades and f.id = ?1")
	List<Parade> findParadesForAdd(Integer dfloatId);

	@Query("select 1.0*count(p)/(select count(pp) from Parade pp where pp.ffinal=false) from Parade p where p.ffinal=true")
	Double ratioParadesDraftVsParadesFinal();

	@Query("select 1.0*count(p)/(select count(pp) from Parade pp) from Parade p where p.ffinal=true group by p.status")
	List<Double> ratioParadeFinalByStatus();

	@Query("select s.banner from Sponsorship s where (s.parade.id = ?1 and s.active=1) order by rand()")
	List<String> findSponsorshipByParadeId(final int paradeId);

	@Query("select a.chapter from Area a where a.brotherhood = (select p.brotherhood.id from Parade p where p.id = ?1)")
	Chapter findChapterByParadeId(final int paradeId);
}
