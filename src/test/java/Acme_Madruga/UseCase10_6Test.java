
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

	// 10.6 Manage the request to march on a procession, which includes listing them by status,
	//	showing them, and deciding on them. When the decision on a pending request is to
	//	accept it, the brotherhood must provide a position in the procession, which is identified 
	//	by means of a row and a column; the system must check that no two members can march at the same
	//	row/column; the system must suggest a good position automatically, but the brotherhood may change 
	//	it. When the decision is to reject it, the brotherhood must provide an explanation.

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

				//b) Positive tests
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the brotherhood1
				"brotherhood1", null
			//brotherhood1 puede ver las peticiones de sus desfiles(POSITIVO)
			}, {

				//b) Negative test , because a sponsor doens't have marchs
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the sponsor1
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

				//b) Positive tests
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the brotherhood1 and one of his marchs
				"brotherhood1", "march1", null
			//Brotherhood1 puede editar sus march (POSITIVO)
			}, {

				//b) Negative test , because the march doesn't exist
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the brotherhood1 and a null march
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
