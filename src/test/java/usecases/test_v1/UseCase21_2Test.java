
package usecases.test_v1;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Finder;
import domain.Parade;
import services.FinderService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase21_2Test extends AbstractTest {

	//	21. An actor who is authenticated as a member must be able to:
	//		2.	Manage his or her finder, which involves updating the
	//			search criteria, listing its con-tents, and clearing it.

	// System under test ------------------------------------------------------

	@Autowired
	private FinderService finderService;

	// Tests ------------------------------------------------------------------


	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"member1", null //Un miembro puede ver sus resultados (POSITIVO)
			}, {
				null, IllegalArgumentException.class //Un actor autneticado no tiene resultados de una busqueda (NEGATIVO) 
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
				"member1", "finder1", null//Un miembro puede actualizar el criterio de busqueda (POSITIVO)
			}, {
				"member2", "finder1", IllegalArgumentException.class //Ese finder no pertenece a ese miembro (NEGATIVO) 
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

			//Buscamos el finder y mostramos las parades
			final Finder finder = this.finderService.findOneByPrincipal();
			final List<Parade> parades = this.finderService.findParadeByFinder(finder);
			for (final Parade parade : parades)
				System.out.println(parade.getTitle());

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

	protected void templateUpdating(final String username, final String finder, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Buscamos el finder y mostramos las parades
			final Finder finderBD = this.finderService.findOne(this.getEntityId(finder));
			final List<Parade> parades = this.finderService.findParadeByFinder(finderBD);
			for (final Parade parade : parades)
				System.out.println(parade.getTitle());
			System.out.println("\n");

			//Actualizamos el finder
			finderBD.setKeyword("cristo");

			final Finder saved = this.finderService.save(finderBD);

			System.out.println("Finder Actualizado:");
			System.out.println("\n");
			final List<Parade> parades2 = this.finderService.findParadeByFinder(saved);
			for (final Parade parade : parades2)
				System.out.println(parade.getTitle());
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

}
