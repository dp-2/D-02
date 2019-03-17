
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.PathService;
import domain.Actor;
import domain.Parade;
import domain.Path;

@Controller
@RequestMapping("path/any")
public class PathAnyController extends AbstractController {

	@Autowired
	private PathService		pathService;
	@Autowired
	private ActorService	actorService;


	@RequestMapping("display")
	public ModelAndView display(@RequestParam(required = false) final Integer pathId, @RequestParam(required = false) final Integer paradeId) {
		Path path = null;
		if (pathId != null)
			path = this.pathService.findOne(pathId);
		else if (paradeId != null)
			path = this.pathService.findByParadeId(paradeId);
		Assert.notNull(path);
		final ModelAndView res = new ModelAndView("path_and_segments/display");
		res.addObject("path", path);
		res.addObject("isPrincipalAuthotizedEdit", this.isPrincipalAuthorizedEdit(path));
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

	private Boolean isPrincipalAuthorizedEdit(final Parade parade) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final Throwable oops) {
			principal = null;
		}
		if (parade.getBrotherhood().equals(principal))
			res = true;
		return res;
	}

}
