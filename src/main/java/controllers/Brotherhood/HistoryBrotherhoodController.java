
package controllers.Brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.HistoryService;
import services.InceptionRecordService;
import services.LegalRecordService;
import services.LinkRecordService;
import services.MiscellaneousRecordService;
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Controller
@RequestMapping("/history/brotherhood")
public class HistoryBrotherhoodController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	HistoryService				historyService;

	@Autowired
	BrotherhoodService			brotherhoodService;

	@Autowired
	ActorService				actorService;

	@Autowired
	InceptionRecordService		inceptionRecordService;

	@Autowired
	PeriodRecordService			periodRecordService;

	@Autowired
	MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	LinkRecordService			linkRecordService;

	@Autowired
	LegalRecordService			legalRecordService;

	@Autowired
	ConfigurationService		configurationService;


	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int historyId) {
		ModelAndView result;
		History history;

		history = this.historyService.findOne(historyId);

		final InceptionRecord inceptionRecord = this.inceptionRecordService.getInceptionRecordByBrotherhood(history.getId());
		final Collection<PeriodRecord> periodRecords = this.periodRecordService.findAllByHistoryId(history.getId());
		final Collection<LinkRecord> linkRecords = this.linkRecordService.findAllByHistoryId(history.getId());
		final Collection<MiscellaneousRecord> miscellaneousRecords = this.miscellaneousRecordService.findAllByHistoryId(history.getId());
		final Collection<LegalRecord> legalRecords = this.legalRecordService.findAllByHistoryId(history.getId());

		result = new ModelAndView("history/display");
		result.addObject("history", history);
		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("linkRecords", linkRecords);
		result.addObject("periodRecords", periodRecords);
		result.addObject("miscellaneousRecords", miscellaneousRecords);
		result.addObject("legalRecords", legalRecords);

		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int historyId) {
		ModelAndView result;
		final History history;

		history = this.historyService.findOne(historyId);
		Assert.notNull(history);

		result = this.createEditModelAndView(history);

		return result;

	}

	protected ModelAndView createEditModelAndView(final History history, final String message) {
		final ModelAndView result;

		result = new ModelAndView("history/edit");

		result.addObject("history", history);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "history/brotherhood/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final History history) {
		ModelAndView result;

		result = this.createEditModelAndView(history, null);

		return result;
	}

}
