
package controllers.Chapter;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChapterService;
import controllers.AbstractController;
import domain.Actor;
import domain.Chapter;
import forms.ChapterForm;

@Controller
@RequestMapping("chapter")
public class ChapterController extends AbstractController {

	@Autowired
	private ChapterService	chapterService;
	@Autowired
	private ActorService	actorService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Chapter> chapters;

		chapters = this.chapterService.findAll();
		result = new ModelAndView("chapter/list");
		result.addObject("chapters", chapters);
		result.addObject("requestURI", "chapter/list.do");

		return result;
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Chapter chapter;

		chapter = this.chapterService.create();

		result = new ModelAndView("chapter/create");
		result.addObject("chapter", chapter);
		return result;
	}

	//------------------Edit---------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int chapterId) {
		ModelAndView result;
		Chapter chapter;

		chapter = this.chapterService.findOne(chapterId);
		Assert.notNull(chapter);
		result = this.createEditModelAndView(chapter);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Chapter chapter, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			System.out.println(binding.getAllErrors());
			result = this.createEditModelAndView(chapter);
			result.addObject("message", "chapter.commit.error");
		} else

			try {

				this.chapterService.save(chapter);
				result = new ModelAndView("redirect:welcome/index.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chapter, "chapter.commit.error");
			}

		return result;
	}

	private ModelAndView createEditModelAndView(final Chapter chapter) {
		return this.createEditModelAndView(chapter, null);
	}

	private ModelAndView createEditModelAndView(final Chapter chapter, final String message) {
		final ModelAndView res = new ModelAndView("chapter/edit");
		final Boolean isPrincipalAuthorizedEdit = this.isPrincipalAuthorizedEdit(chapter);
		res.addObject("chapterForm", chapter);
		res.addObject("message", message);
		res.addObject("isPrincipalAuthorizedEdit", isPrincipalAuthorizedEdit);
		return res;
	}

	private Boolean isPrincipalAuthorizedEdit(final ChapterForm chapterForm) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (chapterForm.getId() > 0)
			res = principal.getId() == chapterForm.getId();
		else if (chapterForm.getId() == 0)
			res = principal == null;
		return res;
	}

	private Boolean isPrincipalAuthorizedEdit(final Chapter chapter) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (chapter.getId() > 0 && principal != null)
			res = principal.getId() == chapter.getId();
		else if (chapter.getId() == 0)
			res = principal == null;
		return res;
	}
}
