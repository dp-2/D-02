
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AreaService;
import services.ChapterService;
import services.ConfigurationService;
import domain.Area;

@Controller
@RequestMapping("/area/")
public class AreaController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public AreaController() {
		super();
	}


	//-----------------Services-------------------------

	@Autowired
	AreaService						areaService;

	@Autowired
	ActorService					actorService;

	@Autowired
	ChapterService					chapterService;

	@Autowired
	private ConfigurationService	configurationService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAll();
		result = new ModelAndView("area/list");
		result.addObject("areas", areas);
		result.addObject("requestURI", "area/list.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	@RequestMapping(value = "/chapterList", method = RequestMethod.GET)
	public ModelAndView chapterList(@RequestParam final int chapterId) {
		ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAreaByChapterId(chapterId);
		result = new ModelAndView("area/list");
		result.addObject("areas", areas);
		result.addObject("requestURI", "area/chapterList.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	@RequestMapping(value = "/assign", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int areaId) {

		ModelAndView result;
		final Area area = this.areaService.findOne(areaId);
		try {
			this.areaService.assign(area);
			result = this.list();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(area, "area.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/coordinate", method = RequestMethod.GET)
	public ModelAndView coordinate(@RequestParam final int areaId) {

		ModelAndView result;
		final Area area = this.areaService.findOne(areaId);
		try {
			this.areaService.coordinate(area);
			result = this.list();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(area, "area.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area) {
		ModelAndView result;

		result = this.createEditModelAndView(area, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area, final String message) {
		ModelAndView result;

		result = new ModelAndView("area/edit");
		result.addObject("area", area);
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		result.addObject("requestURI", "area/edit.do");

		return result;
	}
}
