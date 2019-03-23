
package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import security.LoginService;
import domain.CreditCard;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	// Managed repository ------------------------------------------------------------------
	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private CreditCardService		creditCardService;


	//Constructor----------------------------------------------------------------------------

	public SponsorshipService() {
		super();
	}
	// Simple CRUD methods -------------------------------------------------------------------
	public Sponsorship create(final int SponsorId) {
		final Sponsorship sponsorship = new Sponsorship();
		sponsorship.setSponsor(this.sponsorService.findOne(SponsorId));
		return sponsorship;
	}

	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> sponsorships;

		sponsorships = this.sponsorshipRepository.findAll();
		Assert.notNull(sponsorships);

		return sponsorships;
	}

	public Collection<Sponsorship> findAllByPrincipal() {
		Collection<Sponsorship> sponsorships;
		final Integer sponsorId = this.sponsorService.findSponsorByUserAcountId(LoginService.getPrincipal().getId()).getId();
		sponsorships = this.findSponsorshipsBySponsorId(sponsorId);
		Assert.notNull(sponsorships);

		return sponsorships;
	}

	public Sponsorship findOne(final int SponsorshipId) {
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipRepository.findOne(SponsorshipId);
		Assert.notNull(sponsorship);

		return sponsorship;
	}

	public Sponsorship save(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		this.checkPrincipal(sponsorship);
		Sponsorship result = sponsorship;
		final CreditCard c = this.creditCardService.save(sponsorship.getCreditCard());
		result.setCreditCard(c);
		result.setSponsor(sponsorship.getSponsor());
		result = this.sponsorshipRepository.save(sponsorship);

		return result;
	}

	public Sponsorship save1(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		this.checkPrincipal(sponsorship);
		Sponsorship result = sponsorship;

		result.setCreditCard(sponsorship.getCreditCard());
		result.setSponsor(sponsorship.getSponsor());
		result = this.sponsorshipRepository.save(sponsorship);

		return result;
	}

	public void deActive(final Sponsorship sponsorship) {

		Assert.notNull(sponsorship);
		this.checkPrincipal(sponsorship);

		final Sponsorship s = this.sponsorshipRepository.findOne(sponsorship.getId());
		s.setActive(false);
		this.sponsorshipRepository.save(s);

	}

	public void delete(final Sponsorship sponsorship) {

		this.sponsorshipRepository.delete(sponsorship);
		//	this.creditCardService.delete(sponsorship.getCreditCard());
	}

	// Other bussines methods ------------------------------ (Otras reglas de negocio, como por ejemplo findRegisteredUser())
	public Boolean checkPrincipal(final Sponsorship sponsorship) {
		final Sponsor sponsor = sponsorship.getSponsor();
		Assert.isTrue(sponsor.getUserAccount().equals(LoginService.getPrincipal()));
		return true;
	}
	public List<Sponsorship> findSponsorshipsBySponsorId(final Integer sponsorId) {
		return this.sponsorshipRepository.findSponsorshipsBySponsorId(sponsorId);
	}
	public Sponsorship findSponsorshipByCreditCardId(final Integer creditCardId) {
		return this.sponsorshipRepository.findSponsorshipByCreditCardId(creditCardId);
	}
	public Sponsorship findSponsorshipByParadeId(final Integer paradeId) {
		return this.sponsorshipRepository.findSponsorshipByParadeId(paradeId);
	}

	public Double ratioActiveSponsorship() {
		return this.sponsorshipRepository.ratioActiveSponsorship();
	}
	public Double avgSponsorshipBySponsor() {
		return this.sponsorshipRepository.avgSponsorshipBySponsor();
	}
	public Double minSponsorshipBySponsor() {
		return this.sponsorshipRepository.minSponsorshipBySponsor();
	}
	public Double maxSponsorshipBySponsor() {
		return this.sponsorshipRepository.maxSponsorshipBySponsor();
	}
	public Double stddevSponsorshipBySponsor() {
		return this.sponsorshipRepository.ratioActiveSponsorship();
	}
	public List<String> top5Sponsors() {
		return this.sponsorshipRepository.top5Sponsors();
	}
}
