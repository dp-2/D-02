
package services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
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

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import domain.Actor;
import domain.Administrator;
import domain.Area;
import domain.Box;
import domain.Brotherhood;
import domain.Chapter;
import domain.DFloat;
import domain.March;
import domain.Member;
import domain.Message;
import domain.Parade;
import domain.Proclaim;
import domain.Sponsor;
import domain.Sponsorship;
import forms.ActorForm;

@Service
@Transactional
public class ActorService {

	// Managed repository ----------------------------------------------------------------
	@Autowired
	private ActorRepository			actorRepository;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	//Service-----------------------------------------------------------------------------
	@Autowired
	private MemberService			memberService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired(required = false)
	private Validator				validator;

	@Autowired
	private MessageSource			messageSource;

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private EnrollService			enrollService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private MarchService			marchService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private DFloatService			dFloatService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private ProclaimService			proclaimService;


	public Actor create(final String authority) {
		final Actor actor = new Actor();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();
		a.setAuthority(authority);
		authorities.add(a);
		userAccount.setAuthorities(authorities);
		actor.setSpammer(false);
		actor.setUserAccount(userAccount);
		actor.setBanned(false);
		actor.setScore(0.0);
		return actor;
	}

	public Actor findOne(final int ActorId) {
		return this.actorRepository.findOne(ActorId);
	}

	public Actor findByUsername(final String username) {
		final Actor actor = this.actorRepository.findByUsername(username);
		return actor;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		return this.actorRepository.findByUserAccount(userAccount.getId());
	}

	public Collection<Actor> findAll() {
		final Collection<Actor> actors = this.actorRepository.findAll();
		Assert.notNull(actors);

		return actors;
	}

	public Collection<Actor> findAllTypes() {
		final Collection<Actor> actors = new ArrayList<>();
		actors.addAll(this.administratorService.findAll());
		actors.addAll(this.memberService.findAll());
		actors.addAll(this.brotherhoodService.findAll());

		return actors;
	}

	public Actor save(final Actor actor) {

		Assert.notNull(actor);
		final Actor saved = this.actorRepository.save(actor);

		return saved;
	}

	//Other Methods---------------------------------------------------------------------

	public Collection<Actor> findSpammerActors() {
		return this.actorRepository.findSpammerActors();
	}

	public void ban(final Actor actor) {
		actor.setBanned(true);
		actor.getUserAccount().setBanned(true);
		actor.getUserAccount().setEnabled(false);
		this.save(actor);
	}

	public void unban(final Actor actor) {
		actor.setBanned(false);
		actor.setSpammer(false);
		actor.getUserAccount().setEnabled(true);
		this.save(actor);

	}

	public void update(final Actor actor) {

		Assert.notNull(actor);

		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		final Authority mem = new Authority();
		mem.setAuthority(Authority.MEMBER);
		final Authority bro = new Authority();
		bro.setAuthority(Authority.BROTHERHOOD);
		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);
		final Authority spon = new Authority();
		spon.setAuthority(Authority.SPONSOR);
		final Authority cha = new Authority();
		cha.setAuthority(Authority.CHAPTER);

