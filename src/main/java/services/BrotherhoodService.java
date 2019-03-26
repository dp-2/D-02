
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import domain.Area;
import domain.Brotherhood;
import domain.DFloat;
import domain.Enroll;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.March;
import domain.Member;
import domain.MiscellaneousRecord;
import domain.Parade;
import domain.Path;
import domain.PeriodRecord;
import domain.SocialProfile;
import domain.Sponsorship;
import domain.Url;
import forms.BrotherhoodForm;
import repositories.BrotherhoodRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountRepository;

@Service
@Transactional
public class BrotherhoodService {

	// Repository

	@Autowired
	private BrotherhoodRepository		repository;

	// Services

	@Autowired
	private ActorService				actorService;
	@Autowired
	private BoxService					boxService;
	@Autowired
	private UserAccountRepository		userAccountRepository;
	@Autowired
	private EnrollService				enrollService;

	@Autowired
	private ParadeService				paradeService;

	@Autowired
	private MarchService				marchService;

	@Autowired
	private SocialProfileService		socialProfileService;

	@Autowired
	private DFloatService				dFloatService;

	@Autowired
	private AreaService					areaService;

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private LegalRecordService			legalRecordService;

	@Autowired
	private InceptionRecordService		inceptionRecordService;

	@Autowired
	private LinkRecordService			linkRecordService;

	@Autowired
	private PathService					pathService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private PeriodRecordService			periodRecordService;

	@Autowired
	private SponsorshipService			sponsorshipService;

	@Autowired
	private ServiceUtils				serviceUtils;

	@Autowired(required = false)
	private Validator					validator;
	@Autowired
	private MessageSource				messageSource;
	@Autowired
	private SegmentService				segmentService;


	public Brotherhood findOne(final Integer id) {
		this.serviceUtils.checkId(id);
		return this.repository.findOne(id);
	}

	public Collection<Brotherhood> findAll(final Collection<Integer> ids) {
		this.serviceUtils.checkIds(ids);
		return this.repository.findAll(ids);
	}

	public List<Brotherhood> findAll() {
		return this.repository.findAll();
	}

	public Brotherhood create() {
		this.serviceUtils.checkNoActor();
		final Brotherhood res = new Brotherhood();
		res.setBanned(false);
		res.setSpammer(false);
		res.setEstablishedMoment(new Date(System.currentTimeMillis() - 1000));
		res.setPictures(new ArrayList<Url>());
		res.setUserAccount(new UserAccount());
		res.setScore(0.);
		return res;
	}

	public Brotherhood save(final Brotherhood b) {
		final Brotherhood brotherhood = (Brotherhood) this.serviceUtils.checkObjectSave(b);

		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hash = encoder.encodePassword(b.getUserAccount().getPassword(), null);
		if (b.getId() == 0) {
			this.serviceUtils.checkNoActor();
			b.setBanned(false);
			b.setSpammer(false);
			b.setEstablishedMoment(new Date(System.currentTimeMillis() - 1000));
			b.setScore(0.);
			b.getUserAccount().setPassword(hash);

		} else {
			this.serviceUtils.checkAnyAuthority(new String[] {
				Authority.ADMIN, Authority.BROTHERHOOD
			});
			if (this.actorService.findPrincipal() instanceof Brotherhood) {
				this.serviceUtils.checkActor(brotherhood);
				b.setBanned(brotherhood.getBanned());
				b.setSpammer(brotherhood.getSpammer());
				if (brotherhood.getUserAccount().getPassword() != hash)
					b.getUserAccount().setPassword(hash);
			} else {
				b.setEmail(brotherhood.getEmail());
				b.setName(brotherhood.getName());
				b.setPhone(brotherhood.getPhone());
				b.setPhoto(brotherhood.getPhoto());
				b.setPictures(brotherhood.getPictures());
				b.setSurname(brotherhood.getSurname());
				b.setTitle(brotherhood.getTitle());
				b.setUserAccount(brotherhood.getUserAccount());
				b.setAddress(brotherhood.getAddress());
				b.setMiddleName(brotherhood.getMiddleName());
				b.setScore(brotherhood.getScore());
			}
			b.setEstablishedMoment(brotherhood.getEstablishedMoment());
		}
		final UserAccount userAccount = this.userAccountRepository.save(b.getUserAccount());
		brotherhood.setUserAccount(userAccount);
		final Brotherhood res = this.repository.save(b);
		if (b.getId() == 0) {
			this.boxService.addSystemBox(res);
			this.historyService.createAndSave(res);
			final History historyDB = this.historyService.findOneByBrotherhoodId(res.getId());
			this.inceptionRecordService.createAndSave(historyDB);

		}
		return res;
	}

	public void delete(final Brotherhood b) {
		final Brotherhood brotherhood = (Brotherhood) this.serviceUtils.checkObject(b);
		this.serviceUtils.checkActor(brotherhood);
		this.repository.delete(brotherhood);
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

	public void validateForm(final BrotherhoodForm form, final BindingResult binding) {
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
		if (form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("actor.bademail", null, locale);
			binding.addError(new FieldError("brotherhoodForm", "email", errorMessage));
		}
	}

	public Brotherhood deconstruct(final BrotherhoodForm form) {
		Brotherhood res = null;
		if (form.getPictures() == null)
			form.setPictures(new ArrayList<Url>());
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
		res.setPictures(form.getPictures());
		res.setSurname(form.getSurname());
		res.setTitle(form.getTitle());
		res.getUserAccount().setUsername(form.getUsername());
		res.getUserAccount().setPassword(form.getPassword());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.BROTHERHOOD);
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		return res;
	}

