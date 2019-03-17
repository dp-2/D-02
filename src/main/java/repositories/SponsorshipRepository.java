
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	//query a.1
	@Query("select 1.0*count(s)/(select count(ss) from Sponsorship ss ) from Sponsorship s where s.active=true")
	Double ratioActiveSponsorship();

	//query a.2.1
	@Query("select avg(1.0 * (select count(*) from Sponsorship s where s.sponsor = sp.id)) from Sponsor sp")
	Double avgSponsorshipBySponsor();
	//query a.2.2
	@Query("select min(1.0 * (select count(*) from Sponsorship s where s.sponsor = sp.id)) from Sponsor sp")
	Double minSponsorshipBySponsor();
	//query a.2.3
	@Query("select max(1.0 * (select count(*) from Sponsorship s where s.sponsor = sp.id)) from Sponsor sp")
	Double maxSponsorshipBySponsor();
	//query a.2.4
	@Query("select stddev(1.0 * (select count(*) from Sponsorship s where s.sponsor = sp.id)) from Sponsor sp")
	Double stddevSponsorshipBySponsor();

	//query a.3
	@Query("select s.sponsor.userAccount.username from Sponsorship s where s.active = true group by s.sponsor.id order by count(s) desc")
	public List<String> top5Sponsors();

	//Other queries-----------------------------------------------------------------

	@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	List<Sponsorship> findSponsorshipsBySponsorId(Integer sponsorId);

	@Query("select s from Sponsorship s where s.creditCard.id = ?1")
	Sponsorship findSponsorshipByCreditCardId(Integer creditCardId);

	@Query("select s from Sponsorship s where s.parade.id = ?1")
	Sponsorship findSponsorshipByParadeId(Integer paradeId);

}
