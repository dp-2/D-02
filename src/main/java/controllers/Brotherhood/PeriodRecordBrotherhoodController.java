
package controllers.Brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import services.ParadeService;
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class PeriodRecordBrotherhoodController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	PeriodRecordService		periodRecordService;

	@Autowired
	BrotherhoodService		brotherhoodService;

	@Autowired
	HistoryService			historyService;
	@Autowired
	ActorService			actorService;

	@Autowired
	ParadeService			paradeService;

	@Autowired
	ConfigurationService	configurationService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<PeriodRecord> periodRecords;
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId());
		final History history = this.historyService.findOneByBrotherhoodId(brotherhood.getId());

		periodRecords = this.periodRecordService.findAllByHistoryId(history.getId());

		result = new ModelAndView("periodRecord/list");
		result.addObject("periodRecords", periodRecords);
		result.addObject("requestURI", "periodRecord/brotherhood/list.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//-----------------Display-------------------------

	//	@RequestMapping(value = "/display", method = RequestMethod.GET)
	//	public ModelAndView display(@RequestParam final int dfloatId) {
	//		ModelAndView result;
	//		DFloat dfloat;
	//		Collection<Parade> myParades;
	//		dfloat = this.dfloatService.findOne(dfloatId);
	//		myParades = dfloat.getParades();
	//		result = new ModelAndView("dfloat/display");
	//		result.addObject("dfloat", dfloat);
	//		result.addObject("myParades", myParades);
	//		result.addObject("banner", this.configurationService.findOne().getBanner());
	//
	//		return result;
	//	}
	//
	//	//------------------------------------------
	//	@RequestMapping(value = "/create", method = RequestMethod.GET)
	//	public ModelAndView create() {
	//		ModelAndView result;
	//		DFloat er;
	//		er = this.dfloatService.create();
	//		result = this.createEditModelAndView(er);
	//		return result;
	//
	//	}
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int dfloatId) {
	//		ModelAndView result;
	//		DFloat er;
	//
	//		Collection<Parade> allParades;
	//		final Collection<Parade> myParades;
	//
	//		er = this.dfloatService.findOne(dfloatId);
	//		Assert.notNull(er);
	//		final Brotherhood br = er.getBrotherhood();
	//		allParades = this.paradeService.findParadesByBrotherhoodId(br.getId());
	//		myParades = er.getParades();
	//
	//		result = this.createEditModelAndView(er);
	//		result.addObject("allParades", allParades);
	//		result.addObject("myParades", myParades);
	//
	//		return result;
	//
	//	}
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	//	public ModelAndView save(@Valid final DFloat dfloat, final BindingResult binding) {
	//		ModelAndView result;
	//
	//		if (binding.hasErrors()) {
	//			result = this.createEditModelAndView(dfloat);
	//			System.out.println(binding.getAllErrors());
	//		} else
	//			try {
	//				this.dfloatService.save(dfloat);
	//				result = new ModelAndView("redirect:list.do");
	//			} catch (final Throwable oops) {
	//				result = this.createEditModelAndView(dfloat, "dfloat.commit.error");
	//			}
	//		return result;
	//	}
	//
	//	protected ModelAndView createEditModelAndView(final DFloat dfloat, final String message) {
	//		final ModelAndView result;
	//
	//		result = new ModelAndView("dfloat/edit");
	//
	//		result.addObject("dfloat", dfloat);
	//		result.addObject("banner", this.configurationService.findOne().getBanner());
	//		result.addObject("message", message);
	//		result.addObject("requestURI", "dfloat/brotherhood/edit.do");
	//
	//		return result;
	//	}
	//
	//	protected ModelAndView createEditModelAndView(final DFloat dfloat) {
	//		ModelAndView result;
	//
	//		result = this.createEditModelAndView(dfloat, null);
	//
	//		return result;
	//	}
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	//	public ModelAndView delete(final DFloat dfloat, final BindingResult binding) {
	//		ModelAndView result;
	//		try {
	//			this.dfloatService.delete(dfloat);
	//			result = new ModelAndView("redirect:list.do");
	//		} catch (final Throwable oops) {
	//			result = this.createEditModelAndView(dfloat, "dfloat.commit.error");
	//
	//		}
	//		return result;
	//	}

}
