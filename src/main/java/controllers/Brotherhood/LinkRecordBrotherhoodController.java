
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
import services.LinkRecordService;
import services.ParadeService;
import controllers.AbstractController;
import domain.History;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class LinkRecordBrotherhoodController extends AbstractController {

	//-----------------Services-------------------------
	@Autowired
	LinkRecordService		linkRecordService;

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
		final Collection<LinkRecord> records;
		final History history = this.historyService.findOne(historyId);

		records = this.linkRecordService.findAllByHistoryId(history.getId());

		result = new ModelAndView("linkRecord/list");
		result.addObject("linkRecords", records);
		result.addObject("history", history);
		result.addObject("requestURI", "linkRecord/brotherhood/list.do");
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//-----------------Display-------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord record;
		record = this.linkRecordService.findOne(linkRecordId);
		result = new ModelAndView("linkRecord/display");
		result.addObject("linkRecord", record);
		result.addObject("linkRecordId", linkRecordId);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		return result;
	}
	//
	//	//------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LinkRecord record;
		record = this.linkRecordService.create();
		result = this.createEditModelAndView(record);
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		ModelAndView result;
		final LinkRecord record;

		record = this.linkRecordService.findOne(linkRecordId);

		Assert.notNull(record);

		result = this.createEditModelAndView(record);
		result.addObject("linkRecordId", linkRecordId);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecord record, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(record);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.linkRecordService.save(record);
				result = new ModelAndView("redirect:list.do?historyId=" + record.getHistory().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(record, "linkRecord.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final LinkRecord record, final String message) {
		final ModelAndView result;

		result = new ModelAndView("linkRecord/edit");

		result.addObject("linkRecord", record);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "periodRecord/brotherhood/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final LinkRecord record) {
		ModelAndView result;

		result = this.createEditModelAndView(record, null);

		return result;
	}
	//
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LinkRecord record, final BindingResult binding) {
		ModelAndView result;
		try {
			this.linkRecordService.delete(record);
			result = new ModelAndView("redirect:list.do?historyId=" + record.getHistory().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(record, "linkRecord.commit.error");

		}
		return result;
	}

}