	public Brotherhood findBrotherhoodByUserAcountId(final int userAccountId) {
		return this.repository.findBrotherhoodByUserAcountId(userAccountId);
	}
	//TODO se podria hacer por query pero no lo consigo
	public List<Member> listMembersByBrotherhoodId(final Integer bh) {
		final Brotherhood brother = this.findBrotherhoodByUserAcountId(bh);
		final List<Member> members = new ArrayList<>();
		final List<Enroll> enrolls = (List<Enroll>) this.enrollService.findAll();
		for (final Enroll e : enrolls)
			if (e.getBrotherhood() == brother)
				members.add(e.getMember());
		return members;
	}

	public Brotherhood BrotherhoodWithMoreMembers() {

		final List<Brotherhood> result;
		result = this.repository.listBrotherhoodByMembers();
		final Brotherhood bh = result.get(0);
		return bh;
	}

	public Brotherhood BrotherhoodWithLessMembers() {

		List<Brotherhood> result;
		result = this.repository.listBrotherhoodByMembers();
		final Brotherhood bh = result.get(result.size() - 1);
		return bh;
	}

	public List<String> brotherhoodLargestHistory() {
		return this.repository.brotherhoodLargestHistory();
	}

	public List<String> brotherhoodLargestHistoryThanAVG() {
		return this.repository.brotherhoodLargestHistoryThanAVG();
	}

	public void deleteBrotherhood(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		final Collection<Enroll> enrolls = this.enrollService.findEnrollByBrotherhood(brotherhood.getId());
		final Collection<DFloat> floats = this.dFloatService.findAllDFloatsByBrotherhood(brotherhood);
		final Collection<Parade> parades = this.paradeService.findParadesByBrotherhoodId(brotherhood.getId());
		final Area area = this.areaService.findAreaByBrotherhoodId(brotherhood.getId());
		final History h = this.historyService.findOneByBrotherhoodId(brotherhood.getId());
		final Collection<LegalRecord> legalRecords = this.legalRecordService.findAllByHistoryId(h.getId());
		final Collection<MiscellaneousRecord> miscellaneousRecords = this.miscellaneousRecordService.findAllByHistoryId(h.getId());
		final Collection<PeriodRecord> periodRecords = this.periodRecordService.findAllByHistoryId(h.getId());
		final Collection<LinkRecord> linkRecords = this.linkRecordService.findAllByHistoryId(h.getId());
		final InceptionRecord inceptionRecord = this.inceptionRecordService.getInceptionRecordByBrotherhood(h.getId());

		if (!legalRecords.isEmpty())
			for (final LegalRecord l : legalRecords) {
				Assert.isTrue(l.getHistory().getId() == h.getId());
				this.legalRecordService.delete1(l);
			}

		if (!miscellaneousRecords.isEmpty())
			for (final MiscellaneousRecord m : miscellaneousRecords) {
				Assert.isTrue(m.getHistory().getId() == h.getId());
				this.miscellaneousRecordService.delete(m);
			}

		if (!periodRecords.isEmpty())
			for (final PeriodRecord p : periodRecords) {
				Assert.isTrue(p.getHistory().getId() == h.getId());
				this.periodRecordService.delete(p);
			}

		if (!linkRecords.isEmpty())
			for (final LinkRecord l : linkRecords) {
				Assert.isTrue(l.getHistory().getId() == h.getId());
				this.linkRecordService.delete(l);
			}

		this.inceptionRecordService.delete1(inceptionRecord);

		this.historyService.delete(h);

		if (!enrolls.isEmpty())
			for (final Enroll e : enrolls) {
				this.enrollService.delete1(e);
				final Collection<Enroll> enrolls1 = this.enrollService.findAll();
				Assert.isTrue(!(enrolls1.contains(e)));
			}

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findProfileByActorId(brotherhood.getId());
		if (!socialProfiles.isEmpty())
			for (final SocialProfile s : socialProfiles)
				this.socialProfileService.delete(s);

		if (!floats.isEmpty())
			for (final DFloat f : floats) {
				Assert.isTrue(f.getBrotherhood().getId() == brotherhood.getId());
				this.dFloatService.delete(f);
				final Collection<DFloat> dFloats1 = this.dFloatService.findAll();
				Assert.isTrue(!(dFloats1.contains(f)));
			}

		if (!parades.isEmpty())
			for (final Parade p : parades) {
				final Collection<March> marchs = this.marchService.findMarchsByParade(p.getId());
				final Path path = this.pathService.findByParadeId(p.getId());
				if (!marchs.isEmpty())
					for (final March m : marchs) {
						Assert.isTrue(m.getParade().getId() == p.getId());
						this.marchService.delete1(m);
					}
				if (path != null)
					this.pathService.delete1(path);

			}
		if (area != null) {
			area.setBrotherhood(null);
			this.areaService.save(area);
		}
		if (!parades.isEmpty())
			for (final Parade parade : parades) {
				final List<Sponsorship> sponsorships = this.paradeService.findSponsorshipsByParadeId(parade.getId());
				final List<Sponsorship> sponsorships2 = this.paradeService.findSponsorshipsByParadeId(parade.getId());
				if (sponsorships2.isEmpty())
					this.paradeService.delete1(parade);
				int i = 0;
				if (!sponsorships.isEmpty())
					while (i <= sponsorships.size()) {
						final int j = 0;
						this.sponsorshipService.delete(sponsorships.get(j));
						sponsorships.remove(sponsorships.get(j));
						i++;
						if (sponsorships.isEmpty() || sponsorships == null || sponsorships.size() == 0)

							this.paradeService.delete1(parade);
					}
			}

		this.delete1(brotherhood);

	}
	public void delete1(final Brotherhood brotherhood) {
		this.repository.delete(brotherhood);
	}

	public void flush() {
		this.repository.flush();
	}

}
