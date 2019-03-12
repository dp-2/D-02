
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChapterRepository;
import repositories.ProclaimRepository;
import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.Parade;
import domain.Proclaim;

@Service
@Transactional
public class ProclaimService {

	// Managed repository ----------------------------------------------------------------
	@Autowired
	private ProclaimRepository	proclaimRepository;

	@Autowired
	private ChapterRepository	chapterRepository;


	//Constructor----------------------------------------------------------------------------

	public ProclaimService() {
		super();
	}

	// Simple CRUD methods -------------------------------------------------------------------
	public Proclaim create(final int chapterId) {
		final Proclaim p = new Proclaim();
		final Chapter chapter = this.chapterRepository.findOne(chapterId);
		p.setChapter(chapter);
		return p;
	}
	public List<Proclaim> findAll() {
		final List<Proclaim> proclaims;

		proclaims = this.proclaimRepository.findAll();
		Assert.notNull(proclaims);

		return proclaims;
	}
	public Proclaim findOne(final int proclaimId) {
		final Proclaim proclaim = this.proclaimRepository.findOne(proclaimId);
		Assert.notNull(proclaimId);

		return proclaim;
	}

	public Proclaim save(final Proclaim proclaim) {
		Assert.notNull(proclaim);
		Proclaim result;
		result = this.proclaimRepository.save(proclaim);

		return result;
	}

	public void delete(final Proclaim proclaim) {

		Assert.notNull(proclaim);
		this.proclaimRepository.delete(proclaim);
	}

	//	Other methods

	public Collection<Area> findAreaByChapter(final int chapterId) {
		return this.proclaimRepository.findAreaByChapter(chapterId);
	}

	public Collection<Brotherhood> findBrotherhoodByChapter(final int chapterId) {
		return this.proclaimRepository.findBrotherhoodByChapter(chapterId);
	}

	public Collection<Parade> findParadeByChapter(final int chapterId) {
		return this.proclaimRepository.findParadeByChapter(chapterId);
	}

	public Collection<Proclaim> findProclaimByChapter(final int chapterId) {
		return this.proclaimRepository.findProclaimByChapter(chapterId);
	}

}
