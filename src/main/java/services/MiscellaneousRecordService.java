
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import security.LoginService;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	//Managed repositories--------------------------

	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	//-------------- Supporting Services-----------------------

	@Autowired
	private ServiceUtils					serviceUtils;

	@Autowired
	private BrotherhoodService				brotherhoodService;

	@Autowired
	private HistoryService					historyService;


	// --------------------------Constructor-----------------------
	public MiscellaneousRecordService() {
		super();
	}

	// --------------------CRUD methods----------------------------
	public MiscellaneousRecord create() {
		final MiscellaneousRecord miscellaneousRecord = new MiscellaneousRecord();
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId());

		final History history = this.historyService.findOneByBrotherhoodId(brotherhood.getId());

		miscellaneousRecord.setHistory(history);
		return miscellaneousRecord;
	}
	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);
		final MiscellaneousRecord miscellaneousRecordDB = (MiscellaneousRecord) this.serviceUtils.checkObjectSave(miscellaneousRecord);

		//compruebo que el brotherhood que está intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(miscellaneousRecord.getHistory().getBrotherhood());
		this.serviceUtils.checkAuthority("BROTHERHOOD");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(miscellaneousRecord);

		final MiscellaneousRecord res = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		return res;

	}

	public MiscellaneousRecord findOne(final int miscellaneousRecordId) {
		return this.miscellaneousRecordRepository.findOne(miscellaneousRecordId);
	}

	public Collection<MiscellaneousRecord> findAll() {
		return this.miscellaneousRecordRepository.findAll();
	}

	public Collection<MiscellaneousRecord> findAllByHistoryId(final int historyId) {
		return this.miscellaneousRecordRepository.findMiscellaneousRecordsByHistoryId(historyId);
	}

	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		this.serviceUtils.checkActor(miscellaneousRecord.getHistory().getBrotherhood());
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}

	public Double avgQueryC1() {
		return this.miscellaneousRecordRepository.avgQueryC1();
	}

	public Double maxQueryC1() {
		return this.miscellaneousRecordRepository.maxQueryC1();
	}

	public Double minQueryC1() {
		return this.miscellaneousRecordRepository.minQueryC1();
	}

	public Double stddevQueryC1() {
		return this.miscellaneousRecordRepository.stddevQueryC1();
	}

	public Boolean checkEquals(final MiscellaneousRecord record) {
		Boolean res = false;
		final Collection<MiscellaneousRecord> todos = this.miscellaneousRecordRepository.findAll();
		for (final MiscellaneousRecord r : todos)
			if (r.getHistory().equals(record.getHistory()) && r.getTitle().equals(record.getTitle()) && r.getText().equals(record.getText())) {
				res = true;
				break;
			}
		return res;
	}

}
