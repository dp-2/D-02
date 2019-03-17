
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Controller
@RequestMapping("/history")
public class HistoryController extends AbstractController {

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
	public ModelAndView display(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		History history;

		history = this.historyService.findOneByBrotherhoodId(brotherhoodId);

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

}
