
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
import services.LinkRecordService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase3_1LinkRecordTest extends AbstractTest {

	// 3.1 Manage their history, which includes listing, displaying, creating,
	//updating, and deleting its link records

	// System under test ------------------------------------------------------

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	LinkRecordService	linkRecordService;

	@Autowired
	HistoryService		historyService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null
			//Brotherhood puede ver sus linkRecords (POSITIVO)
			}, {
				"member1", NullPointerException.class
			//Un member no deberia ver sus linkRecords,ya que un miembro no tiene una historia (NEGATIVO) 
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
				"brotherhood1", "linkRecord1", null
			//Brotherhood1 puede editar sus linkRecord (POSITIVO)
			}, {
				"brotherhood1", "linkRecord3", IllegalArgumentException.class
			//Brotherhood1 no deberia editar linkRecord3 porque no es suyo (NEGATIVO) 
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
				"brotherhood1", "a", null
			//Brotherhood1 puede crear sus linkRecord (POSITIVO)
			}, {
				null, "b", IllegalArgumentException.class
			//Un actor que no esta autenticado no deberia crear un linkRecord (NEGATIVO) 
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
				"brotherhood1", "linkRecord1", null
			//Brotherhood1 puede borrar sus linkRecord (POSITIVO)
			}, {
				"brotherhood1", "linkRecord3", IllegalArgumentException.class
			//Brotherhood1 no deberia borrar un linkRecord que no es suyo (NEGATIVO) 
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
			//Cogemos la history
			final History h = this.historyService.findOneByBrotherhoodId(principal.getId());
			//Buscamos los linkRecord de la historia
			final Collection<LinkRecord> linkRecords = this.linkRecordService.findAllByHistoryId(h.getId());
			for (final LinkRecord p : linkRecords)
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

	protected void templateUpdating(final String username, final String linkRecord, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos y editamos un linkRecord
			final LinkRecord linkRecordbd = this.linkRecordService.findOne(this.getEntityId(linkRecord));

			//Cambiamos la propiedad text
			linkRecordbd.setText("a");

			//Gruadamos
			this.linkRecordService.save(linkRecordbd);

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

			//Creamos un linkRecord para la historia de la hermandad logueada
			final LinkRecord linkRecordbd = this.linkRecordService.create();
			linkRecordbd.setText(text);
			linkRecordbd.setTitle("a");
			linkRecordbd.setLinkBrotherhood("http://a.com");

			//Gruadamos
			this.linkRecordService.save(linkRecordbd);

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

	protected void templateDelete(final String username, final String linkRecord, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos una periodrecord de la hermandad
			final LinkRecord linkRecordbd = this.linkRecordService.findOne(this.getEntityId(linkRecord));
			//Borramos la  period record de la historia
			this.linkRecordService.delete(linkRecordbd);

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
