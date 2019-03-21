
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Path;
import domain.Segment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	//Service-------------------------------------------
	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private PathService		pathService;


	// Tests

	@Test
	public void testFindOne() {
		final Integer id = new ArrayList<Segment>(this.segmentService.findAll()).get(0).getId();
		this.findOneTest(null, id, null);
	}

	@Test
	public void testFindOneIdNegative() {
		final Integer id = -1;
		this.findOneTest(null, id, IllegalArgumentException.class);
	}

	@Test
	public void testFindAllIds() {
		final Collection<Integer> ids = new ArrayList<Integer>();
		for (final Segment s : this.segmentService.findAll())
			ids.add(s.getId());
		this.findAllTest(null, ids, null);
	}

	@Test
	public void testFindAllIdsWrongIds() {
		final Collection<Integer> ids = Arrays.asList(-1, 0);
		this.findAllTest(null, ids, IllegalArgumentException.class);
	}

	@Test
	public void testFindAll() {
		this.findAllTest(null, null);
	}

	@Test
	public void testCreate() {
		final Path path = this.pathService.findOne(super.getEntityId("path1"));
		this.createTest("brotherhood1", path, null);
	}

	@Test
	public void testCreateWrongUser() {
		final Path path = this.pathService.findOne(super.getEntityId("path1"));
		this.createTest("brotherhood2", path, IllegalArgumentException.class);
	}

	@Test
	public void testSave() {
		final Double[] locations = new Double[] {
			90.0, 180.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2019, 10, 10, 10, 10, 10), new Date(2019, 10, 11, 11, 11, 11)
		};
		this.saveTest("brotherhood1", "path1", locations, dates, null);
	}

	@Test
	public void testSaveWrongUser() {
		final Double[] locations = new Double[] {
			90.0, 180.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2019, 10, 10, 10, 10, 10), new Date(2019, 10, 11, 11, 11, 11)
		};
		this.saveTest("brotherhood2", "path1", locations, dates, IllegalArgumentException.class);
	}

	@Test
	public void testSaveWrongTime() {
		final Double[] locations = new Double[] {
			90.0, 180.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2020, 6, 29, 17, 00, 00), new Date(2020, 6, 29, 17, 30, 00)
		};
		this.saveTest("brotherhood2", "path1", locations, dates, IllegalArgumentException.class);
	}

	@Test
	public void testSaveWrongLocations() {
		final Double[] locations = new Double[] {
			100.0, 190.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2019, 10, 10, 10, 10, 10), new Date(2019, 10, 11, 11, 11, 11)
		};
		this.saveTest("brotherhood2", "path1", locations, dates, IllegalArgumentException.class);
		System.out.println("testSave");
	}

	@Test
	public void testUpdate() {
		final Double[] locations = new Double[] {
			90.0, 180.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2019, 10, 10, 10, 10, 10), new Date(2019, 10, 11, 11, 11, 11)
		};
		this.updateTest("brotherhood1", "segment1Path1", locations, dates, null);
	}

	@Test
	public void testUpdateWrongUser() {
		final Double[] locations = new Double[] {
			90.0, 180.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2019, 10, 10, 10, 10, 10), new Date(2019, 10, 11, 11, 11, 11)
		};
		this.updateTest("brotherhood2", "segment1Path1", locations, dates, IllegalArgumentException.class);
	}

	@Test
	public void testUpdateWrongTime() {
		final Double[] locations = new Double[] {
			90.0, 180.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2020, 6, 29, 17, 00, 00), new Date(2020, 6, 29, 17, 30, 00)
		};
		this.updateTest("brotherhood2", "segment1Path1", locations, dates, IllegalArgumentException.class);
	}

	@Test
	public void testUpdateWrongLocations() {
		final Double[] locations = new Double[] {
			100.0, 190.0, 0.0, 0.0
		};
		final Date[] dates = new Date[] {
			new Date(2019, 10, 10, 10, 10, 10), new Date(2019, 10, 11, 11, 11, 11)
		};
		this.updateTest("brotherhood2", "segment1Path1", locations, dates, IllegalArgumentException.class);
	}

	@Test
	public void testDelete() {
		this.deleteTest("brotherhood1", "segment1Path1", null);
	}

	@Test
	public void testDeleteWrongUser() {
		this.deleteTest("brotherhood2", "segment1Path1", IllegalArgumentException.class);
	}

	// Methods

	private void findOneTest(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.segmentService.findOne(id);
		} catch (final Throwable t) {
			super.authenticate(username);
			caught = t.getClass();
			super.authenticate(null);
		}
		super.checkExceptions(expected, caught);
	}

	private void findAllTest(final String username, final Collection<Integer> ids, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.segmentService.findAll(ids);
		} catch (final Throwable t) {
			super.authenticate(username);
			caught = t.getClass();
			super.authenticate(null);
		}
		super.checkExceptions(expected, caught);
	}

	private void findAllTest(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.segmentService.findAll();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void createTest(final String username, final Path path, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.segmentService.create(path);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void saveTest(final String username, final String pathBeanName, final Double[] locations, final Date[] dates, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Path path = this.pathService.findOne(super.getEntityId(pathBeanName));
			Segment segment = this.segmentService.create(path);
			segment = this.segmentAssignParameters(segment, locations, dates);
			this.segmentService.save(segment, true);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void updateTest(final String username, final String segmentBeanName, final Double[] locations, final Date[] dates, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Segment segment = this.segmentService.findOne(super.getEntityId(segmentBeanName));
			segment = this.segmentAssignParameters(segment, locations, dates);
			this.segmentService.save(segment, true);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	public void deleteTest(final String username, final String segmentBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Segment segment = this.segmentService.findOne(super.getEntityId(segmentBeanName));
			this.segmentService.delete(segment);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public Segment segmentAssignParameters(final Segment s, final Double[] locations, final Date[] dates) {
		s.setLatitudeDestination(locations[2]);
		s.setLatitudeOrigin(locations[0]);
		s.setLongitudeDestination(locations[3]);
		s.setLongitudeOrigin(locations[1]);
		s.setTimeOrigin(dates[0]);
		s.setTimeDestination(dates[1]);
		return s;
	}

}
