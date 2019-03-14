
package controllers.Brotherhood;

import java.util.ArrayList;
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

import services.BrotherhoodService;
import services.ConfigurationService;
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.InceptionRecord;
import domain.Url;

@Controller
@RequestMapping("/inceptionRecord")
public class InceptionRecordController extends AbstractController {

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

		Collection<Url> inceptionRecordPhotos = inceptionRecord.getPhotos();
		inceptionRecordPhotos = new ArrayList<Url>();
		res.addObject("inceptionRecord", inceptionRecord);
		res.addObject("inceptionRecordPhotos", inceptionRecordPhotos);
		res.addObject("banner", this.configurationService.findOne().getBanner());
		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		final ModelAndView res;
		InceptionRecord inceptionRecord;
		inceptionRecord = this.inceptionService.findOne(inceptionRecordId);

		Collection<Url> inceptionRecordPhotos = inceptionRecord.getPhotos();
		inceptionRecordPhotos = new ArrayList<Url>();

		Assert.notNull(inceptionRecord);

		res = this.createEditModelAndView(inceptionRecord);

		res.addObject("inceptionRecord", inceptionRecord);
		res.addObject("inceptionRecordPhotos", inceptionRecordPhotos);

		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(inceptionRecord);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.inceptionService.save(inceptionRecord);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String message) {
		final ModelAndView result;

		result = new ModelAndView("inceptionRecord/edit");

		result.addObject("dfloat", inceptionRecord);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		result.addObject("message", message);
		result.addObject("requestURI", "inceptionRecord/edit.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(inceptionRecord, null);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final InceptionRecord inceptionRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.inceptionService.delete(inceptionRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");

		}
		return result;
	}

}
