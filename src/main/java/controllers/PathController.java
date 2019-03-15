
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ParadeService;
import services.PathService;
import domain.Actor;
import domain.Parade;
import domain.Path;

@Controller
@RequestMapping("path/brotherhood")
public class PathController extends AbstractController {

	@Autowired
	private PathService		pathService;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private ParadeService	paradeService;


	@RequestMapping("display")
	public ModelAndView display(@RequestParam(required = true) final Integer pathId) {
		final Path path = this.pathService.findOne(pathId);
		Assert.notNull(path);
		final ModelAndView res = new ModelAndView("path_and_segments/display");
		res.addObject("path", path);
		res.addObject("isPrincipalAuthotizedEdit", this.isPrincipalAuthorizedEdit(path));
		return res;
	}

	@RequestMapping("create")
	public ModelAndView create(@RequestParam(required = true) final Integer paradeId) {
		final Parade parade = this.paradeService.findOne(paradeId);
		final Path path = this.pathService.create(parade);
		final Path savedPath = this.pathService.save(path);
		final ModelAndView res = new ModelAndView("path_and_segments/display");
		res.addObject("path", savedPath);
		res.addObject("isPrincipalAuthotizedEdit", this.isPrincipalAuthorizedEdit(path));
		return res;
	}

	@RequestMapping("delete")
	public ModelAndView delete(@RequestParam(required = true) final Integer pathId) {
		final Path path = this.pathService.findOne(pathId);
		Assert.notNull(path);
		ModelAndView res = null;
		try {
			this.pathService.delete(path);
			res = new ModelAndView("redirect:/");
		} catch (final Throwable oops) {
			res = new ModelAndView("path_and_segments/display");
			res.addObject("message", "cannot.commit.error");
			res.addObject("isPrincipalAuthotizedEdit", this.isPrincipalAuthorizedEdit(path));
		}
		return res;
	}

	private Boolean isPrincipalAuthorizedEdit(final Path path) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final Throwable oops) {
			principal = null;
		}
		if (path.getParade().getBrotherhood().equals(principal))
			res = true;
		return res;
	}
}
