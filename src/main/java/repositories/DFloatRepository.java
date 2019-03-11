
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DFloat;

@Repository
public interface DFloatRepository extends JpaRepository<DFloat, Integer> {

	@Query("select c from DFloat c where c.brotherhood.id = ?1")
	Collection<DFloat> SearchDFloatsByBrotherhood(Integer brotherhoodId);

	@Query("select c from DFloat c where c.brotherhood IS NULL")
	Collection<DFloat> SearchDFloatsWithoutBrotherhood();

	@Query("select distinct f from DFloat f, Parade p where p.id = ?1 and p not member of f.parades and f.brotherhood.id = ?2 and p.brotherhood.id = ?2")
	Collection<DFloat> searchFloatNotInParadeByIdByActorId(Integer paradeId, Integer actorId);

	// select distinct f from DFloat f, Parade p where p.id = 2923 and p not member of f.parades and f.brotherhood.id = 2861 and p.brotherhood.id = 2861

	@Query("select distinct f from DFloat f, Parade p where p.id = ?1 and p member of f.parades and f.brotherhood.id = ?2 and p.brotherhood.id = ?2")
	Collection<DFloat> searchFloatInParadeByIdByActorId(Integer paradeId, Integer actorId);

	// select distinct f from DFloat f, Parade p where p.id = 2923 and p member of f.parades and f.brotherhood.id = 2861 and p.brotherhood.id = 2861

	//	@Query("select d from DFloat d where d.parade.id =?1")
	//	List<DFloat> findDFloatsByParadeId(int paradeId);
}
