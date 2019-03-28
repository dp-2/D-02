
package Acme_Madruga;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.BrotherhoodService;
import services.DFloatService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.DFloat;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase10_1Test extends AbstractTest {

	//a) Functional requirements - 10.1 Manage their floats, which includes listing, showing, creating, updating, and deleting them

	// System under test ------------------------------------------------------

	@Autowired
	BrotherhoodService	brotherhoodService;

	@Autowired
	DFloatService		dfloatService;


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
			//Brotherhood puede ver sus dfloats (POSITIVO)
			}, {

				//b) Negative test , a member can´t manage floats
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the member1
				"member1", NullPointerException.class
			//Un member no deberia ver sus dfloats (NEGATIVO) 
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
				//d) Using one of the actors that is the brotherhood1

				"brotherhood1", "dfloat1", null
			//Brotherhood1 puede editar sus dfloat (POSITIVO)
			}, {

				//b) Negative tests because a brotherhood cannot edit any other brotherhood's dfloat
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the brotherhood1 and a float that doesn't belong to him
				"brotherhood1", "dfloat2", IllegalArgumentException.class
			//Brotherhood1 no deberia editar dfloat porque no es suyo (NEGATIVO) 
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

				//b) Positive tests
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the brotherhood1 and one of his floats

				"brotherhood1", "dfloat1", null
			//Brotherhood1 puede crear sus dfloat (POSITIVO)
			}, {

				//b) Negative tests because a member cannot create a float
				//c) analysis of sentence coverage: 100%
				//d) Using an actor that is a member
				"member1", "aa", IllegalArgumentException.class
			//Member no deberia crear un dfloat (NEGATIVO) 
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
				//b) Positive tests
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the brotherhood1 and one of his floats
				"brotherhood1", "dfloat1", null
			//Brotherhood1 puede borrar sus dfloat (POSITIVO)
			}, {

				//b) Negative tests because a brotherhood cannot delete any other brotherhood's dfloat
				//c) analysis of sentence coverage: 100%
				//d) Using one of the actors that is the brotherhood1 and a float that doesn't belong to him
				"brotherhood1", "dfloat2", IllegalArgumentException.class
			//Brotherhood1 no deberia borrar un dfloat que no es suyo (NEGATIVO) 
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
			//Buscamos los dfloat del brotherhood
			final Collection<DFloat> dfloat = this.dfloatService.findAllDFloatsByBrotherhood(principal);
			for (final DFloat d : dfloat)
				System.out.println(d.getTitle());
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

	protected void templateUpdating(final String username, final String dfloat1, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos y editamos un dfloat
			final DFloat dfloat = this.dfloatService.findOne(this.getEntityId(dfloat1));

			//Cambiamos la propiedad title
			dfloat.setTitle("a");

			//Guardamos
			this.dfloatService.save(dfloat);

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

			//Creamos un dfloat 
			final DFloat dfloatbd = this.dfloatService.create();
			dfloatbd.setDescription(text);
			dfloatbd.setTitle("a");

			//Guardamos
			this.dfloatService.save(dfloatbd);

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

	protected void templateDelete(final String username, final String dfloat1, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Cogemos una dfloat de la hermandad
			final DFloat dfloatbd = this.dfloatService.findOne(this.getEntityId(dfloat1));
			//Borramos la  dfloat de la historia
			this.dfloatService.delete(dfloatbd);

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
