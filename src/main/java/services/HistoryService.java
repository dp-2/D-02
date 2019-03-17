
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import domain.Brotherhood;
import domain.History;

@Service
@Transactional
public class HistoryService {

	// Managed repository and services----------------------------------------------------------------
	@Autowired
	private HistoryRepository		historyRepository;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private InceptionRecordService	inceptionService;

	@Autowired
	private ServiceUtils			serviceUtils;


	//Services-----------------------------------------------------------------------------

	public History create(final Brotherhood brotherhood) {
		Assert.notNull(brotherhood);
		final History history = new History();

		history.setBrotherhood(brotherhood);
		history.setTitle("Default title");
		this.inceptionService.create(history);

		return history;
	}

	public History findOne(final int HistoryId) {
		return this.historyRepository.findOne(HistoryId);
	}

	public History findOneByBrotherhoodId(final int brotherhoodId) {
		return this.historyRepository.findHistoryByBrotherhoodId(brotherhoodId);
	}

	public List<History> findAll() {
		return this.historyRepository.findAll();
	}

	public History save(final History history) {
		Assert.notNull(history);
		//compruebo que el brotherhood que está intentando editar sea el el dueño del historial al que pertenece dicho Record
		this.serviceUtils.checkActor(history.getBrotherhood());
		this.serviceUtils.checkAuthority("BROTHERHOOD");

		//comprobamos que el id del objeto no sea nulo o negativo por seguridad
		this.serviceUtils.checkIdSave(history);

		final History res = this.historyRepository.save(history);
		return res;

	}

}
