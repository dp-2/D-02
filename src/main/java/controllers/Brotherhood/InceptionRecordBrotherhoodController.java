
package controllers.Brotherhood;

import java.util.ArrayList;
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

import services.BrotherhoodService;
import services.ConfigurationService;
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord/brotherhood")
public class InceptionRecordBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private InceptionRecordService	inceptionService;

	@Autowired
	private ConfigurationService	configurationService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(final Integer inceptionRecordId) {
		final ModelAndView res = new ModelAndView("inceptionRecord/display");

		final InceptionRecord inceptionRecord = this.inceptionService.findOne(inceptionRecordId);

		List<String> inceptionRecordPhotos = inceptionRecord.getPhotos();
		inceptionRecordPhotos = new ArrayList<String>();
		res.addObject("inceptionRecord", inceptionRecord);
		res.addObject("inceptionRecordPhotos", inceptionRecordPhotos);
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result;
		final InceptionRecord inceptionRecord;

		inceptionRecord = this.inceptionService.findOne(inceptionRecordId);
		Assert.notNull(inceptionRecord);

		result = this.createEditModelAndView(inceptionRecord);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(inceptionRecord);
			System.out.println(binding.getAllErrors());
			if (binding.getAllErrors().toString().contains("URL"))
				result = this.createEditModelAndView(inceptionRecord, "history.URL.error");
			else
				result = this.createEditModelAndView(inceptionRecord);
		} else
			try {
				this.inceptionService.save(inceptionRecord);
				result = new ModelAndView("redirect:display.do?inceptionRecordId=" + inceptionRecord.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String message) {
		final ModelAndView result;

		result = new ModelAndView("inceptionRecord/edit");

		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "inceptionRecord/brotherhood/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord periodRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(periodRecord, null);

		return result;
	}

}
