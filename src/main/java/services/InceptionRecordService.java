
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import domain.History;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	//--------------------------Managed Repository----------------------------------------

	@Autowired
	private InceptionRecordRepository	InceptionRecordRepository;

	// ----------------------Supporting Service------------------------------------

	@Autowired
	private HistoryService				historyService;


	public InceptionRecordService() {
		super();
	}
	// Simple CRUD methods

	public InceptionRecord create() {
		InceptionRecord pr;
		pr = new InceptionRecord();
		return pr;
	}

	public Collection<InceptionRecord> findAll() {
		Collection<InceptionRecord> pr;
		pr = this.InceptionRecordRepository.findAll();
		return pr;
	}

	public InceptionRecord findOne(final int InceptionRecordId) {
		InceptionRecord res;
		res = this.InceptionRecordRepository.findOne(InceptionRecordId);
		return res;

	}

	public InceptionRecord save(final InceptionRecord p) {
		Assert.notNull(p);
		InceptionRecord res;
		res = this.InceptionRecordRepository.save(p);
		return res;
	}

	public void delete(final InceptionRecord p) {
		Assert.notNull(p);
		this.InceptionRecordRepository.delete(p);
	}

	public History getHistoryByBrotherhood(final int historyId) {
		final History res = this.historyService.findOne(historyId);

		return res;
	}

	public InceptionRecord getInceptionRecordByBrotherhood(final int historyId) {
		final InceptionRecord res = this.InceptionRecordRepository.findInceptionByHistoryId(historyId);

		return res;
	}

}
