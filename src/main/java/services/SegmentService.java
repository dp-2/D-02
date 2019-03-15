
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SegmentRepository;
import domain.Path;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	// Repository

	@Autowired
	private SegmentRepository	repository;

	// Services

	@Autowired
	private ServiceUtils		serviceUtils;

	// Validator

	@Autowired(required = false)
	private Validator			validator;


	// CRUD methods

	public Segment findOne(final Integer id) {
		this.serviceUtils.checkId(id);
		return this.repository.findOne(id);
	}

	public Collection<Segment> findAll(final Collection<Integer> ids) {
		this.serviceUtils.checkIds(ids);
		return this.repository.findAll(ids);
	}

	public Collection<Segment> findAll() {
		return this.findAll();
	}

	public Segment create(final Path path) {
		final Segment res = new Segment();
		res.setPath(path);
		return res;
	}

	public Segment save(final Segment s) {
		final Segment segment = (Segment) this.serviceUtils.checkObjectSave(s);
		this.serviceUtils.checkActor(segment.getPath().getParade().getBrotherhood());
		// Si hay segmento previo, se tiene tiene que ajustar el origen
		if (s.getPreviousSegment() != null) {
			s.setLatitudeOrigin(segment.getLatitudeOrigin());
			s.setLongitudeOrigin(segment.getLongitudeOrigin());
		}
		// El tiempo de origen es anterior al de destino
		Assert.isTrue(s.getTimeOrigin().before(s.getTimeDestination()));
		s.setPath(segment.getPath());
		final Segment res = this.save(s);
		return res;
	}

	public void delete(final Segment p) {
		final Segment segment = (Segment) this.serviceUtils.checkObjectSave(p);
		this.serviceUtils.checkActor(segment.getPath().getParade().getBrotherhood());
		// Si era un segmento previo se tiene que ajustar el origen del segemento posterior
		final Segment nextSegment = this.findNextSegment(segment);
		if (nextSegment != null)
			nextSegment.setPreviousSegment(segment.getPreviousSegment());
		this.delete(segment);
		if (nextSegment != null)
			this.save(nextSegment);
	}

	public Segment findNextSegment(final Segment s) {
		final Segment segment = (Segment) this.serviceUtils.checkObject(s);
		return this.repository.findNextSegment(segment.getId(), segment.getPath().getId());
	}

	public Segment deconstruct(final Segment segment, final BindingResult binding) {
		Segment res = null;
		if (segment.getId() == 0)
			res = this.create(segment.getPath());
		else
			res = this.findOne(segment.getId());
		Assert.notNull(res);
		segment.setPath(res.getPath());
		segment.setVersion(res.getVersion());
		res = segment;
		return res;
	}
}
