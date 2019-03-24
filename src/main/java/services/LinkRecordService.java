
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;
import repositories.LinkRecordRepository;
import security.LoginService;

@Service
@Transactional
public class LinkRecordService {

	//Managed repositories--------------------------

	@Autowired
	private LinkRecordRepository	linkRecordRepository;

	//-------------- Supporting Services-----------------------

	@Autowired
	private ServiceUtils			serviceUtils;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


	// --------------------------Constructor-----------------------
	public LinkRecordService() {
		super();
	}

	// --------------------CRUD methods----------------------------
	public LinkRecord create() {
		final LinkRecord record = new LinkRecord();
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(LoginService.getPrincipal().getId());

		final History history = this.historyService.findOneByBrotherhoodId(brotherhood.getId());

		record.setHistory(history);
		return record;
	}
	public LinkRecord save(final LinkRecord record) {
		Assert.notNull(record);
		final LinkRecord linkRecordDB = (LinkRecord) this.serviceUtils.checkObjectSave(record);
		//compruebo que el brotherhood que está intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(record.getHistory().getBrotherhood());
		this.serviceUtils.checkAuthority("BROTHERHOOD");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(record);

		final LinkRecord res = this.linkRecordRepository.save(record);
		return res;

	}

	public LinkRecord findOne(final int recordId) {
		return this.linkRecordRepository.findOne(recordId);
	}

	public Collection<LinkRecord> findAll() {
		return this.linkRecordRepository.findAll();
	}

	public Collection<LinkRecord> findAllByHistoryId(final int historyId) {
		return this.linkRecordRepository.findLinkRecordsByHistoryId(historyId);
	}

	public void delete(final LinkRecord record) {
		this.linkRecordRepository.delete(record);
	}

	public Double avgQueryC1() {
		return this.linkRecordRepository.avgQueryC1();
	}

	public Double maxQueryC1() {
		return this.linkRecordRepository.maxQueryC1();
	}

	public Double minQueryC1() {
		return this.linkRecordRepository.minQueryC1();
	}

	public Double stddevQueryC1() {
		return this.linkRecordRepository.stddevQueryC1();
	}

}
