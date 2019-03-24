
package usecases.test_v1;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Brotherhood;
import domain.Parade;
import services.BrotherhoodService;
import services.ParadeService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase10_5Test extends AbstractTest {

	//	10. An actor who is authenticated as a brotherhood must be able to::
	//		5.	Manage their parade, which includes listing,
	//			showing, creating, updating, and deleting them.

	// System under test ------------------------------------------------------

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	// Tests ------------------------------------------------------------------


	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null //Brotherhood puede ver sus parades (POSITIVO)
			}, {
				null, AssertionError.class //Un actor no autenticado no tiene parades (NEGATIVO) 
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
	public void driverCreating() {
		System.out.println("=====CREATING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null //Brotherhood puede crear parades (POSITIVO)
			}, {
				null, IllegalArgumentException.class //Un actor no autenticado no puede crear parades (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateCreating((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverUpdating() {
		System.out.println("=====UPDATING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", "parade1", null//Parade no final (POSITIVO)
			}, {
				"brotherhood1", "parade3", IllegalArgumentException.class //Parade final(NEGATIVO) 
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
	public void driverDeleting() {
		System.out.println("=====DELETING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", "parade1", null//Parade no final (POSITIVO)
			}, {
				"brotherhood1", "parade3", DataIntegrityViolationException.class //Parade final(NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateDeleting((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
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

			//Buscamos los sponsorships
			final List<Parade> parades = this.paradeService.findParadesByBrotherhoodId(principal.getId());
			for (final Parade parade : parades)
				if (parade.isFfinal() == true)
					System.out.println(parade.getTitle() + ", FINAL");
				else
					System.out.println(parade.getTitle() + ", NO FINAL");
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

	protected void templateCreating(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Creamos una parade
			final Parade parade = this.paradeService.create();
			parade.setDescription("Description Test");
			parade.setTitle("Title Test");
			parade.setMomentOrganised(new Date(2020, 7, 15));

			//Guardamos la parade
			final Parade save = this.paradeService.save(parade);

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Creado correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateUpdating(final String username, final String parade, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos y editamos una parade
			final Parade paradeBD = this.paradeService.findOne(this.getEntityId(parade));

			paradeBD.setTitle("Title Test");

			//Guardamos
			final Parade saved = this.paradeService.save(paradeBD);

			System.out.println(saved.getTitle());

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Editado correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateDeleting(final String username, final String parade, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos una parade
			final Parade paradeBD = this.paradeService.findOne(this.getEntityId(parade));

			//Borramos
			this.paradeService.delete(paradeBD);

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Borrado correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

}
