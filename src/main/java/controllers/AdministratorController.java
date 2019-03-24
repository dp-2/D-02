/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.AreaService;
import services.BrotherhoodService;
import services.ChapterService;
import services.ConfigurationService;
import services.EnrollService;
import services.FinderService;
import services.MarchService;
import services.MemberService;
import services.ParadeService;
import services.PeriodRecordService;
import services.PositionService;
import services.SponsorshipService;
import services.WarningService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Enroll;
import domain.Member;
import domain.Parade;
import domain.Warning;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private PositionService			positionService;

	@Autowired
	private ParadeService			paradeService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private EnrollService			enrollService;

	@Autowired
	private MarchService			marchService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private AreaService				areaService;

	@Autowired
	private FinderService			finderService;

	@Autowired
	public WarningService			warningService;

	@Autowired
	public PeriodRecordService		periodRecordService;

	@Autowired
	public SponsorshipService		sponsorshipService;

	@Autowired
	private ChapterService			chapterService;


	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	@RequestMapping(value = "/scores", method = RequestMethod.GET)
	public ModelAndView scores() {
		ModelAndView result;
		this.administratorService.generateAllScore();
		result = new ModelAndView("administrator/scores");
		final Collection<Actor> actors = this.actorService.findAllTypes();
		result.addObject("actors", actors);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		return result;
	}

	@RequestMapping(value = "/spammers", method = RequestMethod.GET)
	public ModelAndView spammers() {
		ModelAndView result;
		this.administratorService.generateAllSpammers();
		result = new ModelAndView("administrator/spammers");
		final Collection<Actor> actors = this.actorService.findAllTypes();
		result.addObject("actors", actors);
		result.addObject("banner", this.configurationService.findOne().getBanner());
		return result;
	}
	// Dashboard---------------------------------------------------------------

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView action1() throws ParseException {
		ModelAndView result;
		result = new ModelAndView("administrator/dashboard");
		final DecimalFormat df = new DecimalFormat("0.00");
		Map<String, Double> statistics;

		statistics = this.positionService.computeStatistics();
		result.addObject("statistics", statistics);
		result.addObject("banner", this.configurationService.findOne().getBanner());

		//-----------------------Members per brotherhood

		final Double maxMembersPerBrotherhood = this.memberService.membersBrotherhoodStats().get("MAX");
		final Double minMembersPerBrotherhood = this.memberService.membersBrotherhoodStats().get("MIN");
		final Double avgMembersPerBrotherhood = this.memberService.membersBrotherhoodStats().get("AVG");
		final Double stdevMembersPerBrotherhood = this.memberService.membersBrotherhoodStats().get("STD");

		result.addObject("maxMembersPerBrotherhood", maxMembersPerBrotherhood);
		result.addObject("minMembersPerBrotherhood", minMembersPerBrotherhood);
		result.addObject("avgMembersPerBrotherhood", avgMembersPerBrotherhood);
		result.addObject("stdevMembersPerBrotherhood", stdevMembersPerBrotherhood);

		//--------------Largest and smallest brotherhood
		final Brotherhood largestBrotherhood = this.brotherhoodService.BrotherhoodWithMoreMembers();
		final Brotherhood smallestBrotherhood = this.brotherhoodService.BrotherhoodWithLessMembers();

		final List<Enroll> enrollsLarg = (List<Enroll>) this.enrollService.findEnrollsAprovedByBrotherhood(largestBrotherhood.getId());
		final int largestBrotherhoodNumMembers = enrollsLarg.size();

		final List<Enroll> enrollsSmall = (List<Enroll>) this.enrollService.findEnrollsAprovedByBrotherhood(smallestBrotherhood.getId());
		final int smallestBrotherhoodNumMembers = enrollsSmall.size();

		result.addObject("largestBrotherhood", largestBrotherhood);
		result.addObject("smallestBrotherhood", smallestBrotherhood);
		result.addObject("largestBrotherhoodNumMembers", largestBrotherhoodNumMembers);
		result.addObject("smallestBrotherhoodNumMembers", smallestBrotherhoodNumMembers);

		//-----------------------Position Statics
		final Double positionCountTotal = this.positionService.computeStatistics().get("count.total");
		final Double positionCountPresident = this.positionService.computeStatistics().get("count.president");
		final Double positionCountVicepresident = this.positionService.computeStatistics().get("count.vicepresident");
		final Double positionCountSecretary = this.positionService.computeStatistics().get("count.secretary");
		final Double positionCountTreasurer = this.positionService.computeStatistics().get("count.treasurer");
		final Double positionCountHistorian = this.positionService.computeStatistics().get("count.historian");
		final Double positionCountOfficer = this.positionService.computeStatistics().get("count.officer");
		final Double positionCountVocal = this.positionService.computeStatistics().get("count.vocal");

		result.addObject("positionCountTotal", positionCountTotal);
		result.addObject("positionCountPresident", positionCountPresident);
		result.addObject("positionCountVicepresident", positionCountVicepresident);
		result.addObject("positionCountSecretary", positionCountSecretary);
		result.addObject("positionCountTreasurer", positionCountTreasurer);
		result.addObject("positionCountHistorian", positionCountHistorian);
		result.addObject("positionCountOfficer", positionCountOfficer);
		result.addObject("positionCountVocal", positionCountVocal);

		//-----------------------Parades Statics
		final List<Parade> paradesIn30Days = this.paradeService.findParadesIn30Days();
		result.addObject("paradesIn30Days", paradesIn30Days);

		//-----------------------March Statics
		final List<Member> members10RequestAccepted = this.marchService.members10PerMarchAccepted();
		final Double ratioRequestByStatusAPPROVED = this.marchService.ratioRequestByStatus().get(0);
		final Double ratioRequestByStatusPENDING = this.marchService.ratioRequestByStatus().get(1);
		final Double ratioRequestByStatusREJECTED = this.marchService.ratioRequestByStatus().get(2);
		result.addObject("members10RequestAccepted", members10RequestAccepted);
		result.addObject("ratioRequestByStatusAPPROVED", ratioRequestByStatusAPPROVED);
		result.addObject("ratioRequestByStatusPENDING", ratioRequestByStatusPENDING);
		result.addObject("ratioRequestByStatusREJECTED", ratioRequestByStatusREJECTED);

		//-----------------------Brotherhoods per area
		final Double countHermandadesPorArea = this.areaService.statsBrotherhoodPerArea().get("COUNT");
		final Double maxHermandadesPorArea = this.areaService.statsBrotherhoodPerArea().get("MAX");
		final Double minHermandadesPorArea = this.areaService.statsBrotherhoodPerArea().get("MIN");
		final Double avgHermandadesPorArea = this.areaService.statsBrotherhoodPerArea().get("AVG");
		final Double stddevHermandadesPorArea = this.areaService.statsBrotherhoodPerArea().get("STD");

		result.addObject("countHermandadesPorArea", countHermandadesPorArea);
		result.addObject("maxHermandadesPorArea", maxHermandadesPorArea);
		result.addObject("minHermandadesPorArea", minHermandadesPorArea);
		result.addObject("avgHermandadesPorArea", avgHermandadesPorArea);
		result.addObject("stddevHermandadesPorArea", stddevHermandadesPorArea);

		//-----------------------Results in finder
		final Double minResultsInFinder = this.finderService.finderStats().get(0);
		final Double maxResultsInFinder = this.finderService.finderStats().get(1);
		final Double avgResultsInFinder = this.finderService.finderStats().get(2);
		final Double stdResultsInFinder = this.finderService.finderStats().get(3);
		final Double emptyVSNonEmptyFinder = this.finderService.finderStats().get(4);
		System.out.println("--------------->" + emptyVSNonEmptyFinder);

		result.addObject("minResultsInFinder", minResultsInFinder);
		result.addObject("maxResultsInFinder", maxResultsInFinder);
		result.addObject("avgResultsInFinder", avgResultsInFinder);
		result.addObject("stdResultsInFinder", stdResultsInFinder);
		result.addObject("emptyVSNonEmptyFinder", emptyVSNonEmptyFinder);

		//DASHBOARD ACME-PARADE
		//QueryC1
		final Double avgC1 = this.periodRecordService.avgQueryC1();
		final Double maxC1 = this.periodRecordService.maxQueryC1();
		final Double minC1 = this.periodRecordService.minQueryC1();
		final Double stddevC1 = this.periodRecordService.stddevQueryC1();

		if (avgC1 != null)
			result.addObject("avgC1", df.format(avgC1));
		else
			result.addObject("avgc1", 0.0);

		if (maxC1 != null)
			result.addObject("maxC1", df.format(maxC1));
		else
			result.addObject("maxC1", 0.0);

		if (minC1 != null)
			result.addObject("minC1", df.format(minC1));
		else
			result.addObject("minC1", 0.0);

		if (stddevC1 != null)
			result.addObject("stddevC1", df.format(stddevC1));
		else
			result.addObject("stddevc1", 0.0);

		//QUERY C2
		final String queryC2 = this.brotherhoodService.brotherhoodLargestHistory().get(0);
		result.addObject("queryC2", queryC2);

		//QUERY C3
		final List<String> queryC3 = this.brotherhoodService.brotherhoodLargestHistoryThanAVG();
		if (!queryC3.isEmpty())
			result.addObject("queryC3", queryC3);

		//QUERY B1
		final Double queryB1 = this.areaService.ratioAreasNoCoordinated();
		if (queryB1 != null)
			result.addObject("queryB1", df.format(queryB1));
		else
			result.addObject("queryB1", 0.0);

		//QUERY B2
		final Double avgB2 = this.chapterService.queryB2AVG();
		final Double maxB2 = this.chapterService.queryB2MAX();
		final Double minB2 = this.chapterService.queryB2MIN();
		final Double stddevB2 = this.chapterService.queryB2STDDEV();

		if (avgC1 != null)
			result.addObject("avgB2", df.format(avgB2));
		else
			result.addObject("avgB2", 0.0);

		if (maxC1 != null)
			result.addObject("maxB2", df.format(maxB2));
		else
			result.addObject("maxB2", 0.0);

		if (minC1 != null)
			result.addObject("minB2", df.format(minB2));
		else
			result.addObject("minB2", 0.0);

		if (stddevC1 != null)
			result.addObject("stddevB2", df.format(stddevB2));
		else
			result.addObject("stddevB2", 0.0);

		//QUERY B3
		final List<String> queryB3 = this.chapterService.chapters10MoreThanAverage();
		result.addObject("queryB3", queryB3);

		//QUERY B4
		final Double queryB4 = this.paradeService.ratioParadesDraftVsParadesFinal();
		if (queryB4 != null)
			result.addObject("queryB4", df.format(queryB4));
		else
			result.addObject("queryB4", 0.0);

		//QUERY B5
		final Double ratioParadeByStatusACCEPTED = this.paradeService.ratioParadeFinalByStatus().get(0);
		//final Double ratioParadeByStatusSUBMITTED = this.paradeService.ratioParadeFinalByStatus().get(2);
		final Double ratioParadeByStatusREJECTED = this.paradeService.ratioParadeFinalByStatus().get(1);
		if (ratioParadeByStatusACCEPTED != null)
			result.addObject("ratioParadeByStatusACCEPTED", df.format(ratioParadeByStatusACCEPTED));
		else
			result.addObject("ratioParadeByStatusACCEPTED", 0.0);
		/*
		 * if (ratioParadeByStatusSUBMITTED != null)
		 * result.addObject("ratioParadeByStatusSUBMITTED", df.format(ratioParadeByStatusSUBMITTED));
		 * else
		 * result.addObject("ratioParadeByStatusSUBMITTED", 0.0);
		 */
		if (ratioParadeByStatusREJECTED != null)
			result.addObject("ratioParadeByStatusREJECTED", df.format(ratioParadeByStatusREJECTED));
		else
			result.addObject("ratioParadeByStatusREJECTED", 0.0);

		//QUERYA1
		final Double queryA1 = this.sponsorshipService.ratioActiveSponsorship();
		if (queryA1 != null)
			result.addObject("queryA1", df.format(queryA1));
		else
			result.addObject("queryA1", 0.0);

		//QUERY A2
		final Double avgA2 = this.sponsorshipService.avgSponsorshipBySponsor();
		final Double maxA2 = this.sponsorshipService.maxSponsorshipBySponsor();
		final Double minA2 = this.sponsorshipService.minSponsorshipBySponsor();
		final Double stddevA2 = this.sponsorshipService.stddevSponsorshipBySponsor();

		if (avgA2 != null)
			result.addObject("avgA2", df.format(avgA2));
		else
			result.addObject("avgA2", 0.0);

		if (maxA2 != null)
			result.addObject("maxA2", df.format(maxA2));
		else
			result.addObject("maxA2", 0.0);

		if (minA2 != null)
			result.addObject("minA2", df.format(minA2));
		else
			result.addObject("minA2", 0.0);

		if (stddevA2 != null)
			result.addObject("stddevA2", df.format(stddevA2));
		else
			result.addObject("stddevA2", 0.0);

		//Query A3

		final List<String> queryA3 = this.sponsorshipService.top5Sponsors();
		result.addObject("queryA3", queryA3);

		return result;
	}
	@RequestMapping("/adviseTrue")
	public ModelAndView adviseTrue() {
		ModelAndView result;
		Warning war = this.warningService.giveWarning();
		war = this.warningService.setWarningTrue();
		result = new ModelAndView("welcome/index");
		System.out.println("Se ha alertado de una brecha?" + war.getIsWarning());

		return result;
	}

	@RequestMapping("/adviseFalse")
	public ModelAndView adviseFalse() {
		ModelAndView result;
		Warning war = this.warningService.giveWarning();
		war = this.warningService.setWarningFalse();
		result = new ModelAndView("welcome/index");
		System.out.println("Se ha alertado de una brecha?" + war.getIsWarning());

		return result;
	}
	//
	protected ModelAndView createEditModelAndView2(final Warning war) {
		ModelAndView result;

		result = this.createEditModelAndView2(war, null);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Warning warning, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("welcome/index");
		result.addObject("warning", warning);
		result.addObject("message", messageCode);

		return result;
	}

	@RequestMapping(value = "/deleteAdmin", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {

		ModelAndView result;
		final Actor s = this.actorService.findPrincipal();
		try {
			this.administratorService.deleteAdmin((Administrator) s);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/welcome/index.do");
			if (oops.getMessage().equals("UniqueAdmin"))
				result.addObject("message", "error.admin.unique");

		}
		return result;
	}

}
