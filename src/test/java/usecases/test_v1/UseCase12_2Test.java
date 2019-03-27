
package usecases.test_v1;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.AdministratorService;
import services.PositionService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Position;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase12_2Test extends AbstractTest {

	// 12.2 Manage the catalogue of positions, which includes listing, showing, creating, updating, and deleting them. Positions can be deleted as long as they are not used

	// System under test ------------------------------------------------------

	@Autowired
	AdministratorService	adminService;

	@Autowired
	PositionService			positionService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"admin1", null
			//Un administrator puede ver las positions (POSITIVO)
			}, {
				"member1", NullPointerException.class
			//Un member no deberia ver lass positions (NEGATIVO) 
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
				"admin1", "position1", null
			//administrator1 puede editar sus position (POSITIVO)
			}, {
				"brotherhoood1", "position1", IllegalArgumentException.class
			//brotherhood1 no deberia editar position  (NEGATIVO) 
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
				"admin1", "position1", null
			//administrator1 puede crear una position (POSITIVO)
			}, {
				"brotherhood1", "aa", IllegalArgumentException.class
			//Member no deberia crear una position (NEGATIVO) 
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
				"admin1", "position1", null
			//administrator1 puede borrar sus dfloat (POSITIVO)
			}, {
				"member1", "position2", IllegalArgumentException.class
			//member1 no deberia borrar un dfloat que no es suyo (NEGATIVO) 
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
			final Administrator principal = this.adminService.findOne(this.getEntityId(username));
			//Buscamos los dfloat del brotherhood
			final Collection<Position> position = this.positionService.findAll();
			for (final Position d : position)
				System.out.println(d.getNameEnglish());
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

	protected void templateUpdating(final String username, final String position1, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos y editamos un dfloat
			final Position position = this.positionService.findOne(this.getEntityId(position1));

			//Cambiamos la propiedad title
			position.setNameEnglish("a");

			//Guardamos
			this.positionService.save(position);

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

			//Creamos una position
			final Position positionbd = this.positionService.create();
			positionbd.setNameEnglish("Inglesito");
			positionbd.setNameSpanish("Españolsitio");

			//Guardamos
			this.positionService.save(positionbd);

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

	protected void templateDelete(final String username, final String position1, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos una position
			final Position positionbd = this.positionService.findOne(this.getEntityId(position1));
			//Borramos la  miscellaneousRecord de la historia
			this.positionService.delete(positionbd);

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
