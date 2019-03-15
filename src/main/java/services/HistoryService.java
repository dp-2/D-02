
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.HistoryRepository;
import security.LoginService;
import security.UserAccount;
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


	//Services-----------------------------------------------------------------------------

	public History create() {
		final History history = new History();
		final UserAccount userAccount = LoginService.getPrincipal();
		final Brotherhood brotherhood = this.brotherhoodService.findBrotherhoodByUserAcountId(userAccount.getId());

		history.setBrotherhood(brotherhood);
		history.setTitle("Default title");
		this.inceptionService.create();

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

}
