
package controllers.Sponsor;

import java.util.Collection;

import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.ConfigurationService;
import services.ParadeService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Actor;
import domain.Parade;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	//Services-----------------------------------------------------------

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private ConfigurationService	configurationService;


	//Constructor---------------------------------------------------------

	public SponsorshipSponsorController() {
		super();
	}
	//List ---------------------------------------------------------------
	@RequestMapping(value = "/MyList", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("sponsorship/list");
		Collection<Sponsorship> sponsorships;

		final Actor a = this.actorService.findByUserAccount(LoginService.getPrincipal());
		final int sponsorId = a.getId();
		sponsorships = this.sponsorshipService.findSponsorshipsBySponsorId(sponsorId);
		for (final Sponsorship s : sponsorships) {
			if (s.getCreditCard().getExpirationYear() < LocalDate.now().getYear()) {
				s.setActive(false);
				this.sponsorshipService.save1(s);
			} else if (s.getCreditCard().getExpirationYear() == LocalDate.now().getYear() && s.getCreditCard().getExpirationMonth() < LocalDate.now().getMonthOfYear()) {
				s.setActive(false);
				this.sponsorshipService.save1(s);

			} else {
				s.setActive(s.getActive());
				this.sponsorshipService.save1(s);
			}

			result.addObject("sponsorships", sponsorships);
			result.addObject("sponsorId", sponsorId);
			result.addObject("requestURI", "sponsorship/sponsor/MyList.do");
		}
		return result;

	}
	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Sponsorship sponsorship;

		final Actor a = this.actorService.findByUserAccount(LoginService.getPrincipal());
		sponsorship = this.sponsorshipService.create(a.getId());
		result = this.createEditModelAndView(sponsorship);

		return result;
	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		Assert.notNull(sponsorship);
		result = this.createEditModelAndView(sponsorship);

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Sponsorship sponsorship, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:/sponsorship/sponsor/MyList.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("errorCredit"))
					result = this.createEditModelAndView(sponsorship, "sponsorship.commit.errorCredit");
				else
					result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");

			}
		return result;
	}

	@RequestMapping(value = "/deActive", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int sponsorshipId) {

		ModelAndView result;
		final Sponsorship sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		try {
			this.sponsorshipService.deActive(sponsorship);
			result = this.list();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String message) {
		ModelAndView result;
		final Collection<Parade> parades = this.paradeService.findParadesAccepted();
		final Collection<String> makeName = this.configurationService.findOne().getMakeName();
		final Double flatFare = this.configurationService.flatFareWithVAT();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("parades", parades);
		result.addObject("makeName", makeName);
		result.addObject("flatFare", flatFare);
		result.addObject("requestURI", "sponsorship/sponsor/edit.do");

		return result;
	}

}
