
package Acme_Madruga;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.BrotherhoodService;
import services.MarchService;
import services.ParadeService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.March;
import domain.Parade;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase10_6Test extends AbstractTest {

	// 3.1 Manage their history, which includes listing, displaying, creating,
	//updating, and deleting its records

	// System under test ------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MarchService		marchService;

	@Autowired
	private ParadeService		paradeService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null
			//brotherhood1 puede ver las peticiones de sus desfiles(POSITIVO)
			}, {
				"sponsor1", NullPointerException.class
			//Sponsor 1 no tiene desfiles y por lo tanto no puede ver las peticiones(NEGATIVO)
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
				"brotherhood1", "march1", null
			//Brotherhood1 puede editar sus march (POSITIVO)
			}, {
				"brotherhood2", null, AssertionError.class
			//Brotherhood1 no deberia editar un march que no existe(NEGATIVO)
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateUpdating((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
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
			final List<Parade> parades = this.paradeService.findParadesByBrotherhoodId(principal.getId());
			for (final Parade p : parades) {
				//Mostramos un march que pasamos por parámetro
				final Collection<March> marchs = this.marchService.findMarchsByParade(p.getId());
				for (final March m : marchs)
					System.out.println(m.getStatus());

			}
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

	protected void templateUpdating(final String username, final String march, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos y editamos un march
			final March march2 = this.marchService.findOne(this.getEntityId(march));

			//Cambiamos la propiedad status
			march2.setStatus("PENDING");

			//Gruadamos
			this.marchService.save(march2);

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

}
