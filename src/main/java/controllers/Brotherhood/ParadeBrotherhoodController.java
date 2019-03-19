
package controllers.Brotherhood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.DFloatService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Actor;
import domain.DFloat;
import domain.Parade;
import forms.ParadeFloatForm;

@Controller
@RequestMapping("/parade/brotherhood")
public class ParadeBrotherhoodController extends AbstractController {

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

	public ParadeBrotherhoodController() {
		super();
	}

	//List of all parade by principal brotherhood---------------------

	@RequestMapping(value = "/myList", method = RequestMethod.GET)
	public ModelAndView myList() {
		final ModelAndView modelAndView = new ModelAndView("parade/list");
		final int brotherhoodId = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId()).getId();

		final List<Parade> parades = this.paradeService.findParadesByBrotherhoodId(brotherhoodId);

		modelAndView.addObject("parades", parades);
		modelAndView.addObject("paradeService", this.paradeService);
		modelAndView.addObject("banner", this.configurationService.findOne().getBanner());
		modelAndView.addObject("requestURI", "parade/brotherhood/myList.do");
		modelAndView.addObject("numResults", this.configurationService.findOne().getNumResults());
		modelAndView.addObject("hasArea", this.areaService.findAreaByBrotherhoodId(brotherhoodId));
		modelAndView.addObject("brotherhoodId", brotherhoodId);

