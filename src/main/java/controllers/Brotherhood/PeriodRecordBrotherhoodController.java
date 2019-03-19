
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

import services.ActorService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import services.ParadeService;
import services.PeriodRecordService;
import controllers.AbstractController;
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
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView result;
		final Collection<PeriodRecord> periodRecords;
		final History history = this.historyService.findOne(historyId);

		periodRecords = this.periodRecordService.findAllByHistoryId(history.getId());

		result = new ModelAndView("periodRecord/list");
		result.addObject("periodRecords", periodRecords);
		result.addObject("history", history);
		result.addObject("requestURI", "periodRecord/brotherhood/list.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int periodRecordId) {
		ModelAndView result;
		PeriodRecord periodRecord;
		periodRecord = this.periodRecordService.findOne(periodRecordId);
		result = new ModelAndView("periodRecord/display");
		result.addObject("periodRecord", periodRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//
	//	//------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PeriodRecord periodRecord;
		periodRecord = this.periodRecordService.create();
		result = this.createEditModelAndView(periodRecord);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId) {
		ModelAndView result;
		final PeriodRecord periodRecord;

		periodRecord = this.periodRecordService.findOne(periodRecordId);
		Assert.notNull(periodRecord);

		result = this.createEditModelAndView(periodRecord);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(periodRecord);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.periodRecordService.save(periodRecord);
				result = new ModelAndView("redirect:list.do?historyId=" + periodRecord.getHistory().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String message) {
		final ModelAndView result;

		result = new ModelAndView("periodRecord/edit");

		result.addObject("periodRecord", periodRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "periodRecord/brotherhood/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(periodRecord, null);

		return result;
	}
	//
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PeriodRecord periodRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.periodRecordService.delete(periodRecord);
			result = new ModelAndView("redirect:list.do?historyId=" + periodRecord.getHistory().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");

		}
		return result;
	}

}
