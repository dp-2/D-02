
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	@Query("select s from Sponsor s where s.userAccount.id =?1")
	Sponsor findSponsorByUserAcountId(int userAccountId);

	@Query("select s.sponsor from Sponsorship s where s.creditCard.id =?1")
	Sponsor findSponsorByCreditCardId(int creditCardId);
}
