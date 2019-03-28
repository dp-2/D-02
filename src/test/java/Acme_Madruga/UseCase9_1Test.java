
package Acme_Madruga;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ParadeService;
import utilities.AbstractTest;
import domain.Parade;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase9_1Test extends AbstractTest {

	// 9.1 Do the same as an actor who is not authenticated, but register to the system
	//Listamos los desfiles que pueden ser listado por un actor no autenticado.

	// System under test ------------------------------------------------------

	@Autowired
	private ParadeService	paradeService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"sponsor1", "parade4", null
			//Sponsor1 puede ver una parade final(POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can access to a final parade
			}
		//No hemos encontrado un caso negativo para este caso de uso
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateListing((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			j++;
		}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateListing(final String username, final String parade, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);
			//Cogemos una parade del sistema
			final Parade parade1 = this.paradeService.findOne(this.getEntityId(parade));
			//mostramos su titulo
			System.out.println(parade1.getTitle());
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

}
