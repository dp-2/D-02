
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	//Other queries-----------------------------------------------------------------

	@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	List<Sponsorship> findSponsorshipsBySponsorId(Integer sponsorId);

	@Query("select s from Sponsorship s where s.creditCard.id = ?1")
	Sponsorship findSponsorshipByCreditCardId(Integer creditCardId);

	@Query("select 1.0*count(s)/(select count(ss) from Sponsorship ss ) from Sponsorship s where s.isActive=true")
	Double ratioActiveSponsorship();

	@Query("select s from Sponsorship s where s.parade.id = ?1")
	Sponsorship findSponsorshipByParadeId(Integer paradeId);

}