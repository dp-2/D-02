
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.HistoryRepository;
import security.LoginService;
import security.UserAccount;
import domain.Brotherhood;
import domain.History;

@Service
@Transactional
public class HistoryService {

	// Managed repository and services----------------------------------------------------------------
	@Autowired
	private HistoryRepository	historyRepository;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	//Services-----------------------------------------------------------------------------

	public History create() {
		final History history = new History();
		final UserAccount userAccount = LoginService.getPrincipal();
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(userAccount.getId());

		history.setBrotherhood(brotherhood);

		return history;
	}

	public History findOne(final int HistoryId) {
		return this.historyRepository.findOne(HistoryId);
	}
	//
	//	public Actor findByUsername(final String username) {
	//		final Actor actor = this.actorRepository.findByUsername(username);
	//		return actor;
	//	}
	//
	//	public Actor findByUserAccount(final UserAccount userAccount) {
	//		return this.actorRepository.findByUserAccount(userAccount.getId());
	//	}
	//
	//	public Collection<Actor> findAll() {
	//		final Collection<Actor> actors = this.actorRepository.findAll();
	//		Assert.notNull(actors);
	//
	//		return actors;
	//	}
	//
	//	public Collection<Actor> findAllTypes() {
	//		final Collection<Actor> actors = new ArrayList<>();
	//		actors.addAll(this.administratorService.findAll());
	//		actors.addAll(this.memberService.findAll());
	//		actors.addAll(this.brotherhoodService.findAll());
	//
	//		return actors;
	//	}
	//
	//	public Actor save(final Actor actor) {
	//
	//		Assert.notNull(actor);
	//		final Actor saved = this.actorRepository.save(actor);
	//
	//		return saved;
	//	}
	//
	//	//Other Methods---------------------------------------------------------------------
	//
	//	public Collection<Actor> findSpammerActors() {
	//		return this.actorRepository.findSpammerActors();
	//	}
	//
	//	public void ban(final Actor actor) {
	//		actor.setBanned(true);
	//		actor.getUserAccount().setBanned(true);
	//		actor.getUserAccount().setEnabled(false);
	//		this.save(actor);
	//	}
	//
	//	public void unban(final Actor actor) {
	//		actor.setBanned(false);
	//		actor.setSpammer(false);
	//		actor.getUserAccount().setEnabled(true);
	//		this.save(actor);
	//
	//	}
	//
	//	public void update(final Actor actor) {
	//
	//		Assert.notNull(actor);
	//
	//		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
	//		final Authority mem = new Authority();
	//		mem.setAuthority(Authority.MEMBER);
	//		final Authority bro = new Authority();
	//		bro.setAuthority(Authority.BROTHERHOOD);
	//		final Authority admin = new Authority();
	//		admin.setAuthority(Authority.ADMIN);
	//
	//		if (authorities.contains(mem)) {
	//			Member member = null;
	//			if (actor.getId() != 0)
	//				member = this.memberService.findOne(actor.getId());
	//			else
	//				member = this.memberService.create();
	//			member.setUserAccount(actor.getUserAccount());
	//
	//			member.setEmail(actor.getEmail());
	//			member.setBanned(actor.getBanned());
	//			member.setSpammer(actor.getSpammer());
	//			member.setName(actor.getName());
	//			member.setPhone(actor.getPhone());
	//			member.setPhoto(actor.getPhoto());
	//			member.setSurname(actor.getSurname());
	//			member.setScore(actor.getScore());
	//			member.setAddress(actor.getAddress());
	//			member.setMiddleName(actor.getMiddleName());
	//
	//			final Actor actor1 = this.memberService.save(member);
	//			//this.boxService.createIsSystemBoxs(actor1);
	//		} else if (authorities.contains(bro)) {
	//			Brotherhood brotherhood = null;
	//			if (actor.getId() != 0)
	//
	//				brotherhood = this.brotherhoodService.findOne(actor.getId());
	//			else {
	//				brotherhood = this.brotherhoodService.create();
	//				brotherhood.setUserAccount(actor.getUserAccount());
	//			}
	//			brotherhood.setScore(actor.getScore());
	//			brotherhood.setEmail(actor.getEmail());
	//			brotherhood.setBanned(actor.getBanned());
	//			brotherhood.setSpammer(actor.getSpammer());
	//			brotherhood.setName(actor.getName());
	//			brotherhood.setPhone(actor.getPhone());
	//			brotherhood.setPhoto(actor.getPhoto());
	//			brotherhood.setSurname(actor.getSurname());
	//			brotherhood.setAddress(actor.getAddress());
	//			brotherhood.setMiddleName(actor.getMiddleName());
	//
	//			final Actor actor1 = this.brotherhoodService.save(brotherhood);
	//			//this.boxService.createIsSystemBoxs(actor1);
	//
	//		} else if (authorities.contains(admin)) {
	//			Administrator administrator = null;
	//			if (actor.getId() != 0)
	//				administrator = this.administratorService.findOne(actor.getId());
	//			else {
	//				administrator = this.administratorService.create();
	//				administrator.setUserAccount(actor.getUserAccount());
	//			}
	//
	//			administrator.setSurname(actor.getSurname());
	//			administrator.setMiddleName(actor.getMiddleName());
	//			administrator.setEmail(actor.getEmail());
	//			administrator.setBanned(actor.getBanned());
	//			administrator.setSpammer(actor.getSpammer());
	//			administrator.setName(actor.getName());
	//			administrator.setPhone(actor.getPhone());
	//			administrator.setPhoto(actor.getPhoto());
	//			administrator.setAddress(actor.getAddress());
	//
	//			final Actor actor1 = this.administratorService.save(administrator);
	//			//this.boxService.createIsSystemBoxs(actor1);
	//		}
	//
	//	}
	//
	//	public Actor findActorByUsername(final String username) {
	//		final Actor actor = this.actorRepository.findByUsername(username);
	//		return actor;
	//	}
	//
	//	public Actor findPrincipal() {
	//		final UserAccount userAccount = LoginService.getPrincipal();
	//		return this.actorRepository.findByUserAccount(userAccount.getId());
	//	}
	//
	//	public Collection<Actor> findAllExceptMe() {
	//		final Collection<Actor> res = this.findAll();
	//		res.remove(this.findPrincipal());
	//		return res;
	//	}
	//
	//	public ActorForm construct(final Actor b) {
	//		final ActorForm res = new ActorForm();
	//		res.setEmail(b.getEmail());
	//		res.setName(b.getName());
	//		res.setPhone(b.getPhone());
	//		res.setPhoto(b.getPhoto());
	//		res.setSurname(b.getSurname());
	//		res.setUsername(b.getUserAccount().getUsername());
	//		res.setId(b.getId());
	//		res.setVersion(b.getVersion());
	//		res.setMiddleName(b.getMiddleName());
	//		res.setAddress(b.getAddress());
	//		final Authority auth = ((List<Authority>) b.getUserAccount().getAuthorities()).get(0);
	//		res.setAuthority(auth.getAuthority());
	//		return res;
	//	}
	//
	//	public void validateForm(final ActorForm form, final BindingResult binding) {
	//		if (form.getId() == 0 && !form.getAccept()) {
	//			/*
	//			 * binding.addError(new FieldError("brotherhoodForm", "accept", form.getAccept(), false, new String[] {
	//			 * "brotherhoodForm.accept", "accept"
	//			 * }, new Object[] {
	//			 * new DefaultMessageSourceResolvable(new String[] {
	//			 * "brotherhoodForm.accept", "accept"
	//			 * }, new Object[] {}, "accept")
	//			 * }, "brotherhood.mustaccept"));
	//			 */
	//			final Locale locale = LocaleContextHolder.getLocale();
	//			final String errorMessage = this.messageSource.getMessage("brotherhood.mustaccept", null, locale);
	//			binding.addError(new FieldError("actorForm", "accept", errorMessage));
	//		}
	//		if (!form.getConfirmPassword().equals(form.getPassword())) {
	//			final Locale locale = LocaleContextHolder.getLocale();
	//			final String errorMessage = this.messageSource.getMessage("brotherhood.mustmatch", null, locale);
	//			binding.addError(new FieldError("actorForm", "confirmPassword", errorMessage));
	//		}
	//		if ((form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) && !form.getAuthority().equals(Authority.ADMIN)) {
	//			final Locale locale = LocaleContextHolder.getLocale();
	//			final String errorMessage = this.messageSource.getMessage("actor.bademail", null, locale);
	//			binding.addError(new FieldError("actorForm", "email", errorMessage));
	//		}
	//	}
	//	public Actor deconstruct(final ActorForm form, final BindingResult binding) {
	//		Actor res = null;
	//		if (form.getId() == 0)
	//			res = this.create(form.getAuthority());
	//		else {
	//			res = this.findOne(form.getId());
	//			Assert.notNull(res);
	//		}
	//		res.setAddress(form.getAddress());
	//		res.setMiddleName(form.getMiddleName());
	//		res.setEmail(form.getEmail());
	//		res.setName(form.getName());
	//		res.setPhone(form.getPhone());
	//		res.setPhoto(form.getPhoto());
	//		res.setSurname(form.getSurname());
	//		res.getUserAccount().setUsername(form.getUsername());
	//		res.getUserAccount().setPassword(form.getPassword());
	//		final Collection<Authority> authorities = new ArrayList<Authority>();
	//		final Authority auth = new Authority();
	//		auth.setAuthority(form.getAuthority());
	//		authorities.add(auth);
	//		res.getUserAccount().setAuthorities(authorities);
	//		this.validator.validate(form, binding);
	//		return res;
	//	}
	//
	//	public boolean containsSpam(final String s) {
	//		boolean res = false;
	//		final List<String> negativeWords = new ArrayList<>();
	//		negativeWords.addAll(this.configurationService.findOne().getNegativeWordsEN());
	//		negativeWords.addAll(this.configurationService.findOne().getNegativeWordsES());
	//		for (final String spamWord : negativeWords)
	//			if (s.contains(spamWord)) {
	//				System.out.println(spamWord);
	//				res = true;
	//				break;
	//			}
	//		return res;
	//	}
	//	public boolean isSpammer(final Actor a) {
	//		boolean res = false;
	//		Assert.notNull(a);
	//		this.serviceUtils.checkId(a.getId());
	//		final Actor actor = this.actorRepository.findOne(a.getId());
	//		Assert.notNull(actor);
	//
	//		if (!res)
	//			for (final Message m : this.messageService.findSendedMessages(actor)) {
	//				res = this.containsSpam(m.getBody()) || this.containsSpam(m.getSubject());
	//				if (!res)
	//
	//					res = this.containsSpam(m.getTags());
	//
	//				else
	//					break;
	//			}
	//
	//		return res;
	//	}
}
