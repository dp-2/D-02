
package Acme_Parade;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.HistoryService;
import services.LegalRecordService;
import utilities.AbstractTest;
import domain.History;
import domain.LegalRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase3_1LegalRecord extends AbstractTest {

	//3. An actor who is authenticated as a brotherhood must be able to:
	//1. Manage their history, which includes listing, displaying, creating, updating, and deleting its legal records.

	//Service-------------------------------------------------------------------------

	@Autowired
	private LegalRecordService	legalRecordService;

	//Service-------------------------------------------

	@Autowired
	private HistoryService		historyService;


	// Tests

	@Test
	public void driver() {
		// Una brotherhood crea un InceptionRecord(CASO POSITIVO)
		this.templateCreate("brotherhood1", null);
		// Un admin trata de crear un InceptionRecord, pero no es una brotherhood(CASO NEGATIVO)
		this.templateCreate("admin1", NullPointerException.class);
		// Una brotherhood edita uno de sus InceptionRecord(CASO POSITIVO)
		this.templateUpdate("brotherhood1", "legalRecord1", null);
		// Una brotherhood trata de editar un InceptionRecord de otra brotherhood(CASO NEGATIVO)
		this.templateUpdate("brotherhood2", "legalRecord1", IllegalArgumentException.class);
		// Una brotherhood elimina uno de sus InceptionRecord(CASO POSITIVO)
		this.templateDelete("brotherhood1", "legalRecord1", null);
		// Una brotherhood trata de eliminar un InceptionRecord de otra brotherhood(CASO NEGATIVO)
		this.templateDelete("brotherhood1", "legalRecord2", IllegalArgumentException.class);
	}

	// Methods

	private void templateCreate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//NOs autenticamos
			super.authenticate(username);
			//Creamos el legal record
			final LegalRecord legalRecord = this.legalRecordService.create();
			this.legalRecordAssignParameters(legalRecord, new String[] {
				"history1", "laws", "legalName", "text", "title", "21"
			});
			//Guardamos
			final LegalRecord res = this.legalRecordService.save(legalRecord);
			this.legalRecordService.flush();
			//comprobamos
			Assert.notNull(this.legalRecordService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void templateUpdate(final String username, final String legalRecordBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//Nos autenticamos
			super.authenticate(username);
			//Recuperamos el legal record
			final LegalRecord legalRecord = this.legalRecordService.findOne(super.getEntityId(legalRecordBeanName));
			final int legalRecordId = legalRecord.getId();
			final int legalRecordVersion = legalRecord.getVersion();
			//Modificamos las leyes
			legalRecord.setLaws("Just a test laws for updating");
			//Guardamos el cambio
			final LegalRecord res = this.legalRecordService.save(legalRecord);
			this.legalRecordService.flush();
			//Comprobamos que se ha hecho el cambio
			Assert.isTrue(res.getId() == legalRecordId && res.getVersion() == legalRecordVersion + 1);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	private void templateDelete(final String username, final String legalRecordBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//Nos atenticamos
			super.authenticate(username);
			//Recuperamos el legal record
			final LegalRecord legalRecord = this.legalRecordService.findOne(super.getEntityId(legalRecordBeanName));
			//Borramos
			this.legalRecordService.delete(legalRecord);
			this.legalRecordService.flush();
			Assert.isNull(this.legalRecordService.findOne(legalRecord.getId()));
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
