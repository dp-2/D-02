
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.History;
import domain.InceptionRecord;
import repositories.InceptionRecordRepository;

@Service
@Transactional
public class InceptionRecordService {

	//--------------------------Managed Repository----------------------------------------

	@Autowired
	private InceptionRecordRepository	InceptionRecordRepository;

	// ----------------------Supporting Service------------------------------------

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private ServiceUtils				serviceUtils;

	@Autowired
	private BrotherhoodService			brotherhoodService;


	public InceptionRecordService() {
		super();
	}
	// Simple CRUD methods

	public InceptionRecord createAndSave(final History history) {
		final History historyDB = (History) this.serviceUtils.checkObject(history);
		this.serviceUtils.checkActor(historyDB.getBrotherhood());
		Assert.notNull(history);
		final InceptionRecord inceptionRecord = new InceptionRecord();
		final String photo = "https://content.thriveglobal.com/wp-content/uploads/2017/10/change-pixabay.jpg";
		final List<String> photos = new ArrayList<>();
		photos.add(photo);
		inceptionRecord.setHistory(history);
		inceptionRecord.setTitle("Default Inception Record Title");
		inceptionRecord.setText("Default text");
		inceptionRecord.setPhotos(photos);

		final InceptionRecord res = this.InceptionRecordRepository.saveAndFlush(inceptionRecord);
		this.InceptionRecordRepository.flush();
		return res;
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

	public InceptionRecord save(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);
		final InceptionRecord inceptionRecordDB = (InceptionRecord) this.serviceUtils.checkObjectSave(inceptionRecord);

		//compruebo que el brotherhood que está intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(inceptionRecord.getHistory().getBrotherhood());
		this.serviceUtils.checkAuthority("BROTHERHOOD");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(inceptionRecord);

		final InceptionRecord res = this.InceptionRecordRepository.save(inceptionRecord);
		return res;

	}

	public void delete(final InceptionRecord p) {
		Assert.notNull(p);
		final InceptionRecord inceptionRecord = (InceptionRecord) this.serviceUtils.checkObject(p);
		this.serviceUtils.checkActor(inceptionRecord.getHistory().getBrotherhood());
		this.InceptionRecordRepository.delete(p);
	}

	public void delete1(final InceptionRecord p) {
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

	public void flush() {
		this.InceptionRecordRepository.flush();
	}

	public Double avgQueryC1() {
		return this.InceptionRecordRepository.avgQueryC1();
	}

	public Double maxQueryC1() {
		return this.InceptionRecordRepository.maxQueryC1();
	}

	public Double minQueryC1() {
		return this.InceptionRecordRepository.minQueryC1();
	}

	public Double stddevQueryC1() {
		return this.InceptionRecordRepository.stddevQueryC1();
	}

}
