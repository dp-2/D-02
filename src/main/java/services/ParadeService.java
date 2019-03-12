
package services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParadeRepository;
import security.LoginService;
import domain.Actor;
import domain.Brotherhood;
import domain.DFloat;
import domain.Parade;
import forms.ParadeForm;

@Service
@Transactional
public class ParadeService {

	//Repository-----------------------------------------------------------------

	@Autowired
	private ParadeRepository		paradeRepository;

	//Service--------------------------------------------------------------------

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ActorService			actorService;
	@Autowired
	private FinderService			finderService;

	@Autowired
	private DFloatService			dFloatService;
	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired(required = false)
	private Validator				validator;

	@Autowired
	private MessageService			messageService;


	//Methods--------------------------------------------------------------------

	public Parade create() {
		final Parade parade = new Parade();
		final String ticker = "ticker";
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId());

		parade.setBrotherhood(brotherhood);
		parade.setFfinal(false);
		parade.setTicker(ticker);

		return parade;

	}

	public List<Parade> findAll() {
		return this.paradeRepository.findAll();
	}

	public Parade findOne(final Integer id) {
		return this.paradeRepository.findOne(id);
	}

	public Parade save(final Parade parade) {
		Assert.notNull(parade);
		this.checkMoment(parade);

		if (parade.getId() != 0) {
			this.checkPrincipal(parade);
			this.checkNoFinalMode(parade);
		} else
			parade.setTicker(this.configurationService.isUniqueTicker(parade));

		final Parade saved = this.paradeRepository.save(parade);
		return saved;
	}

	public void delete(final Parade parade) {
		this.checkPrincipal(parade);
		this.checkNoFinalMode(parade);
		//this.deleteDFloatsOfParade(parade);
		this.paradeRepository.delete(parade);
	}

	//Other Methods---------------------------------------------------------------

	private boolean checkPrincipal(final Parade parade) {
		final Brotherhood brotherhood = parade.getBrotherhood();
		final Brotherhood principal = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId());

		Assert.isTrue(brotherhood.getId() == principal.getId(), "noOwner");

		return true;
	}

	private boolean checkNoFinalMode(final Parade parade) {
		final Parade paradeBD = this.findOne(parade.getId());
		Assert.isTrue(paradeBD.isFfinal() == false, "noFinal");

		return true;
	}

	private boolean checkMoment(final Parade parade) {
		final Date now = new Date();
		Assert.isTrue(now.before(parade.getMomentOrganised()), "noFuture");
		return true;
	}

	public Parade reconstructToProceesion(final ParadeForm paradeForm, final BindingResult bindingResult) {
		Parade parade;
		final Brotherhood brotherhood = this.brotherhoodService.findOne(paradeForm.getBrotherhoodId());

		if (paradeForm.getId() == 0)
			parade = this.create();
		else {
			parade = this.findOne(paradeForm.getId());
			parade.setTicker(paradeForm.getTicker());
			parade.setTitle(paradeForm.getTitle());
			parade.setDescription(paradeForm.getDescription());
			parade.setFfinal(paradeForm.isFfinal());
			parade.setBrotherhood(brotherhood);

			//			this.validator.validate(parade, bindingResult);
		}

		return parade;

	}

	public List<Parade> findParadesFinal() {
		return this.paradeRepository.findParadesFinal();
	}

	public List<Parade> findParadesAccepted() {
		return this.paradeRepository.findParadesAccepted();
	}

	public List<Parade> findParadesFinalByBrotherhoodId(final int brotherhoodId) {
		return this.paradeRepository.findParadesFinalByBrotherhoodId(brotherhoodId);
	}

	public List<Parade> findParadesByBrotherhoodId(final int brotherhoodId) {
		return this.paradeRepository.findParadesByBrotherhoodId(brotherhoodId);
	}

	public List<Parade> findParadeOfMember(final int memberId) {
		return this.paradeRepository.findParadeOfMember(memberId);
	}

	//	private void deleteDFloatsOfParade(final Parade parade) {
	//		final List<DFloat> dFloats = this.dFloatService.findDFloatsByParadeId(parade.getId());
	//
	//		if (!dFloats.isEmpty())
	//			for (final DFloat dFloat : dFloats)
	//				this.dFloatService.delete(dFloat);
	//	}

	public List<Actor> getActorsByParade(final int paradeId) {
		final Collection<Actor> res = new ArrayList<Actor>();
		final Collection<Actor> actors = this.actorService.findAll();
		final Parade proces = this.findOne(paradeId);
		for (final Actor a : actors) {
			final List<Parade> pros = this.findParadeOfMember(a.getId());
			for (final Parade pr : pros)
				if (pros.contains(proces))
					res.add(a);
		}
		return (List<Actor>) res;
	}

	public List<Parade> findParadesIn30Days() throws ParseException {
		//creo el formato de fecha para convertirlo luego
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//Creo la fecha sumándole directamente un mes y pasándola por el formato y conviritiendola a string para que la parsee a modo fecha
		final Date date = dateFormat.parse(LocalDate.now().plusMonths(1).toString());
		return this.paradeRepository.findParadesIn30Days(date);
	}

	public List<Parade> findParadesForRemove(final DFloat dfloat) {
		this.serviceUtils.checkObject(dfloat);
		return this.paradeRepository.findParadesForRemove(dfloat.getId());
	}

	public List<Parade> findParadesForAdd(final DFloat dfloat) {
		this.serviceUtils.checkObject(dfloat);
		return this.paradeRepository.findParadesForAdd(dfloat.getId());
	}

}
