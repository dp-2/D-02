
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.DFloatRepository;
import security.LoginService;
import domain.Actor;
import domain.Brotherhood;
import domain.DFloat;
import domain.Parade;

@Service
@Transactional
public class DFloatService {

	//Managed Repository

	@Autowired
	private DFloatRepository	dfloatRepository;
	//Supporting Service

	@Autowired
	ActorService				actorService;

	@Autowired
	ServiceUtils				serviceUtils;


	// Simple CRUD methods
	public DFloat create() {
		DFloat res;
		Brotherhood brotherhood;
		final Collection<Parade> parades = new ArrayList<>();
		brotherhood = (Brotherhood) this.actorService.findByUserAccount(LoginService.getPrincipal());
		res = new DFloat();
		res.setBrotherhood(brotherhood);
		res.setParades(parades);

		return res;
	}

	public Collection<DFloat> findAll() {
		return this.dfloatRepository.findAll();
	}

	public DFloat findOne(final int dfloatId) {
		return this.dfloatRepository.findOne(dfloatId);
	}

	public DFloat save(final DFloat dfloat) {
		DFloat res = null;
		Assert.notNull(dfloat);
		this.serviceUtils.checkObjectSave(dfloat);

		res = this.dfloatRepository.save(dfloat);

		return res;
	}

	public void delete(final DFloat dfloat) {
		Assert.notNull(dfloat);
		this.dfloatRepository.delete(dfloat);
	}

	//----------------- Other business methods----------------------------------

	public Collection<DFloat> findAllDFloatsByBrotherhood(final Brotherhood brotherhood) {
		return this.dfloatRepository.SearchDFloatsByBrotherhood(brotherhood.getId());
	}

	public Collection<DFloat> SearchDFloatsByBrotherhood(final int brotherhoodId) {
		return this.dfloatRepository.SearchDFloatsByBrotherhood(brotherhoodId);
	}

	public Collection<DFloat> findAllDFloatsWithoutBrotherhood() {
		return this.dfloatRepository.SearchDFloatsWithoutBrotherhood();
	}

	//	public List<DFloat> findDFloatsByParadeId(final int paradeId) {
	//		return this.dfloatRepository.findDFloatsByParadeId(paradeId);
	//	

	public Collection<DFloat> searchFloatNotInParadeByIdByActorId(final Parade parade, final Actor actor) {
		this.serviceUtils.checkObject(parade);
		this.serviceUtils.checkObject(actor);
		return this.dfloatRepository.searchFloatNotInParadeByIdByActorId(parade.getId(), actor.getId());
	}

	public Collection<DFloat> searchFloatInParadeByIdByActorId(final Parade parade, final Actor actor) {
		this.serviceUtils.checkObject(parade);
		this.serviceUtils.checkObject(actor);
		return this.dfloatRepository.searchFloatInParadeByIdByActorId(parade.getId(), actor.getId());
	}
}
