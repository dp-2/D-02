
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
		return this.repository.findAll();
	}

	public Segment create(final Path path) {
		this.serviceUtils.checkActor(path.getParade().getBrotherhood());
		final Segment res = new Segment();
		res.setPath(path);
		return res;
	}

	public Segment save(final Segment s, final Boolean loop) {
		System.out.println("save");
		final Segment segment = (Segment) this.serviceUtils.checkObjectSave(s);
		System.out.println("checkActor");
		this.serviceUtils.checkActor(segment.getPath().getParade().getBrotherhood());
		// El tiempo de origen es anterior al de destino
		System.out.println("time");
		Assert.isTrue(s.getTimeOrigin().before(s.getTimeDestination()));
		// El tiempo entre segmentos es consistente
		// El espacio de tiempo que ocupa el segmento no puede estar ocupado
		System.out.println("occupied");
		Assert.isTrue(this.isTimeRangeOccupied(s).isEmpty());
		s.setPath(segment.getPath());
		System.out.println("repoSave");
		final Segment res = this.repository.save(s);
		System.out.println(loop);
		if (loop) {
			this.flush();
			System.out.println("set");
			final SortedSet<Segment> segmentsSet = new TreeSet<Segment>(new Comparator<Segment>() {

				@Override
				public int compare(final Segment o1, final Segment o2) {
					if (o1.getTimeOrigin().after(o2.getTimeOrigin()))
						return 1;
					else if (o1.getTimeOrigin().before(o2.getTimeOrigin()))
						return -1;
					else
						return 0;
				}
			});
			System.out.println("list");
			final List<Segment> segmentsList = new ArrayList<Segment>(segmentsSet);
			// Se ajustan las localizaciones para que sean contiguas
			System.out.println("for");
			for (Integer i = 0; i < segmentsList.size(); i++) {
				System.out.println("forSave");
				try {
					final Segment seg = segmentsList.get(i);
					final Segment nextSeg = segmentsList.get(i + 1);
					nextSeg.setLatitudeOrigin(seg.getLatitudeDestination());
					nextSeg.setLongitudeOrigin(seg.getLongitudeDestination());
					this.save(nextSeg, false);
				} catch (final IndexOutOfBoundsException e) {
				}
			}
		}
		return res;
	}

	public void delete(final Segment p) {
		final Segment segment = (Segment) this.serviceUtils.checkObjectSave(p);
		this.serviceUtils.checkActor(segment.getPath().getParade().getBrotherhood());
		this.repository.delete(segment);
		this.flush();
		final SortedSet<Segment> segmentsSet = new TreeSet<Segment>(new Comparator<Segment>() {

			@Override
			public int compare(final Segment o1, final Segment o2) {
				if (o1.getTimeOrigin().after(o2.getTimeOrigin()))
					return 1;
				else if (o1.getTimeOrigin().before(o2.getTimeOrigin()))
					return -1;
				else
					return 0;
			}
		});
		final List<Segment> segmentsList = new ArrayList<Segment>(segmentsSet);
		// Se ajustan las localizaciones para que sean contiguas
		for (Integer i = 0; i < segmentsList.size(); i++)
			try {
				final Segment seg = segmentsList.get(i);
				final Segment nextSeg = segmentsList.get(i + 1);
				nextSeg.setLatitudeOrigin(seg.getLatitudeDestination());
				nextSeg.setLongitudeOrigin(seg.getLongitudeDestination());
				this.save(nextSeg, false);
			} catch (final IndexOutOfBoundsException e) {
			}
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

	public Collection<Segment> isTimeRangeOccupied(final Segment s) {
		Assert.notNull(s);
		return this.repository.isTimeRangeOccupied(s.getTimeOrigin(), s.getTimeDestination(), s.getPath().getId());
	}

	public void flush() {
		this.repository.flush();
	}

}
