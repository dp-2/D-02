
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.ParadeService;
import domain.Brotherhood;
import domain.Parade;

@Controller
@RequestMapping("/parade")
public class ParadeController extends AbstractController {

	//Services--------------------------------------------------------------------

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	//Constructor-----------------------------------------------------------------

	public ParadeController() {
		super();
	}

	//List of Parade all actors-----------------------------------------------

	@RequestMapping(value = "/chapterList", method = RequestMethod.GET)
	public ModelAndView chapterList(@RequestParam final int chapterId) {
		ModelAndView result;
		final List<Parade> parades;
		final Collection<Brotherhood> brotherhoods;

		parades = this.paradeService.findAll();
		brotherhoods = this.areaService.findBrotherhoodByChapterId(chapterId);
		final List<Parade> paradesFinales = new ArrayList<Parade>();
		int i = 0;
		while (i < parades.size()) {
			final Brotherhood b = parades.get(i).getBrotherhood();
			if (brotherhoods.contains(b) && (parades.get(i).getStatus().equals("ACCEPTED")))
				paradesFinales.add(parades.get(i));
			i++;
		}
		result = new ModelAndView("parade/list");
		result.addObject("paradeService", this.paradeService);
		result.addObject("parades", paradesFinales);
		result.addObject("requestURI", "parade/chapterList.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	//	@RequestMapping(value = "/list", method = RequestMethod.GET)
	//	public ModelAndView list() {
	//		ModelAndView result;
	//		final List<Parade> parades;
	//		final Collection<Brotherhood> brotherhoods;
	//		final int chapterId;
	//
	//		chapterId = this.actorService.findPrincipal().getId();
	//		parades = this.paradeService.findAll();
	//		brotherhoods = this.areaService.findBrotherhoodByChapterId(chapterId);
	//		final List<Parade> paradesFinales = new ArrayList<Parade>();
	//		//		int i = 0;
	//		for (final Parade p : parades) {
	//			final Brotherhood b = p.getBrotherhood();
	//			if (brotherhoods.contains(b))
	//				paradesFinales.add(p);
	//		}
	//		result = new ModelAndView("parade/list");
	//		result.addObject("paradeService", this.paradeService);
	//		result.addObject("parades", paradesFinales);
	//		result.addObject("requestURI", "parade/list.do");
	//		result.addObject("banner", this.configurationService.findOne().getBanner());
	//
	//		return result;
	//	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView = new ModelAndView("parade/list");

		final SecurityContext context = SecurityContextHolder.getContext();
		Assert.notNull(context);
		final Authentication authentication = context.getAuthentication();
		final UserAccount chapter;
		final int chapterId;

		final List<Parade> parades = this.paradeService.findParadesFinal();
		final List<Parade> parades2;
		final Collection<Brotherhood> brotherhoods;
		final List<Parade> paradesFinales = new ArrayList<Parade>();

		if (LoginService.getPrincipal().getAuthorities().contains("CHAPTER")) {

			chapterId = this.actorService.findPrincipal().getId();
			parades2 = this.paradeService.findAll();
			brotherhoods = this.areaService.findBrotherhoodByChapterId(chapterId);
			int i = 0;
			while (i < parades2.size()) {
				final Brotherhood b = parades2.get(i).getBrotherhood();
				if (brotherhoods.contains(b))
					paradesFinales.add(parades2.get(i));
				i++;
			}
		}
		if (authentication.getPrincipal().equals("anonymousUser")) {
			final Authority authority = new Authority();
			authority.setAuthority("BROTHERHOOD");
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				final int brotherhoodId = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId()).getId();
				modelAndView.addObject("hasArea", this.areaService.findAreaByBrotherhoodId(brotherhoodId));
			}
		}

		modelAndView.addObject("paradeService", this.paradeService);
		modelAndView.addObject("parades", parades);
		modelAndView.addObject("paradesFinales", paradesFinales);
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		modelAndView.addObject("numResults", this.configurationService.findOne().getNumResults());
		modelAndView.addObject("requestURI", "parade/list.do");

		return modelAndView;
	}

	//List of Parade Navigation Brotherhood-----------------------------------------------
	@RequestMapping(value = "/listBrotherhood", method = RequestMethod.GET)
	public ModelAndView listBrotherhood(final int paradeId) {
		final ModelAndView modelAndView = new ModelAndView("parade/list");

		final Brotherhood brotherhood = this.paradeService.findOne(paradeId).getBrotherhood();
		final List<Parade> parades = this.paradeService.findParadesFinalByBrotherhoodId(brotherhood.getId());

		modelAndView.addObject("parades", parades);
		modelAndView.addObject("paradeService", this.paradeService);
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		//modelAndView.addObject("requestURI", "parade/listBrotherhood.do");

		return modelAndView;
	}

	@RequestMapping(value = "/listBrotherhoodAllUsers", method = RequestMethod.GET)
	public ModelAndView listBrotherhoodAllUsers(@RequestParam final int brotherhoodId) {
		final ModelAndView modelAndView;

		final List<Parade> parades = this.paradeService.findParadesFinalByBrotherhoodId(brotherhoodId);

		modelAndView = new ModelAndView("parade/list");
		modelAndView.addObject("paradeService", this.paradeService);
		modelAndView.addObject("requestURI", "parade/listBrotherhoodAllUsers.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		modelAndView.addObject("parades", parades);

		return modelAndView;
	}

	//Model and View-------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createEditModelAndView(parade, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String message) {
		ModelAndView result;

		result = new ModelAndView("parade/edit");
		result.addObject("parade", parade);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("requestURI", "parade/edit.do");

		return result;
	}

}
