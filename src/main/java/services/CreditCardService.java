
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import security.LoginService;
import domain.CreditCard;
import domain.Sponsor;

@Service
@Transactional
public class CreditCardService {

	// Repository-----------------------------------------------
	@Autowired
	private CreditCardRepository	creditCardRepository;

	// Services-------------------------------------------------
	@Autowired
	private SponsorService			sponsorService;


	// Constructor----------------------------------------------
	public CreditCardService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public CreditCard create() {
		final CreditCard creditCard = new CreditCard();
		return creditCard;
	}

	public List<CreditCard> findAll() {
		return this.creditCardRepository.findAll();
	}

	public CreditCard findOne(final Integer creditCardId) {
		return this.creditCardRepository.findOne(creditCardId);
	}

	public CreditCard save(final CreditCard creditCard) {
		//	Assert.isTrue(this.isGood(creditCard), "errorCredit");
		Assert.notNull(creditCard);

		final CreditCard saved = this.creditCardRepository.save(creditCard);
		return saved;
	}

	public void delete(final CreditCard creditCard) {
		this.creditCardRepository.delete(creditCard);

	}
	// Other Methods--------------------------------------------
	public Boolean checkPrincipal(final CreditCard creditCard) {
		final Sponsor sponsor = this.sponsorService.findSponsorByCreditCardId(creditCard.getId());
		Assert.isTrue(sponsor.getUserAccount().equals(LoginService.getPrincipal()));
		return true;
	}

	public boolean isGood(final CreditCard creditCard) {
		boolean res = false;
		if (creditCard.getExpirationYear() >= LocalDate.now().getYear())
			if (creditCard.getExpirationMonth() >= LocalDate.now().getMonthOfYear())
				res = true;
		return res;
	}
}
