
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PathRepository;
import domain.Parade;
import domain.Path;
import domain.Segment;

@Service
@Transactional
public class PathService {

	// Repository

	@Autowired
	private PathRepository	repository;

	// Services

	@Autowired
	private ServiceUtils	serviceUtils;


	// CRUD methods

	public Path findOne(final Integer id) {
		this.serviceUtils.checkId(id);
		return this.repository.findOne(id);
	}

	public Collection<Path> findAll(final Collection<Integer> ids) {
		this.serviceUtils.checkIds(ids);
		return this.repository.findAll(ids);
	}

	public Collection<Path> findAll() {
		return this.repository.findAll();
	}

	public Path create(final Parade p) {
		this.serviceUtils.checkActor(p.getBrotherhood());
		final Path res = new Path();
		res.setSegments(new ArrayList<Segment>());
		final Parade parade = (Parade) this.serviceUtils.checkObject(p);
		res.setParade(parade);
		return res;
	}
	public Path save(final Path p) {
		final Path path = (Path) this.serviceUtils.checkObjectSave(p);
		this.serviceUtils.checkActor(path.getParade().getBrotherhood());
		p.setParade(path.getParade());
		p.setSegments(path.getSegments());
		final Path res = this.repository.save(p);
		return res;
	}

	public void delete(final Path p) {
		final Path path = (Path) this.serviceUtils.checkObjectSave(p);
		this.serviceUtils.checkActor(path.getParade().getBrotherhood());
		this.repository.delete(path);
	}

	public void delete1(final Path p) {

		this.repository.delete(p);
	}

	public Path findByParadeId(final Integer paradeId) {
		Assert.notNull(paradeId);
		Assert.isTrue(paradeId > 0);
		return this.repository.findByParadeId(paradeId);
	}

}
