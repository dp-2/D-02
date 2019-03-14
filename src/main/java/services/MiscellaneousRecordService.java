
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
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}

}
