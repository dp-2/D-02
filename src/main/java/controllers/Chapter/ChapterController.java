
package controllers.Chapter;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChapterService;
import controllers.AbstractController;
import domain.Actor;
import domain.Chapter;
import forms.ActorForm;

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
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create() {
		ModelAndView result;
		final Chapter chapter;

		chapter = this.chapterService.create();

		result = new ModelAndView("actor/edit");
		result.addObject("chapter", chapter);
		return result;
	}

	//------------------Edit---------------------------------------------

	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int chapterId) {
	//		ModelAndView result;
	//		Chapter chapter;
	//
	//		chapter = this.chapterService.findOne(chapterId);
	//		Assert.notNull(chapter);
	//		result = this.createEditModelAndView(chapter);
	//
	//		return result;
	//
	//	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			System.out.println(binding.getAllErrors());
			result = this.createEditModelAndView(actorForm);
		} else

			try {
				final Actor actor = this.actorService.deconstruct(actorForm, binding);
				final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				actor.getUserAccount().setPassword(encoder.encodePassword(actor.getUserAccount().getPassword(), null));
				actor.getUserAccount().setEnabled(true);
				this.actorService.update(actor);
				result = new ModelAndView("redirect:welcome/index.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actorForm, "chapter.commit.error");
			}

		return result;
	}

	private ModelAndView createEditModelAndView(final ActorForm actorForm) {
		return this.createEditModelAndView(actorForm, null);
	}

	private ModelAndView createEditModelAndView(final ActorForm actorForm, final String message) {
		final ModelAndView res = new ModelAndView("chapter/edit");
		final Boolean isPrincipalAuthorizedEdit = this.isPrincipalAuthorizedEdit(actorForm);
		res.addObject("chapterForm", actorForm);
		res.addObject("message", message);
		res.addObject("isPrincipalAuthorizedEdit", isPrincipalAuthorizedEdit);
		return res;
	}

	private Boolean isPrincipalAuthorizedEdit(final ActorForm actorForm) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final IllegalArgumentException e) {
			principal = null;
		}
		if (actorForm.getId() > 0)
			res = principal.getId() == actorForm.getId();
		else if (actorForm.getId() == 0)
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
