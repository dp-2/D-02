
package controllers.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.DFloatService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Parade;

@Controller
@RequestMapping("/parade/chapter")
public class ParadeChapterController extends AbstractController {

	//Services-----------------------------------------------------------

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private DFloatService			dFloatService;

	@Autowired
	private ActorService			actorService;


	//Constructor---------------------------------------------------------

	public ParadeChapterController() {
		super();
	}

	//Edit a Parade------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		final ModelAndView modelAndView;

		final Parade parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);

		modelAndView = this.createEditModelAndView(parade);
		modelAndView.addObject("requestURI", "parade/chapter/edit.do");

		return modelAndView;

	}

	//ModelAndView-----------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createEditModelAndView(parade, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String message) {
		ModelAndView result;

		result = new ModelAndView("parade/edit");
		result.addObject("parade", parade);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("isRead", false);

		result.addObject("requestURI", "parade/brotherhood/edit.do");

		return result;
	}
}
