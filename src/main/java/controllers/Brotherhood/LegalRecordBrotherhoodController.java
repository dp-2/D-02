
package controllers.Brotherhood;

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

import security.LoginService;
import services.ActorService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import services.LegalRecordService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class LegalRecordBrotherhoodController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	LegalRecordService		legalRecordService;

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
		final Collection<LegalRecord> legalRecords;
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId());
		final History history = this.historyService.findOneByBrotherhoodId(brotherhood.getId());

		legalRecords = this.legalRecordService.findAllByHistoryId(history.getId());

		result = new ModelAndView("legalRecord/list");
		result.addObject("legalRecords", legalRecords);
		result.addObject("requestURI", "legalRecord/brotherhood/list.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord;
		legalRecord = this.legalRecordService.findOne(legalRecordId);
		result = new ModelAndView("legalRecord/display");
		result.addObject("legalRecord", legalRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//
	//	//------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LegalRecord legalRecord;
		legalRecord = this.legalRecordService.create();
		result = this.createEditModelAndView(legalRecord);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		ModelAndView result;
		final LegalRecord legalRecord;

		legalRecord = this.legalRecordService.findOne(legalRecordId);
		Assert.notNull(legalRecord);

		result = this.createEditModelAndView(legalRecord);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(legalRecord);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.legalRecordService.save(legalRecord);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String message) {
		final ModelAndView result;

		result = new ModelAndView("legalRecord/edit");

		result.addObject("legalRecord", legalRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "legalRecord/brotherhood/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(legalRecord, null);

		return result;
	}
	//
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LegalRecord legalRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.legalRecordService.delete(legalRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");

		}
		return result;
	}

}
