
package controllers.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChapterService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Actor;
import domain.Chapter;
import forms.ChapterForm;

@Controller
@RequestMapping("chapter")
public class ChapterController extends AbstractController {

	@Autowired
	private ChapterService			chapterService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping("none/create")
	public ModelAndView create() {
		final Chapter chapter = this.chapterService.create();
		final ChapterForm chapterForm = this.chapterService.construct(chapter);
		return this.createEditModelAndView(chapterForm);
	}

	private ModelAndView createEditModelAndView(final ChapterForm chapterForm) {
		return this.createEditModelAndView(chapterForm, null);
	}

	private ModelAndView createEditModelAndView(final ChapterForm chapterForm, final String message) {
		final ModelAndView res = new ModelAndView("chapter/edit");
		final Boolean isPrincipalAuthorizedEdit = this.isPrincipalAuthorizedEdit(chapterForm);
		res.addObject("chapterForm", chapterForm);
		res.addObject("message", message);
		res.addObject("banner", this.configurationService.findOne().getBanner());
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
