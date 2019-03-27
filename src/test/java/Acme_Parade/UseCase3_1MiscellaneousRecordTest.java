
package Acme_Parade;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.BrotherhoodService;
import services.HistoryService;
import services.MiscellaneousRecordService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase3_1MiscellaneousRecordTest extends AbstractTest {

	// 3.1 Manage their history, which includes listing, displaying, creating,
	//updating, and deleting its miscellaneous records

	// System under test ------------------------------------------------------

	@Autowired
	BrotherhoodService			brotherhoodService;

	@Autowired
	MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	HistoryService				historyService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null
			//Brotherhood puede ver sus miscellaneousRecords (POSITIVO)
			}, {
				"member1", NullPointerException.class
			//Un member no deberia ver sus miscellaneousRecords (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateListing((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverUpdating() {
		System.out.println("=====UPDATING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", "miscellaneousRecord1", null
			//Brotherhood1 puede editar sus miscellaneousRecord (POSITIVO)
			}, {
				"brotherhood1", "miscellaneousRecord2", IllegalArgumentException.class
			//Brotherhood1 no deberia editar miscellaneousRecord2 porque no es suyo (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateUpdating((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			j++;
		}

	}

	@Test
	public void driverCreating() {
		System.out.println("=====CREATING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", "miscellaneousRecord1", null
			//Brotherhood1 puede crear sus miscellaneousRecord (POSITIVO)
			}, {
				"member1", "aa", NullPointerException.class
			//Member no deberia crear un miscellaneousRecord (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			j++;
		}

	}

	@Test
	public void driverDeleting() {
		System.out.println("=====DELETING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", "miscellaneousRecord1", null
			//Brotherhood1 puede borrar sus miscellaneousRecord (POSITIVO)
			}, {
				"brotherhood1", "miscellaneousRecord2", IllegalArgumentException.class
			//Brotherhood1 no deberia borrar un miscellaneousRecord que no es suyo (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			j++;
		}

	}

	// Ancillary methods ------------------------------------------------------

	protected void templateListing(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);
			final Brotherhood principal = this.brotherhoodService.findOne(this.getEntityId(username));
			final History h = this.historyService.findOneByBrotherhoodId(principal.getId());
			//Buscamos los miscellaneousRecord de la historia
			final Collection<MiscellaneousRecord> miscellaneousRecords = this.miscellaneousRecordService.findAllByHistoryId(h.getId());
			for (final MiscellaneousRecord m : miscellaneousRecords)
				System.out.println(m.getTitle());
			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Mostrados correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateUpdating(final String username, final String miscellaneousRecord, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos y editamos un miscellaneousRecord
			final MiscellaneousRecord miscellaneousRecordbd = this.miscellaneousRecordService.findOne(this.getEntityId(miscellaneousRecord));

			//Cambiamos la propiedad text
			miscellaneousRecordbd.setText("a");

			//Gruadamos
			this.miscellaneousRecordService.save(miscellaneousRecordbd);

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Editando correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateCreate(final String username, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Creamos un miscellaneousRecord para la historia de la hermandad logueada
			final MiscellaneousRecord miscellaneousRecordbd = this.miscellaneousRecordService.create();
			miscellaneousRecordbd.setText(text);
			miscellaneousRecordbd.setTitle("a");

			//Gruadamos
			this.miscellaneousRecordService.save(miscellaneousRecordbd);

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Creando correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateDelete(final String username, final String miscellaneousRecord, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos una miscellaneousrecord de la hermandad
			final MiscellaneousRecord miscellaneousRecordbd = this.miscellaneousRecordService.findOne(this.getEntityId(miscellaneousRecord));
			//Borramos la  miscellaneousRecord de la historia
			this.miscellaneousRecordService.delete(miscellaneousRecordbd);

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Borrando correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

}
