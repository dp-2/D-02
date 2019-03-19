
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Parade;
import domain.Path;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class PathServiceTest extends AbstractTest {

	//Service-------------------------------------------
	@Autowired
	private ParadeService	paradeService;

	@Autowired
	private PathService		pathService;


	// Tests

	@Test
	public void testFindOne() {
		final Integer id = new ArrayList<Path>(this.pathService.findAll()).get(0).getId();
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
		for (final Path s : this.pathService.findAll())
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
		final Parade parade = this.paradeService.findOne(super.getEntityId("parade1"));
		this.createTest("brotherhood1", parade, null);
	}

	@Test
	public void testCreateWrongUser() {
		final Parade parade = this.paradeService.findOne(super.getEntityId("parade1"));
		this.createTest("brotherhood2", parade, IllegalArgumentException.class);
	}

	@Test
	public void testSave() {
		this.saveTest("brotherhood1", "parade1", null);
	}

	@Test
	public void testSaveWrongUser() {
		this.saveTest("brotherhood2", "parade1", IllegalArgumentException.class);
	}

	@Test
	public void testSaveWrongTime() {
		this.saveTest("brotherhood2", "parade1", IllegalArgumentException.class);
	}

	@Test
	public void testSaveWrongLocations() {
		this.saveTest("brotherhood2", "parade1", IllegalArgumentException.class);
		System.out.println("testSave");
	}

	@Test
	public void testUpdate() {
		this.updateTest("brotherhood1", "path1", null);
	}

	@Test
	public void testUpdateWrongUser() {
		this.updateTest("brotherhood2", "path1", IllegalArgumentException.class);
	}

	@Test
	public void testDelete() {
		this.deleteTest("brotherhood1", "path1", null);
	}

	@Test
	public void testDeleteWrongUser() {
		this.deleteTest("brotherhood2", "path1", IllegalArgumentException.class);
	}

	// Methods

	private void findOneTest(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.pathService.findOne(id);
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
			this.pathService.findAll(ids);
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
			this.pathService.findAll();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void createTest(final String username, final Parade parade, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.pathService.create(parade);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void saveTest(final String username, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Parade parade = this.paradeService.findOne(super.getEntityId(paradeBeanName));
			final Path path = this.pathService.create(parade);
			this.pathService.save(path);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void updateTest(final String username, final String pathBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Path path = this.pathService.findOne(super.getEntityId(pathBeanName));
			this.pathService.save(path);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	public void deleteTest(final String username, final String pathBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Path path = this.pathService.findOne(super.getEntityId(pathBeanName));
			this.pathService.delete(path);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

}
