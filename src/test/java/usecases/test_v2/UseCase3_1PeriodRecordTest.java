
package usecases.test_v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;
import services.BrotherhoodService;
import services.HistoryService;
import services.PeriodRecordService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase3_1PeriodRecordTest extends AbstractTest {

	// 3.1 Manage their history, which includes listing, displaying, creating,
	//updating, and deleting its records

	// System under test ------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private PeriodRecordService	periodRecordService;

	@Autowired
	private HistoryService		historyService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null
			//Brotherhood puede ver sus periodRecords (POSITIVO)
			}, {
				"member1", NullPointerException.class
			//Un member no deberia ver sus periodRecords (NEGATIVO)
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
				"brotherhood1", "periodRecord1", null
			//Brotherhood1 puede editar sus periodRecord (POSITIVO)
			}, {
				"brotherhood1", "periodRecord2", IllegalArgumentException.class
			//Brotherhood1 no deberia editar periodRecord2 porque no es suyo (NEGATIVO)
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
				"brotherhood1", "test", null
			//Brotherhood1 puede crear sus periodRecord (POSITIVO)
			}, {
				null, "test", IllegalArgumentException.class
			//Brotherhood1 no deberia crear un periodRecord con text nulo (NEGATIVO)
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
				"brotherhood1", "periodRecord1", null
			//Brotherhood1 puede borrar sus periodRecord (POSITIVO)
			}, {
				"brotherhood1", "periodRecord2", IllegalArgumentException.class
			//Brotherhood1 no deberia borrar un periodRecord que no es suya (NEGATIVO)
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
			//Buscamos los periodRecord de la historia
			final Collection<PeriodRecord> periodRecords = this.periodRecordService.findAllByHistoryId(h.getId());
			for (final PeriodRecord p : periodRecords)
				System.out.println(p.getTitle());
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

	protected void templateUpdating(final String username, final String periodRecord, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos y editamos un periodRecord
			final PeriodRecord periodRecordbd = this.periodRecordService.findOne(this.getEntityId(periodRecord));

			//Cambiamos la propiedad text
			periodRecordbd.setText("a");

			//Gruadamos
			this.periodRecordService.save(periodRecordbd);

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

	protected void templateCreate(final String username, final String string, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Creamos un periodRecord para la historia de la hermandad logueada
			final PeriodRecord periodRecordbd = this.periodRecordService.create();
			periodRecordbd.setText("a");
			periodRecordbd.setEndYear(new Date(2018, 02, 12));
			periodRecordbd.setStartYear(new Date(2017, 02, 12));
			periodRecordbd.setTitle(string);
			final String a = "http://a.com";
			final List<String> p = new ArrayList<>();
			p.add(a);
			periodRecordbd.setPhotos(p);

			//Gruadamos
			final PeriodRecord saved = this.periodRecordService.save(periodRecordbd);

			System.out.println(this.periodRecordService.findOne(saved.getId()).getTitle());
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

	protected void templateDelete(final String username, final String periodRecord, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos una periodrecord de la hermandad
			final PeriodRecord periodRecordbd = this.periodRecordService.findOne(this.getEntityId(periodRecord));
			//Borramos la  period record de la historia
			this.periodRecordService.delete(periodRecordbd);

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
