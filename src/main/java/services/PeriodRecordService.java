
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	//Managed repositories--------------------------

	@Autowired
	private PeriodRecordRepository	periodRecordRepository;

	//-------------- Supporting Services-----------------------

	@Autowired
	private ServiceUtils			serviceUtils;


	// --------------------------Constructor-----------------------
	public PeriodRecordService() {
		super();
	}

	// --------------------CRUD methods----------------------------
	public PeriodRecord create() {
		final PeriodRecord periodRecord = new PeriodRecord();
		return periodRecord;
	}

	public PeriodRecord save(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		//compruebo que el brotherhood que está intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(periodRecord.getHistory().getBrotherhood());
		this.serviceUtils.checkAuthority("BROTHERHOOD");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(periodRecord);

		final PeriodRecord res = this.periodRecordRepository.save(periodRecord);
		return res;

	}

	public PeriodRecord findOne(final int periodRecordId) {
		return this.periodRecordRepository.findOne(periodRecordId);
	}

	public Collection<PeriodRecord> findAll() {
		return this.periodRecordRepository.findAll();
	}

	public Collection<PeriodRecord> findAllByHistoryId(final int historyId) {
		return this.periodRecordRepository.findPeriodRecordsByHistoryId(historyId);
	}
}