		if (authorities.contains(mem)) {
			Member member = null;
			if (actor.getId() != 0)
				member = this.memberService.findOne(actor.getId());
			else
				member = this.memberService.create();
			member.setUserAccount(actor.getUserAccount());

			member.setEmail(actor.getEmail());
			member.setBanned(actor.getBanned());
			member.setSpammer(actor.getSpammer());
			member.setName(actor.getName());
			member.setPhone(actor.getPhone());
			member.setPhoto(actor.getPhoto());
			member.setSurname(actor.getSurname());
			member.setScore(actor.getScore());
			member.setAddress(actor.getAddress());
			member.setMiddleName(actor.getMiddleName());

			final Actor actor1 = this.memberService.save(member);
			if (actor.getId() == 0)
				this.boxService.addSystemBox(actor1);
		} else if (authorities.contains(bro)) {
			Brotherhood brotherhood = null;
			if (actor.getId() != 0)

				brotherhood = this.brotherhoodService.findOne(actor.getId());
			else {
				brotherhood = this.brotherhoodService.create();
				brotherhood.setUserAccount(actor.getUserAccount());
			}
			brotherhood.setScore(actor.getScore());
			brotherhood.setEmail(actor.getEmail());
			brotherhood.setBanned(actor.getBanned());
			brotherhood.setSpammer(actor.getSpammer());
			brotherhood.setName(actor.getName());
			brotherhood.setPhone(actor.getPhone());
			brotherhood.setPhoto(actor.getPhoto());
			brotherhood.setSurname(actor.getSurname());
			brotherhood.setAddress(actor.getAddress());
			brotherhood.setMiddleName(actor.getMiddleName());

			final Actor actor1 = this.brotherhoodService.save(brotherhood);
			if (actor.getId() == 0)
				this.boxService.addSystemBox(actor1);

		} else if (authorities.contains(admin)) {
			Administrator administrator = null;
			if (actor.getId() != 0)
				administrator = this.administratorService.findOne(actor.getId());
			else {
				administrator = this.administratorService.create();
				administrator.setUserAccount(actor.getUserAccount());
			}

			administrator.setSurname(actor.getSurname());
			administrator.setMiddleName(actor.getMiddleName());
			administrator.setEmail(actor.getEmail());
			administrator.setBanned(actor.getBanned());
			administrator.setSpammer(actor.getSpammer());
			administrator.setName(actor.getName());
			administrator.setPhone(actor.getPhone());
			administrator.setPhoto(actor.getPhoto());
			administrator.setAddress(actor.getAddress());

			final Actor actor1 = this.administratorService.save(administrator);
			if (actor.getId() == 0)
				this.boxService.addSystemBox(actor1);
		} else if (authorities.contains(spon)) {
			Sponsor sponsor = null;
			if (actor.getId() != 0)
				sponsor = this.sponsorService.findOne(actor.getId());
			else
				sponsor = this.sponsorService.create();
			sponsor.setUserAccount(actor.getUserAccount());

			sponsor.setEmail(actor.getEmail());
			sponsor.setBanned(actor.getBanned());
			sponsor.setSpammer(actor.getSpammer());
			sponsor.setName(actor.getName());
			sponsor.setPhone(actor.getPhone());
			sponsor.setPhoto(actor.getPhoto());
			sponsor.setSurname(actor.getSurname());
			sponsor.setScore(actor.getScore());
			sponsor.setAddress(actor.getAddress());
			sponsor.setMiddleName(actor.getMiddleName());

			final Actor actor1 = this.sponsorService.save(sponsor);
			if (actor.getId() == 0)
				this.boxService.addSystemBox(actor1);
		} else if (authorities.contains(cha)) {
			Chapter chapter = null;
			if (actor.getId() != 0)

				chapter = this.chapterService.findOne(actor.getId());
			else {
				chapter = this.chapterService.create();
				chapter.setUserAccount(actor.getUserAccount());
			}
			chapter.setScore(actor.getScore());
			chapter.setEmail(actor.getEmail());
			chapter.setBanned(actor.getBanned());
			chapter.setSpammer(actor.getSpammer());
			chapter.setName(actor.getName());
			chapter.setPhone(actor.getPhone());
			chapter.setPhoto(actor.getPhoto());
			chapter.setSurname(actor.getSurname());
			chapter.setAddress(actor.getAddress());
			chapter.setMiddleName(actor.getMiddleName());
			chapter.setTitle(chapter.getTitle());

			final Actor actor1 = this.chapterService.save(chapter);
			if (actor.getId() == 0)
				this.boxService.addSystemBox(actor1);
		}

	}

	public Actor findActorByUsername(final String username) {
		final Actor actor = this.actorRepository.findByUsername(username);
		return actor;
	}

	public Actor findPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		return this.actorRepository.findByUserAccount(userAccount.getId());
	}

	public Collection<Actor> findAllExceptMe() {
		final Collection<Actor> res = this.findAll();
		res.remove(this.findPrincipal());
		return res;
	}

	public ActorForm construct(final Actor b) {
		final ActorForm res = new ActorForm();
		res.setEmail(b.getEmail());
		res.setName(b.getName());
		res.setPhone(b.getPhone());
		res.setPhoto(b.getPhoto());
		res.setSurname(b.getSurname());
		res.setUsername(b.getUserAccount().getUsername());
		res.setId(b.getId());
		res.setVersion(b.getVersion());
		res.setMiddleName(b.getMiddleName());
		res.setAddress(b.getAddress());
		final Authority auth = ((List<Authority>) b.getUserAccount().getAuthorities()).get(0);
		if (auth.getAuthority().equals(("CHAPTER"))) {
			final Chapter c = this.chapterService.findOne(b.getId());
			res.setTitle(c.getTitle());
		}
		res.setAuthority(auth.getAuthority());
		return res;
	}
	public void validateForm(final ActorForm form, final BindingResult binding) {
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
			binding.addError(new FieldError("actorForm", "accept", errorMessage));
		}
		if (!form.getConfirmPassword().equals(form.getPassword())) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("brotherhood.mustmatch", null, locale);
			binding.addError(new FieldError("actorForm", "confirmPassword", errorMessage));
		}
		if ((form.getEmail().endsWith("@") || form.getEmail().endsWith("@>")) && !form.getAuthority().equals(Authority.ADMIN)) {
			final Locale locale = LocaleContextHolder.getLocale();
			final String errorMessage = this.messageSource.getMessage("actor.bademail", null, locale);
			binding.addError(new FieldError("actorForm", "email", errorMessage));
		}
	}
	public Actor deconstruct(final ActorForm form, final BindingResult binding) {
		Actor res = null;
		if (form.getId() == 0)
			res = this.create(form.getAuthority());
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
		auth.setAuthority(form.getAuthority());
		authorities.add(auth);
		res.getUserAccount().setAuthorities(authorities);
		this.validator.validate(form, binding);
		return res;
	}

	public boolean containsSpam(final String s) {
		boolean res = false;
		final List<String> negativeWords = new ArrayList<>();
		negativeWords.addAll(this.configurationService.findOne().getNegativeWordsEN());
		negativeWords.addAll(this.configurationService.findOne().getNegativeWordsES());
		for (final String spamWord : negativeWords)
			if (s.contains(spamWord)) {
				System.out.println(spamWord);
				res = true;
				break;
			}
		return res;
	}
	public boolean isSpammer(final Actor a) {
		boolean res = false;
		Assert.notNull(a);
		this.serviceUtils.checkId(a.getId());
		final Actor actor = this.actorRepository.findOne(a.getId());
		Assert.notNull(actor);

		if (!res)
			for (final Message m : this.messageService.findSendedMessages(actor)) {
				res = this.containsSpam(m.getBody()) || this.containsSpam(m.getSubject());
				if (!res)

					res = this.containsSpam(m.getTags());

				else
					break;
			}

		return res;
	}

	public void exportPDF(final int actorId) {
		final Actor actor = this.findOne(actorId);
		Document document = new Document();

		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		final Authority mem = new Authority();
		mem.setAuthority(Authority.MEMBER);
		final Authority bro = new Authority();
		bro.setAuthority(Authority.BROTHERHOOD);
		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);
		final Authority spon = new Authority();
		spon.setAuthority(Authority.SPONSOR);
		final Authority cha = new Authority();
		cha.setAuthority(Authority.CHAPTER);

		if (authorities.contains(mem)) {
			final Member member = this.memberService.findOne(actorId);

			try {

				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + member.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docMember(document, member);
				document.close();

				pdfWriter.close();

			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (authorities.contains(bro)) {
			final Brotherhood brotherhood = this.brotherhoodService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + brotherhood.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docBrotherhood(document, brotherhood);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (authorities.contains(admin)) {
			final Administrator administrator = this.administratorService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + administrator.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (authorities.contains(spon)) {
			final Sponsor sponsor = this.sponsorService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + sponsor.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docSponsor(document, sponsor);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (authorities.contains(cha)) {
			final Chapter chapter = this.chapterService.findOne(actor.getId());

			try {
				final OutputStream outputStream = new FileOutputStream("C:\\Users\\" + chapter.getUserAccount().getUsername() + "_data.pdf");
				final PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
				document = this.initDoc(actor, document);
				document = this.docChapter(document, chapter);
				document.close();
				pdfWriter.close();
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public Document initDoc(final Actor actor, final Document document) throws MalformedURLException, IOException {

		try {
			document.open();

			document.add(new Paragraph("USER DATA.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
			document.add(new Paragraph("\n"));
			if (StringUtils.isNotEmpty(actor.getPhoto())) {
				final URL url = new URL(actor.getPhoto());
				document.add(this.urlToImage(url));
			}
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Name: " + actor.getName()));
			if (StringUtils.isNotEmpty(actor.getMiddleName()))
				document.add(new Paragraph("Middle Name: " + actor.getMiddleName()));
			document.add(new Paragraph("Surname: " + actor.getSurname()));
			if (StringUtils.isNotEmpty(actor.getAddress()))
				document.add(new Paragraph("Address: " + actor.getAddress()));
			document.add(new Paragraph("Email: " + actor.getEmail()));
			if (StringUtils.isNotEmpty(actor.getPhone()))
				document.add(new Paragraph("Phone: " + actor.getPhone()));

		} catch (final DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	public Image urlToImage(final URL url) throws IOException, BadElementException {
		final HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
		httpURLCon.addRequestProperty("User-Agent", "Mozilla/4.76");
		final BufferedImage c = ImageIO.read(httpURLCon.getInputStream());
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(c, "png", baos);
		final Image iTextImage = Image.getInstance(baos.toByteArray());
		iTextImage.scaleToFit(100f, 200f);
		return iTextImage;
	}

	public Collection<Box> findBoxByActorId(final int actorId) {
		return this.actorRepository.findBoxByActorId(actorId);
	}

	public Document docMember(final Document document, final Member member) throws DocumentException, MalformedURLException, IOException {

		final List<Brotherhood> brotherhoods = new ArrayList<>(this.enrollService.findBrotherhoodsByMemberId(member.getId()));
		if (brotherhoods != null)
			if (!brotherhoods.isEmpty()) {
				document.add(new Paragraph("BROTHERHOODS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Brotherhood brotherhood : brotherhoods) {
					if (StringUtils.isNotEmpty(brotherhood.getPhoto()))
						document.add(this.urlToImage(new URL(brotherhood.getPhoto())));
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(brotherhood.getTitle()));
					document.add(new Paragraph("\n"));
				}
			}

		final List<Parade> parades = this.paradeService.findParadeOfMemberAPPROVED(member.getId());
		final List<March> marchs = new ArrayList<>(this.marchService.findMarchsByMemberAPPROVED(member.getId()));
		if (parades != null)
			if (!parades.isEmpty()) {
				document.add(new Paragraph("PARADES.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Parade parade : parades) {
					document.add(new Paragraph(parade.getTitle() + " --> " + parade.getBrotherhood().getTitle()));
					document.add(new Paragraph("\n"));
					if (marchs != null)
						if (!marchs.isEmpty())
							for (final March march : marchs)
								if (march.getParade().getId() == parade.getId())
									document.add(new Paragraph("Location: Row (" + march.getLocation().get(0) + ") and Column (" + march.getLocation().get(1) + ")"));

				}
			}
		return document;
	}

	public Document docBrotherhood(final Document document, final Brotherhood brotherhood) throws DocumentException, MalformedURLException, IOException {

		final List<Parade> parades = new ArrayList<>(this.paradeService.findParadesByBrotherhoodId(brotherhood.getId()));
		if (parades != null)
			if (!parades.isEmpty()) {
				document.add(new Paragraph("PARADES.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Parade parade : parades) {
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(parade.getTitle() + " --> " + parade.getMomentOrganised()));
					document.add(new Paragraph("\n"));
				}
			}

		final List<Member> members = this.memberService.listMembersByBrotherhood(brotherhood.getId());
		if (parades != null)
			if (!parades.isEmpty()) {
				document.add(new Paragraph("MEMBERS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Member member : members) {
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(member.getUserAccount().getUsername() + ": " + member.getName() + " " + member.getSurname()));
					document.add(new Paragraph("\n"));
				}
			}

		final List<DFloat> dFloats = new ArrayList<>(this.dFloatService.SearchDFloatsByBrotherhood(brotherhood.getId()));
		if (parades != null)
			if (!parades.isEmpty()) {
				document.add(new Paragraph("FLOATS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final DFloat dFloat : dFloats) {
					document.add(new Paragraph("-----------------------"));
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(dFloat.getTitle()));
					document.add(new Paragraph("\n"));
					if (dFloat.getPictures() != null)
						if (!dFloat.getPictures().isEmpty()) {
							final String[] strings = dFloat.getPictures().split(",");
							for (final String string : strings) {
								document.add(this.urlToImage(new URL(string)));
								document.add(new Paragraph("\n"));
							}
						}
				}
			}

		final Area area = this.areaService.findAreaByBrotherhoodId(brotherhood.getId());
		if (area != null) {
			document.add(new Paragraph("AREA.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph(area.getName()));
			if (area.getPictures() != null)
				if (!area.getPictures().isEmpty()) {
					final String[] strings = area.getPictures().split(",");
					for (final String string : strings) {
						document.add(this.urlToImage(new URL(string)));
						document.add(new Paragraph("\n"));
					}
				}
		}

		return document;
	}

	public Document docSponsor(final Document document, final Sponsor sponsor) throws DocumentException, MalformedURLException, IOException {

		final List<Sponsorship> sponsorships = new ArrayList<>(this.sponsorshipService.findSponsorshipsBySponsorId(sponsor.getId()));
		if (sponsorships != null)
			if (!sponsorships.isEmpty()) {
				document.add(new Paragraph("SPONSORSHIPS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				for (final Sponsorship sponsorship : sponsorships) {
					if (StringUtils.isNotEmpty(sponsorship.getBanner()))
						document.add(this.urlToImage(new URL(sponsorship.getBanner())));
					document.add(new Paragraph("\n"));
					if (sponsorship.getActive() == true)
						document.add(new Paragraph("Activated"));
					else
						document.add(new Paragraph("Desactivated"));
					document.add(new Paragraph("\n"));
				}
			}

		return document;
	}

	public Document docChapter(final Document document, final Chapter chapter) throws DocumentException, MalformedURLException, IOException {

		final List<Proclaim> proclaims = new ArrayList<>(this.proclaimService.findProclaimByChapter(chapter.getId()));
		if (proclaims != null)
			if (!proclaims.isEmpty()) {
				document.add(new Paragraph("PROCLAIMS.", new Font(FontFactory.getFont("arial", 22, Font.UNDERLINE))));
				document.add(new Paragraph("\n"));
				int i = 1;
				for (final Proclaim proclaim : proclaims) {
					document.add(new Paragraph("Proclaim" + " " + i + ":"));
					document.add(new Paragraph(proclaim.getText()));
					document.add(new Paragraph("\n"));
					i++;
				}
			}

		return document;
	}

}
