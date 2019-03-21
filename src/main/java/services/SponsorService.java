
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import repositories.SponsorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorForm;

@Service
@Transactional
public class SponsorService {

	//Repository-------------------------------------------------------------------

	@Autowired
	private SponsorRepository		sponsorRepository;

	//Service---------------------------------------------------------------------

	@Autowired(required = false)
	private Validator				validator;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MessageSource			messageSource;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SocialProfileService	socialProfileService;


	//Methods----------------------------------------------------------------------

	public Sponsor create() {
		Sponsor result;
		result = new Sponsor();
		//establezco ya su tipo de userAccount porque no va a cambiar
		result.setUserAccount(new UserAccount());
		final Authority authority = new Authority();
		authority.setAuthority(Authority.SPONSOR);
		result.getUserAccount().addAuthority(authority);
		//los atributos que no pueden estar vacíos
		result.setBanned(false);
		result.setSpammer(false);
		result.setScore(0.0);
		return result;
	}

	public Sponsor findSponsorByUserAcountId(final int userAccountId) {
		return this.sponsorRepository.findSponsorByUserAcountId(userAccountId);
	}

	public Sponsor findOne(final Integer id) {
		return this.sponsorRepository.findOne(id);
	}

	public Sponsor findSponsorByCreditCardId(final int creditCardId) {
		return this.sponsorRepository.findSponsorByCreditCardId(creditCardId);
	}

	public void flush() {
		this.sponsorRepository.flush();
	}

	/*
	 * public Sponsor save(final Sponsor sponsor) {
	 * //comprobamos que el customer que nos pasan no sea nulo
	 * Assert.notNull(sponsor);
	 * Boolean isCreating = null;
	 * 
	 * Assert.isTrue(!(sponsor.getEmail().endsWith("@") || sponsor.getEmail().endsWith("@>")));
	 * 
	 * if (sponsor.getId() == 0) {
	 * isCreating = true;
	 * sponsor.setSpammer(false);
	 * 
	 * //comprobamos que ningún actor resté autenticado (ya que ningun actor puede crear los customers)
	 * //this.serviceUtils.checkNoActor();
	 * 
	 * } else {
	 * isCreating = false;
	 * //comprobamos que su id no sea negativa por motivos de seguridad
	 * this.serviceUtils.checkIdSave(sponsor);
	 * 
	 * //este customer será el que está en la base de datos para usarlo si estamos ante un customer que ya existe
	 * Sponsor sponsorDB;
	 * Assert.isTrue(sponsor.getId() > 0);
	 * 
	 * //cogemos el customer de la base de datos
	 * sponsorDB = this.sponsorRepository.findOne(sponsor.getId());
	 * 
	 * sponsor.setSpammer(sponsorDB.getSpammer());
	 * sponsor.setUserAccount(sponsorDB.getUserAccount());
	 * 
	 * //Comprobamos que el actor sea un Sponsor
	 * final String[] auths = new String[] {
	 * "SPONSOR", "ADMIN"
	 * };
	 * this.serviceUtils.checkAnyAuthority(auths);
	 * //esto es para ver si el actor que está logueado es el mismo que se está editando
	 * Assert.isTrue(this.serviceUtils.checkActorBoolean(sponsor) || this.serviceUtils.checkAuthorityBoolean("ADMIN"));
	 * 
	 * }
	 * 
	 * if ((!sponsor.getPhone().startsWith("+")) && StringUtils.isNumeric(sponsor.getPhone()) && sponsor.getPhone().length() > 3) {
	 * final Configuration confs = this.configurationService.findOne();
	 * sponsor.setPhone(confs.getCountryCode() + sponsor.getPhone());
	 * }
	 * Sponsor res;
	 * //le meto al resultado final el customer que he ido modificando anteriormente
	 * res = this.sponsorRepository.save(sponsor);
	 * this.flush();
	 * if (isCreating)
	 * this.boxService.createIsSystemBoxs(res);
	 * return res;
	 * }
	 */
	public Sponsor save(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		final Sponsor saved = this.sponsorRepository.save(sponsor);

		return saved;
	}

	public SponsorForm construct(final Sponsor s) {
		final SponsorForm res = new SponsorForm();
		res.setEmail(s.getEmail());
		res.setName(s.getName());
		res.setPhone(s.getPhone());
		res.setPhoto(s.getPhoto());
		res.setSurname(s.getSurname());
		res.setUsername(s.getUserAccount().getUsername());
		res.setId(s.getId());
		res.setVersion(s.getVersion());
		res.setMiddleName(s.getMiddleName());
		res.setAddress(s.getAddress());
		return res;
	}

	public Sponsor deconstruct(final SponsorForm form, final BindingResult binding) {
		Sponsor res = null;
		if (form.getId() == 0 && !form.getAccept()) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("brotherhood.mustaccept", null, locale);
			binding.addError(new FieldError("brotherhoodForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("brotherhood.mustmatch", null, locale);
			binding.addError(new FieldError("brotherhoodForm", "confirmPassword", errorMessage));
		}
		if (form.getId() == 0)
			res = this.create();
		else {
			res = this.findOne(form.getId());
			Assert.notNull(res);
		}
		res.setAddress(form.getAddress());
		res.setMiddleName(form.getMiddleName());
		res.setEmail(form.getEmail());
		res.setName(form.getName());
		res.setPhone(form.getPhone());
		res.setPhoto(form.getPhoto());
		res.setSurname(form.getSurname());
		res.getUserAccount().setUsername(form.getUsername());
		res.getUserAccount().setPassword(form.getPassword());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.SPONSOR);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		this.validator.validate(form, binding);
		return res;
	}

	public void deleteSponsor(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		final List<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsBySponsorId(sponsor.getId());
		for (final Sponsorship s : sponsorships) {
			Assert.isTrue(s.getSponsor().getId() == sponsor.getId());
			this.sponsorshipService.delete(s);
			this.creditCardService.delete(s.getCreditCard());
			final Collection<Sponsorship> sponsorships1 = this.sponsorshipService.findAll();
			Assert.isTrue(!(sponsorships1.contains(s)));

		}
		//		final Collection<Box> boxes = this.actorService.findBoxByActorId(sponsor.getId());
		//		for (final Box b : boxes) {
		//			Assert.isTrue(b.getActor().getId() == sponsor.getId());
		//			this.boxService.delete(b);
		//			final Collection<Box> boxes1 = this.boxService.findAll();
		//			Assert.isTrue(!(boxes1.contains(b)));
		//		}
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findProfileByActorId(sponsor.getId());
		for (final SocialProfile s : socialProfiles) {
			Assert.isTrue(s.getActor().getId() == sponsor.getId());
			this.socialProfileService.delete(s);
		}
		this.sponsorRepository.delete(sponsor.getId());
		final Collection<Actor> actors = this.actorService.findAll();
		Assert.isTrue(!(actors.contains(sponsor)));
	}

}
