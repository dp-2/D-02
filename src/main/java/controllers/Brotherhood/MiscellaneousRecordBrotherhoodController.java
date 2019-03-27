
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
import services.MiscellaneousRecordService;
import services.ParadeService;
import controllers.AbstractController;
import domain.History;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord/brotherhood")
public class MiscellaneousRecordBrotherhoodController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	BrotherhoodService			brotherhoodService;

	@Autowired
	HistoryService				historyService;
	@Autowired
	ActorService				actorService;

	@Autowired
	ParadeService				paradeService;

	@Autowired
	ConfigurationService		configurationService;


	//-------------------------- List ----------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int historyId) {
		ModelAndView result;
		final Collection<MiscellaneousRecord> miscellaneousRecords;
		final History history = this.historyService.findOne(historyId);

		miscellaneousRecords = this.miscellaneousRecordService.findAllByHistoryId(history.getId());

		result = new ModelAndView("miscellaneousRecord/list");
		result.addObject("miscellaneousRecords", miscellaneousRecords);
		result.addObject("history", history);
		result.addObject("requestURI", "miscellaneousRecord/brotherhood/list.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		result = new ModelAndView("miscellaneousRecord/display");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//
	//	//------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.create();
		result = this.createEditModelAndView(miscellaneousRecord);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		final MiscellaneousRecord miscellaneousRecord;

		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		Assert.notNull(miscellaneousRecord);

		result = this.createEditModelAndView(miscellaneousRecord);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(miscellaneousRecord);
			System.out.println(binding.getAllErrors());
		} else
			try {
				if (this.miscellaneousRecordService.checkEquals(miscellaneousRecord))
					result = this.createEditModelAndView(miscellaneousRecord, "history.equalsRecord.error");
				else {
					this.miscellaneousRecordService.save(miscellaneousRecord);
					result = new ModelAndView("redirect:list.do?historyId=" + miscellaneousRecord.getHistory().getId());
				}
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String message) {
		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");

		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "miscellaneousRecord/brotherhood/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousRecord, null);

		return result;
	}
	//
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.miscellaneousRecordService.delete(miscellaneousRecord);
			result = new ModelAndView("redirect:list.do?historyId=" + miscellaneousRecord.getHistory().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");

		}
		return result;
	}

}
