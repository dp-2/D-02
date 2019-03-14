
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

import repositories.ChapterRepository;
import security.Authority;
import security.UserAccount;
import domain.Chapter;
import domain.Configuration;
import forms.ChapterForm;

@Service
@Transactional
public class ChapterService {

	//Repository-------------------------------------------------------------------

	@Autowired
	private ChapterRepository		chapterRepository;

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

	public Chapter create() {
		Chapter result;
		result = new Chapter();
		//establezco ya su tipo de userAccount porque no va a cambiar
		result.setUserAccount(new UserAccount());
		final Authority authority = new Authority();
		authority.setAuthority(Authority.CHAPTER);
		result.getUserAccount().addAuthority(authority);
		//los atributos que no pueden estar vacíos
		result.setBanned(false);
		result.setSpammer(false);
		result.setScore(0.0);
		return result;
	}

	public Chapter findChapterByUserAcountId(final int userAccountId) {
		return this.chapterRepository.findChapterByUserAcountId(userAccountId);
	}

	public Chapter findOne(final Integer id) {
		return this.chapterRepository.findOne(id);
	}

	//	public Chapter findChapterByCreditCardId(final int creditCardId) {
	//		return this.chapterRepository.findChapterByCreditCardId(creditCardId);
	//	}

	public void flush() {
		this.chapterRepository.flush();
	}

	public Chapter save(final Chapter chapter) {
		//comprobamos que el customer que nos pasan no sea nulo
		Assert.notNull(chapter);
		Boolean isCreating = null;

		Assert.isTrue(!(chapter.getEmail().endsWith("@") || chapter.getEmail().endsWith("@>")));

		if (chapter.getId() == 0) {
			isCreating = true;
			chapter.setSpammer(false);

			//comprobamos que ningún actor resté autenticado (ya que ningun actor puede crear los customers)
			//this.serviceUtils.checkNoActor();

		} else {
			isCreating = false;
			//comprobamos que su id no sea negativa por motivos de seguridad
			this.serviceUtils.checkIdSave(chapter);

			//este customer será el que está en la base de datos para usarlo si estamos ante un customer que ya existe
			Chapter chapterDB;
			Assert.isTrue(chapter.getId() > 0);

			//cogemos el customer de la base de datos
			chapterDB = this.chapterRepository.findOne(chapter.getId());

			chapter.setSpammer(chapterDB.getSpammer());
			chapter.setUserAccount(chapterDB.getUserAccount());

			//Comprobamos que el actor sea un Chapter
			final String[] auths = new String[] {
				"CHAPTER", "ADMIN"
			};
			this.serviceUtils.checkAnyAuthority(auths);
			//esto es para ver si el actor que está logueado es el mismo que se está editando
			Assert.isTrue(this.serviceUtils.checkActorBoolean(chapter) || this.serviceUtils.checkAuthorityBoolean("ADMIN"));

		}

		if ((!chapter.getPhone().startsWith("+")) && StringUtils.isNumeric(chapter.getPhone()) && chapter.getPhone().length() > 3) {
			final Configuration confs = this.configurationService.findOne();
			chapter.setPhone(confs.getCountryCode() + chapter.getPhone());
		}
		Chapter res;
		//le meto al resultado final el customer que he ido modificando anteriormente
		res = this.chapterRepository.save(chapter);
		this.flush();
		if (isCreating)
			this.boxService.createIsSystemBoxs(res);
		return res;
	}

	public ChapterForm construct(final Chapter chapter) {
		final ChapterForm res = new ChapterForm();
		res.setEmail(chapter.getEmail());
		res.setName(chapter.getName());
		res.setPhone(chapter.getPhone());
		res.setPhoto(chapter.getPhoto());
		res.setSurname(chapter.getSurname());
		res.setUsername(chapter.getUserAccount().getUsername());
		res.setId(chapter.getId());
		res.setVersion(chapter.getVersion());
		res.setMiddleName(chapter.getMiddleName());
		res.setAddress(chapter.getAddress());
		return res;
	}

	public Chapter deconstruct(final ChapterForm form, final BindingResult binding) {
		Chapter res = null;
		if (form.getId() == 0 && !form.getAccept()) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("chapter.mustaccept", null, locale);
			binding.addError(new FieldError("chapterForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("chapter.mustmatch", null, locale);
			binding.addError(new FieldError("chapterForm", "confirmPassword", errorMessage));
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
