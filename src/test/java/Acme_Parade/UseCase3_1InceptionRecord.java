
package Acme_Parade;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.HistoryService;
import services.InceptionRecordService;
import utilities.AbstractTest;
import domain.History;
import domain.InceptionRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase3_1InceptionRecord extends AbstractTest {

	//3. An actor who is authenticated as a brotherhood must be able to:
	//1. Manage their history, which includes listing, displaying, creating, updating, and deleting its inception record.

	//Service-------------------------------------------------------------------------

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	//Service-------------------------------------------

	@Autowired
	private HistoryService			historyService;


	// Tests

	@Test
	public void driver() {
		this.before();
		// Una brotherhood crea un InceptionRecord(CASO POSITIVO)
		this.templateCreate("brotherhood3", "history3", null);
		// Un admin trata de crear un InceptionRecord, pero no es una brotherhood(CASO NEGATIVO)
		this.templateCreate("admin1", "history4", IllegalArgumentException.class);
		// Una brotherhood edita uno de sus InceptionRecord(CASO POSIIVO)
		this.templateUpdate("brotherhood1", "inceptionRecord1", null);
		// Una brotherhood trata de editar un InceptionRecord de otra brotherhood(CASO NEGATIVO)
		this.templateUpdate("brotherhood2", "inceptionRecord1", IllegalArgumentException.class);
		// Una brotherhood elimina uno de sus InceptionRecord(CASO POSITIVO)
		this.templateDelete("brotherhood1", "inceptionRecord1", null);
		// Una brotherhood trata de eliminar un InceptionRecord de otra brotherhood(CASO NEGATIVO)
		this.templateDelete("brotherhood1", "inceptionRecord2", IllegalArgumentException.class);
	}

	// Methods

	public void before() {
		final InceptionRecord inceptionRecord3 = this.inceptionRecordService.findOne(super.getEntityId("inceptionRecord3"));
		final InceptionRecord inceptionRecord4 = this.inceptionRecordService.findOne(super.getEntityId("inceptionRecord4"));
		super.authenticate("brotherhood3");
		this.inceptionRecordService.delete(inceptionRecord3);
		this.inceptionRecordService.flush();
		super.authenticate("brotherhood4");
		this.inceptionRecordService.delete(inceptionRecord4);
		this.inceptionRecordService.flush();
		super.authenticate(null);
	}

	private void templateCreate(final String username, final String historyBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//Nos autenticamos
			super.authenticate(username);
			//Cogemos la historia del usuario logueado
			final History history = this.historyService.findOne(super.getEntityId(historyBeanName));
			//Creamos un inception record a esa historia
			final InceptionRecord res = this.inceptionRecordService.createAndSave(history);
			this.inceptionRecordService.flush();
			//Comprobamos que se ha creado
			Assert.notNull(this.inceptionRecordService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	private void templateUpdate(final String username, final String inceptionRecordBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//Nos autenticamos
			super.authenticate(username);
			//Cogemos la inception record que pasamos por parametro
			final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(super.getEntityId(inceptionRecordBeanName));
			final int inceptionRecordId = inceptionRecord.getId();
			final int inceptionRecordVersion = inceptionRecord.getVersion();
			//Modificamos una propiedad
			inceptionRecord.setTitle("Just a test title for updating");
			//Guardamos el cambio
			final InceptionRecord res = this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();
			//Comprobamos que se ha producido el cambio
			Assert.isTrue(res.getId() == inceptionRecordId && res.getVersion() == inceptionRecordVersion + 1);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	private void templateDelete(final String username, final String inceptionRecordBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//Nos autenticamos
			super.authenticate(username);
			//Recuperamos el inception record que pasamos por parametro
			final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(super.getEntityId(inceptionRecordBeanName));
			//Borramos ese inception record
			this.inceptionRecordService.delete(inceptionRecord);
			this.inceptionRecordService.flush();
			//Comprobamos que se ha borrado
			Assert.isNull(this.inceptionRecordService.findOne(inceptionRecord.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others
	//Metodo para modificar las propiedades que pasamos por parametros los valores que deben tomar
	public InceptionRecord inceptionRecordAssignParameters(final InceptionRecord i, final String[] parameters, final List<String> photos) {
		final History history = this.historyService.findOne(super.getEntityId(parameters[0]));
		i.setHistory(history);
		i.setPhotos(photos);
		i.setText(parameters[1]);
		i.setTitle(parameters[2]);
		return i;
	}
}
