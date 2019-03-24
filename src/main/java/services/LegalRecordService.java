
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;
import repositories.LegalRecordRepository;
import security.LoginService;

@Service
@Transactional
public class LegalRecordService {

	//Managed repositories--------------------------

	@Autowired
	private LegalRecordRepository	legalRecordRepository;

	//-------------- Supporting Services-----------------------

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;

	@Autowired
	private ActorService			actorService;


	// --------------------------Constructor-----------------------
	public LegalRecordService() {
		super();
	}

	// --------------------CRUD methods----------------------------
	public LegalRecord create() {
		final LegalRecord legalRecord = new LegalRecord();
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId());

		final History history = this.historyService.findOneByBrotherhoodId(brotherhood.getId());

		legalRecord.setHistory(history);
		return legalRecord;
	}
	public LegalRecord save(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		final LegalRecord legalRecordDB = (LegalRecord) this.serviceUtils.checkObjectSave(legalRecord);
		//compruebo que el brotherhood que está intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(legalRecord.getHistory().getBrotherhood());
		this.serviceUtils.checkAuthority("BROTHERHOOD");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(legalRecord);

		final LegalRecord res = this.legalRecordRepository.save(legalRecord);
		return res;

	}

	public LegalRecord findOne(final int legalRecordId) {
		return this.legalRecordRepository.findOne(legalRecordId);
	}

	public Collection<LegalRecord> findAll() {
		return this.legalRecordRepository.findAll();
	}

	public Collection<LegalRecord> findAllByHistoryId(final int historyId) {
		return this.legalRecordRepository.findLegalRecordsByHistoryId(historyId);
	}

	public void delete(final LegalRecord legalRecord) {
		// Se comprueba que no se pasa objeto nulo y que existe enla base de datos
		//System.out.println("checkObj");
		final LegalRecord legalRecordDB = (LegalRecord) this.serviceUtils.checkObject(legalRecord);
		// Se comprueba que el actor asociado al objeto en base de datos es el actor logueado
		//System.out.println("checkActor");
		this.serviceUtils.checkActor(legalRecord.getHistory().getBrotherhood());
		//System.out.println("delete");
		this.legalRecordRepository.delete(legalRecord);
	}

	public void delete1(final LegalRecord legalRecord) {

		this.legalRecordRepository.delete(legalRecord);
	}

	public void flush() {
		this.legalRecordRepository.flush();
	}

	public Double avgQueryC1() {
		return this.legalRecordRepository.avgQueryC1();
	}

	public Double maxQueryC1() {
		return this.legalRecordRepository.maxQueryC1();
	}

	public Double minQueryC1() {
		return this.legalRecordRepository.minQueryC1();
	}

	public Double stddevQueryC1() {
		return this.legalRecordRepository.stddevQueryC1();
	}

}
