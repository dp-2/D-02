
package controllers;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

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
import domain.Segment;

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
		final SortedSet<Segment> segments = new TreeSet<Segment>(new Comparator<Segment>() {

			@Override
			public int compare(final Segment o1, final Segment o2) {
				if (o1.getNumberOrder() > o2.getNumberOrder())
					return 1;
				else if (o1.getNumberOrder() < o2.getNumberOrder())
					return -1;
				else
					return 0;
			}
		});
		segments.addAll(path.getSegments());
		res.addObject("path", path);
		res.addObject("segments", segments);
		res.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(path));
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
