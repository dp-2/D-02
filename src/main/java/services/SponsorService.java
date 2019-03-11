
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import domain.Brotherhood;
import domain.Configuration;
import domain.Sponsor;
import forms.BrotherhoodForm;
import forms.SponsorForm;
import repositories.SponsorRepository;
import security.Authority;
import security.UserAccount;

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


	//Methods----------------------------------------------------------------------

	public Sponsor create() {
		Sponsor result;
		result = new Sponsor();
		//establezco ya su tipo de userAccount porque no va a cambiar
		result.setUserAccount(new UserAccount());
		final Authority authority = new Authority();
		authority.setAuthority(Authority.MEMBER);
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

	public Sponsor save(final Sponsor sponsor) {
		//comprobamos que el customer que nos pasan no sea nulo
		Assert.notNull(sponsor);
		Boolean isCreating = null;

		Assert.isTrue(!(sponsor.getEmail().endsWith("@") || sponsor.getEmail().endsWith("@>")));

		if (sponsor.getId() == 0) {
			isCreating = true;
			sponsor.setSpammer(false);

			//comprobamos que ningún actor resté autenticado (ya que ningun actor puede crear los customers)
			//this.serviceUtils.checkNoActor();

		} else {
			isCreating = false;
			//comprobamos que su id no sea negativa por motivos de seguridad
			this.serviceUtils.checkIdSave(sponsor);

			//este customer será el que está en la base de datos para usarlo si estamos ante un customer que ya existe
			Sponsor sponsorDB;
			Assert.isTrue(sponsor.getId() > 0);

			//cogemos el customer de la base de datos
			sponsorDB = this.sponsorRepository.findOne(sponsor.getId());

			sponsor.setSpammer(sponsorDB.getSpammer());
			sponsor.setUserAccount(sponsorDB.getUserAccount());

			//Comprobamos que el actor sea un Sponsor
			final String[] auths = new String[] {
				"MEMBER", "ADMIN"
			};
			this.serviceUtils.checkAnyAuthority(auths);
			//esto es para ver si el actor que está logueado es el mismo que se está editando
			Assert.isTrue(this.serviceUtils.checkActorBoolean(sponsor) || this.serviceUtils.checkAuthorityBoolean("ADMIN"));

		}

		if ((!sponsor.getPhone().startsWith("+")) && StringUtils.isNumeric(sponsor.getPhone()) && sponsor.getPhone().length() > 3) {
			final Configuration confs = this.configurationService.findOne();
			sponsor.setPhone(confs.getCountryCode() + sponsor.getPhone());
		}
		Sponsor res;
		//le meto al resultado final el customer que he ido modificando anteriormente
		res = this.sponsorRepository.save(sponsor);
		this.flush();
		if (isCreating)
			this.boxService.createIsSystemBoxs(res);
		return res;
	}

	public BrotherhoodForm construct(final Brotherhood b) {
		final BrotherhoodForm res = new BrotherhoodForm();
		res.setEmail(b.getEmail());
		res.setName(b.getName());
		res.setPhone(b.getPhone());
		res.setPhoto(b.getPhoto());
		res.setPictures(b.getPictures());
		res.setSurname(b.getSurname());
		res.setTitle(b.getTitle());
		res.setUsername(b.getUserAccount().getUsername());
		res.setId(b.getId());
		res.setVersion(b.getVersion());
		res.setMiddleName(b.getMiddleName());
		res.setAddress(b.getAddress());
		return res;
	}

	public Sponsor deconstruct(final SponsorForm form, final BindingResult binding) {
		Sponsor res = null;
		if (form.getId() == 0 && !form.getAccept()) {
			/*
			 * binding.addError(new FieldError("brotherhoodForm", "accept", form.getAccept(), false, new String[] {
			 * "brotherhoodForm.accept", "accept"
			 * }, new Object[] {
			 * new DefaultMessageSourceResolvable(new String[] {
			 * "brotherhoodForm.accept", "accept"
			 * }, new Object[] {}, "accept")
			 * }, "brotherhood.mustaccept"));
			 */
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
		auth.setAuthority(Authority.BROTHERHOOD);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		this.validator.validate(form, binding);
		return res;
	}

}
