
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.March;
import domain.Member;

@Repository
public interface MarchRepository extends JpaRepository<March, Integer> {

	@Query("select a from March a where a.member.id = ?1")
	Collection<March> findMarchsByMember(int memberId);

	@Query("select a from March a where a.member.id = ?1 and a.status = 'APPROVED'")
	Collection<March> findMarchsByMemberAPPROVED(int memberId);

	@Query("select a from March a where a.parade.id = ?1")
	Collection<March> findMarchsByParade(int paradeId);

	@Query("select count(m) from March m where m.parade.id = ?1 and m.member.id = ?2")
	Double findMatchByParadeidAndMemberid(int paradeId, int memberId);

	//Queries Dashboard-----------------------------------------------------------

	//The ratio of requests to march in a parade, grouped by their status.

	@Query("select 1.0*count(m)/(select count(mm) from March mm ) from March m where m.status='APPROVED' and m.parade.id=?1 group by m.status")
	List<Double> ratioAcceptedMarchByParade(int paradeId);

	//The listing of members who have got at least 10% the maximum number of request to march accepted.

	@Query("select m from Member m where (((select count(mm) from March mm where mm.member.id=m.id and mm.status = 'APPROVED' )*1.0 /(select count(mmm) from March mmm ))>=(select count(mmmm) from March mmmm ))")
	List<Member> members10PerMarchAccepted();

	@Query("select (count(m)*1.0)/(select count(mm) from March mm) from March m group by m.status")
	public List<Double> queryC6c();

}
