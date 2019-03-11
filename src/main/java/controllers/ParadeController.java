
package controllers;

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
	private ParadeService		paradeService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	//Constructor-----------------------------------------------------------------

	public ParadeController() {
		super();
	}

	//List of Parade all actors-----------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView modelAndView = new ModelAndView("parade/list");

		final SecurityContext context = SecurityContextHolder.getContext();
		Assert.notNull(context);
		final Authentication authentication = context.getAuthentication();

		if (!authentication.getPrincipal().equals("anonymousUser")) {
			final Authority authority = new Authority();
			authority.setAuthority("BROTHERHOOD");
			if (LoginService.getPrincipal().getAuthorities().contains(authority)) {
				final int brotherhoodId = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId()).getId();
				modelAndView.addObject("hasArea", this.areaService.findAreaByBrotherhoodId(brotherhoodId));
			}
		}

		final List<Parade> parades = this.paradeService.findParadesFinal();

		modelAndView.addObject("parades", parades);
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
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		//modelAndView.addObject("requestURI", "parade/listBrotherhood.do");

		return modelAndView;
	}

	@RequestMapping(value = "/listBrotherhoodAllUsers", method = RequestMethod.GET)
	public ModelAndView listBrotherhoodAllUsers(@RequestParam final int brotherhoodId) {
		final ModelAndView modelAndView;

		final List<Parade> parades = this.paradeService.findParadesFinalByBrotherhoodId(brotherhoodId);

		modelAndView = new ModelAndView("parade/list");
		modelAndView.addObject("requestURI", "parade/listBrotherhoodAllUsers.do");
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		modelAndView.addObject("parades", parades);

		return modelAndView;
	}

}
