
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Sponsor;
import repositories.SponsorRepository;

@Service
@Transactional
public class SponsorService {

	//Repository-------------------------------------------------------------------

	@Autowired
	private SponsorRepository sponsorRepository;

	//Methods----------------------------------------------------------------------


	public Sponsor findSponsorByUserAcountId(final int userAccountId) {
		return this.sponsorRepository.findSponsorByUserAcountId(userAccountId);
	}

	public Sponsor findOne(final Integer id) {
		return this.sponsorRepository.findOne(id);
	}

	public Sponsor findSponsorByCreditCardId(final int creditCardId) {
		return this.sponsorRepository.findSponsorByCreditCardId(creditCardId);
	}

}
