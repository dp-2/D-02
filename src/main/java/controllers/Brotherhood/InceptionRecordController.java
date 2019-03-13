
package controllers.Brotherhood;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


	//	@RequestMapping("/display")
	//	public ModelAndView display(@RequestParam(required = true) final Integer inceptionRecordId) {
	//		final ModelAndView res = new ModelAndView("inceptionRecord/display");
	//
	//		final InceptionRecord inceptionRecord = this.inceptionService.findOne(inceptionRecordId);
	//
	//		Collection<Url> inceptionRecordPhotos = inceptionRecord.getPhotos();
	//		inceptionRecordPhotos = new ArrayList<Url>();
	//		res.addObject("inceptionRecord", inceptionRecord);
	//		res.addObject("inceptionRecordPhotos", inceptionRecordPhotos);
	//		res.addObject("banner", this.configurationService.findOne().getBanner());
	//		return res;
	//	}

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

}
