
package controllers.Sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SponsorService;
import controllers.AbstractController;
import domain.Actor;
import domain.Sponsor;

@Controller
@RequestMapping("/sponsor")
public class SponsorController extends AbstractController {

	//Services-----------------------------------------------------------

	@Autowired
	private SponsorService	sponsorService;

	@Autowired
	private ActorService	actorService;


	//Constructor---------------------------------------------------------

	public SponsorController() {
		super();
	}

	@RequestMapping(value = "/deleteSponsor", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {

		ModelAndView result;
		final Actor s = this.actorService.findPrincipal();
		try {
			this.sponsorService.deleteSponsor((Sponsor) s);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			System.out.println("NO SE HA PODIDO BORRAR EL USUARIO");
		}
		return result;
	}

}
