
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.PathService;
import services.SegmentService;
import domain.Actor;
import domain.Path;
import domain.Segment;

@RequestMapping("segment/brotherhood")
public class SegmentController extends AbstractController {

	@Autowired
	private SegmentService	segmentService;
	@Autowired
	private PathService		pathService;
	@Autowired
	private ActorService	actorService;


	@RequestMapping("create")
	public ModelAndView create(@RequestParam(required = true) final Integer pathId) {
		final Path path = this.pathService.findOne(pathId);
		Assert.notNull(path);
		final Segment segment = this.segmentService.create(path);
		return this.createEditModelAndView(segment);
	}

	@RequestMapping("edit")
	public ModelAndView edit(@RequestParam(required = true) final Integer segmentId) {
		final Segment segment = this.segmentService.findOne(segmentId);
		Assert.notNull(segment);
		return this.createEditModelAndView(segment);
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Segment segment, final BindingResult binding) {
		ModelAndView res = null;
		final Segment decSegment = this.segmentService.deconstruct(segment, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(decSegment);
		try {
			this.segmentService.save(decSegment);
			res = new ModelAndView("redirect:/path/brotherhood/display.do?pathId=" + new Integer(decSegment.getPath().getId()).toString());
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(decSegment, "cannot.commit.error");
		}
		return res;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Segment segment, final BindingResult binding) {
		ModelAndView res = null;
		final Segment decSegment = this.segmentService.deconstruct(segment, binding);
		try {
			this.segmentService.delete(decSegment);
			res = new ModelAndView("redirect:/");
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(decSegment, "cannot.commit.error");
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

	private ModelAndView createEditModelAndView(final Segment segment) {
		return this.createEditModelAndView(segment, null);
	}

	private ModelAndView createEditModelAndView(final Segment segment, final String message) {
		final ModelAndView res = new ModelAndView("path_and_segments/edit");
		res.addObject("segment", segment);
		res.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(segment.getPath()));
		res.addObject("message", message);
		return res;
	}

}