		return modelAndView;
	}

	//Create a Parade------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView modelAndView;

		final Parade parade = this.paradeService.create();
		Assert.notNull(parade);

		modelAndView = this.createEditModelAndView(parade);

		return modelAndView;

	}

	//Show a Parade------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int paradeId) {
		final ModelAndView modelAndView;

		final Parade parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);

		modelAndView = this.createEditModelAndView(parade);
		modelAndView.addObject("requestURI", "parade/brotherhood/show.do");
		modelAndView.addObject("isRead", true);

		return modelAndView;

	}

	//Edit a Parade------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		final ModelAndView modelAndView;

		final Parade parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);

		modelAndView = this.createEditModelAndView(parade);
		modelAndView.addObject("requestURI", "parade/brotherhood/edit.do");

		return modelAndView;

	}

	//Save a Parade-----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Parade parade, final BindingResult bindingResult) {
		ModelAndView modelAndView;

		if (bindingResult.hasErrors())
			modelAndView = this.createEditModelAndView(parade);
		else
			try {

				this.paradeService.save(parade);
				modelAndView = this.myList();
			} catch (final Exception e) {
				if (e.getMessage().equals("noOwner"))
					modelAndView = this.createEditModelAndView(parade, "parade.error.owner");
				else if (e.getMessage().equals("noFinal"))
					modelAndView = this.createEditModelAndView(parade, "parade.error.final");
				else if (e.getMessage().equals("noFuture"))
					modelAndView = this.createEditModelAndView(parade, "parade.error.moment");
				else
					modelAndView = this.createEditModelAndView(parade, "parade.commit.error");
			}
		return modelAndView;
	}

	//Save a Parade-----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final Parade parade, final BindingResult bindingResult) {
		ModelAndView modelAndView;

		if (bindingResult.hasErrors())
			modelAndView = this.createEditModelAndView(parade);
		else
			try {
				this.paradeService.delete(parade);
				modelAndView = this.myList();
			} catch (final Exception e) {
				if (e.getMessage().equals("noOwner"))
					modelAndView = this.createEditModelAndView(parade, "parade.error.owner");
				else if (e.getMessage().equals("noFinal"))
					modelAndView = this.createEditModelAndView(parade, "parade.error.final");
				else
					modelAndView = this.createEditModelAndView(parade, "parade.commit.error");
			}
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

	@RequestMapping("addFloat")
	public ModelAndView addFloatEdit(@RequestParam(required = true) final Integer paradeId) {
		ModelAndView res = null;
		final Parade parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);
		res = this.createAddFloatModelAndView(parade, null);
		return res;
	}

	@RequestMapping("removeFloat")
	public ModelAndView removeFloatEdit(@RequestParam(required = true) final Integer paradeId) {
		ModelAndView res = null;
		final Parade parade = this.paradeService.findOne(paradeId);
		Assert.notNull(parade);
		res = this.createRemoveFloatModelAndView(parade, null);
		return res;
	}

	@RequestMapping(value = "addFloat", method = RequestMethod.POST, params = "add")
	public ModelAndView addFloatSave(@Valid final ParadeFloatForm paradeFloatForm, final BindingResult binding) {
		ModelAndView res = null;
		try {
			final DFloat dFloat = paradeFloatForm.getDFloat();
			final Parade parade = paradeFloatForm.getParade();
			if (!(dFloat.getParades() == null))
				dFloat.setParades(new ArrayList<Parade>());
			if (!dFloat.getParades().contains(parade)) {
				final Collection<Parade> parades = new ArrayList<Parade>();
				parades.addAll(dFloat.getParades());
				parades.add(parade);
				dFloat.setParades(parades);
			}
			this.dFloatService.save(dFloat);
			res = new ModelAndView("redirect:/parade/list.do");
		} catch (final Throwable oops) {
			res = this.createAddFloatModelAndView(paradeFloatForm.getParade(), "cannot.commit.error");
		}
		return res;
	}
	@RequestMapping(value = "removeFloat", method = RequestMethod.POST, params = "remove")
	public ModelAndView removeFloatSave(@Valid final ParadeFloatForm paradeFloatForm, final BindingResult binding) {
		ModelAndView res = null;
		try {
			final DFloat dFloat = paradeFloatForm.getDFloat();
			final Parade parade = paradeFloatForm.getParade();
			if (dFloat.getParades().contains(parade)) {
				final Collection<Parade> parades = new ArrayList<Parade>();
				parades.addAll(dFloat.getParades());
				parades.remove(parade);
				dFloat.setParades(parades);
			}
			this.dFloatService.save(dFloat);
			res = new ModelAndView("redirect:/parade/list.do");
		} catch (final Throwable oops) {
			res = this.createAddFloatModelAndView(paradeFloatForm.getParade(), "cannot.commit.error");
		}
		return res;
	}

	protected ModelAndView createAddFloatModelAndView(final Parade parade, final String message) {
		ModelAndView result;

		final ParadeFloatForm paradeFloatForm = new ParadeFloatForm();
		paradeFloatForm.setParade(parade);

		result = new ModelAndView("parade/add");
		result.addObject("parade", parade);
		result.addObject("paradeFloatForm", paradeFloatForm);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("requestURI", "parade/brotherhood/addFloat.do");
		result.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(parade));
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final Throwable oops) {
			principal = null;
		}
		result.addObject("floatsForAdd", this.dFloatService.searchFloatNotInParadeByIdByActorId(parade, principal));
		return result;
	}
	protected ModelAndView createRemoveFloatModelAndView(final Parade parade, final String message) {
		ModelAndView result;

		final ParadeFloatForm paradeFloatForm = new ParadeFloatForm();
		paradeFloatForm.setParade(parade);

		result = new ModelAndView("parade/remove");
		result.addObject("parade", parade);
		result.addObject("paradeFloatForm", paradeFloatForm);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("isRead", false);
		result.addObject("requestURI", "parade/brotherhood/addFloat.do");
		result.addObject("isPrincipalAuthorizedEdit", this.isPrincipalAuthorizedEdit(parade));
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final Throwable oops) {
			principal = null;
		}
		result.addObject("floatsForRemove", this.dFloatService.searchFloatInParadeByIdByActorId(parade, principal));
		return result;
	}

	private Boolean isPrincipalAuthorizedEdit(final Parade parade) {
		Boolean res = false;
		Actor principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final Throwable oops) {
			principal = null;
		}
		if (principal.equals(parade.getBrotherhood()) && !parade.isFfinal())
			res = true;
		return res;
	}

}
