
package services;

import java.util.ArrayList;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.History;
import domain.MiscellaneousRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MiscellaneousRecordTest extends AbstractTest {

	//	Service	
	@Autowired
	private MiscellaneousRecordService	miscellaneousService;

	@Autowired
	private HistoryService				historyService;


	// Tests

	@Test
	public void testFindOne() {
		final Integer id = new ArrayList<MiscellaneousRecord>(this.miscellaneousService.findAll()).get(0).getId();
		this.findOneTest(null, id, null);
	}

	@Test
	public void testFindOneIdNegative() {
		final Integer id = -1;
		this.findOneTest(null, id, IllegalArgumentException.class);
	}

	//	@Test
	//	public void testFindAllIds() {
	//		final Collection<Integer> ids = new ArrayList<Integer>();
	//		for (final MiscellaneousRecord s : this.miscellaneousService.findAll())
	//			ids.add(s.getId());
	//		this.findAllTest(null, ids, null);
	//	}
	//
	//	@Test
	//	public void testFindAllIdsWrongIds() {
	//		final Collection<Integer> ids = Arrays.asList(-1, 0);
	//		this.findAllTest(null, ids, IllegalArgumentException.class);
	//	}

	@Test
	public void testFindAll() {
		this.findAllTest(null, null);
	}

	@Test
	public void testCreate() {
		this.createTest("brotherhood1", null);
	}

	@Test
	public void testCreateWrongAuthority() {
		this.createTest("amdin1", IllegalArgumentException.class);
	}

	@Test
	public void testSave() {
		final String[] parameters = new String[] {
			"history1", "text", "title"
		};
		this.saveTest("brotherhood1", parameters, null);
	}

	@Test
	public void testSaveWrongUser() {
		final String[] parameters = new String[] {
			"history1", "text", "title"
		};
		this.saveTest("brotherhood2", parameters, IllegalArgumentException.class);
	}

	@Test
	public void testSaveWithNullHistory() {
		final String[] parameters = new String[] {
			null, "text", "title"
		};
		this.saveTest("brotherhood1", parameters, AssertionError.class);
	}

	@Test
	public void testUpdate() {
		final String[] parameters = new String[] {
			"history1", "text", "title"
		};
		this.updateTest("brotherhood1", "miscellaneousRecord1", parameters, null);
	}

	@Test
	public void testUpdateWrongUser() {
		final String[] parameters = new String[] {
			"history1", "text", "title"
		};
		this.updateTest("brotherhood2", "miscellaneousRecord1", parameters, IllegalArgumentException.class);
	}

	@Test
	public void testUpdateChangeHistory() {
		final String[] parameters = new String[] {
			"history2", "text", "title"
		};
		this.updateTest("brotherhood1", "miscellaneousRecord1", parameters, IllegalArgumentException.class);
	}

	@Test
	public void testUpdateNullHistory() {
		final String[] parameters = new String[] {
			null, "text", "title"
		};
		this.updateTest("brotherhood1", "miscellaneousRecord1", parameters, AssertionError.class);
	}

	@Test
	public void testUpdateBlankTitle() {
		final String[] parameters = new String[] {
			"history1", "text", ""
		};
		this.updateTest("brotherhood1", "miscellaneousRecord1", parameters, ConstraintViolationException.class);
	}

	@Test
	public void testDelete() {
		this.deleteTest("brotherhood1", "miscellaneousRecord1", null);
	}

	@Test
	public void testDeleteWrongUser() {
		this.deleteTest("brotherhood2", "miscellaneousRecord1", IllegalArgumentException.class);
	}

	// Methods

	private void findOneTest(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.miscellaneousService.findOne(id);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void findAllTest(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.miscellaneousService.findAll();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void createTest(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.miscellaneousService.create();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void saveTest(final String username, final String[] parameters, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousService.create();
			this.miscellaneousRecordAssignParameters(miscellaneousRecord, parameters);
			this.miscellaneousService.save(miscellaneousRecord);
			this.miscellaneousService.flush();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void updateTest(final String username, final String miscellaneousRecordBeanName, final String[] parameters, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			MiscellaneousRecord miscellaneousRecord = this.miscellaneousService.findOne(super.getEntityId(miscellaneousRecordBeanName));
			miscellaneousRecord = this.miscellaneousRecordAssignParameters(miscellaneousRecord, parameters);
			this.miscellaneousService.save(miscellaneousRecord);
			this.miscellaneousService.flush();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	public void deleteTest(final String username, final String miscellaneousRecordBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final MiscellaneousRecord miscellaneousRecord = this.miscellaneousService.findOne(super.getEntityId(miscellaneousRecordBeanName));
			this.miscellaneousService.delete(miscellaneousRecord);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public MiscellaneousRecord miscellaneousRecordAssignParameters(final MiscellaneousRecord miscellaneousRecord, final String[] parameters) {
		final History history = this.historyService.findOne(super.getEntityId(parameters[0]));
		miscellaneousRecord.setHistory(history);
		miscellaneousRecord.setText(parameters[1]);
		miscellaneousRecord.setTitle(parameters[2]);
		return miscellaneousRecord;
	}

}
