
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
import domain.LegalRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class LegalRecordServiceTest extends AbstractTest {

	//Service-------------------------------------------------------------------------

	@Autowired
	private LegalRecordService	legalRecordService;

	//Service-------------------------------------------

	@Autowired
	private HistoryService		historyService;


	// Tests

	@Test
	public void testFindOne() {
		final Integer id = new ArrayList<LegalRecord>(this.legalRecordService.findAll()).get(0).getId();
		this.findOneTest(null, id, null);
	}

	@Test
	public void testFindOneIdNegative() {
		final Integer id = -1;
		this.findOneTest(null, id, null);
	}

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
			"history1", "laws", "legalName", "text", "title", "VATnumber"
		};
		this.saveTest("brotherhood1", parameters, null);
	}

	@Test
	public void testSaveWrongUser() {
		final String[] parameters = new String[] {
			"history1", "laws", "legalName", "text", "title", "VATnumber"
		};
		this.saveTest("brotherhood2", parameters, IllegalArgumentException.class);
	}

	@Test
	public void testSaveWithNullHistory() {
		final String[] parameters = new String[] {
			null, "laws", "legalName", "text", "title", "VATnumber"
		};
		this.saveTest("brotherhood1", parameters, AssertionError.class);
	}

	@Test
	public void testUpdateUnsecureText() {
		final String[] parameters = new String[] {
			"history1", "laws", "legalName", "<>", "title", "VATnumber"
		};
		this.updateTest("brotherhood1", "legalRecord1", parameters, ConstraintViolationException.class);
	}

	@Test
	public void testSaveWithBlankLaws() {
		final String[] parameters = new String[] {
			"history1", "", "legalName", "text", "title", "VATnumber"
		};
		this.saveTest("brotherhood2", parameters, IllegalArgumentException.class);
	}

	@Test
	public void testUpdate() {
		final String[] parameters = new String[] {
			"history1", "laws", "legalName", "text", "title", "VATnumber"
		};
		this.updateTest("brotherhood1", "legalRecord1", parameters, null);
	}

	@Test
	public void testUpdateWrongUser() {
		final String[] parameters = new String[] {
			"history1", "laws", "legalName", "text", "title", "VATnumber"
		};
		this.updateTest("brotherhood2", "legalRecord1", parameters, IllegalArgumentException.class);
	}

	@Test
	public void testUpdateChangeHistory() {
		final String[] parameters = new String[] {
			"history2", "laws", "legalName", "text", "title", "VATnumber"
		};
		this.updateTest("brotherhood1", "legalRecord1", parameters, IllegalArgumentException.class);
	}

	@Test
	public void testUpdateNullHistory() {
		final String[] parameters = new String[] {
			null, "laws", "legalName", "text", "title", "VATnumber"
		};
		this.updateTest("brotherhood1", "legalRecord1", parameters, AssertionError.class);
	}

	@Test
	public void testUpdateBlankTitle() {
		final String[] parameters = new String[] {
			"history1", "laws", "legalName", "text", "", "VATnumber"
		};
		this.updateTest("brotherhood1", "legalRecord1", parameters, ConstraintViolationException.class);
	}

	@Test
	public void testUpdateUnsecureLegalName() {
		final String[] parameters = new String[] {
			"history1", "laws", "<script></script>", "text", "title", "VATnumber"
		};
		this.updateTest("brotherhood1", "legalRecord1", parameters, ConstraintViolationException.class);
	}

	@Test
	public void testDelete() {
		this.deleteTest("brotherhood1", "legalRecord1", null);
	}

	@Test
	public void testDeleteWrongUser() {
		this.deleteTest("brotherhood2", "legalRecord1", IllegalArgumentException.class);
	}

	// Methods

	private void findOneTest(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.legalRecordService.findOne(id);
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
			this.legalRecordService.findAll();
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
			this.legalRecordService.create();
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
			final LegalRecord legalRecord = this.legalRecordService.create();
			this.legalRecordAssignParameters(legalRecord, parameters);
			this.legalRecordService.save(legalRecord);
			this.legalRecordService.flush();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void updateTest(final String username, final String legalRecordBeanName, final String[] parameters, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			LegalRecord legalRecord = this.legalRecordService.findOne(super.getEntityId(legalRecordBeanName));
			legalRecord = this.legalRecordAssignParameters(legalRecord, parameters);
			this.legalRecordService.save(legalRecord);
			this.legalRecordService.flush();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	public void deleteTest(final String username, final String legalRecordBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final LegalRecord legalRecord = this.legalRecordService.findOne(super.getEntityId(legalRecordBeanName));
			this.legalRecordService.delete(legalRecord);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public LegalRecord legalRecordAssignParameters(final LegalRecord legalRecord, final String[] parameters) {
		final History history = this.historyService.findOne(super.getEntityId(parameters[0]));
		legalRecord.setHistory(history);
		legalRecord.setLaws(parameters[1]);
		legalRecord.setLegalName(parameters[2]);
		legalRecord.setText(parameters[3]);
		legalRecord.setTitle(parameters[4]);
		legalRecord.setVATNumber(parameters[5]);
		return legalRecord;
	}
}
