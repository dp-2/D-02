
package Acme_Madruga;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.EnrollService;
import utilities.AbstractTest;
import domain.Enroll;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase11_2Test extends AbstractTest {

	//	11. An actor who is authenticated as a member must be able to::
	//		2.	Drop out from a brotherhood to which he or she belongs. The system must
	//			record the moment then the drop out takes place. A member may be re-enrolled
	//			after he or she drops out.

	// System under test ------------------------------------------------------

	@Autowired
	private EnrollService	enrollService;


	// Tests ------------------------------------------------------------------

	@Test
	@Before
	public void driverDropingOut() {
		System.out.println("=====DROP OUT=====");
		final Object testingData[][] = {
			{
				"member1", null
			//Un miembro puede salirse de una hermandad  (POSITIVO)
			}, {
				null, AssertionError.class
			//Un actor no autenticado no puede salirse de una hermandad (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateDropingOut((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverEnrolling() {
		System.out.println("=====ENROLLING=====");
		final Object testingData[][] = {
			{
				"member2", null
			//Un miembro podrá hacer un enroll sobre una hermandad (POSITIVO)
			}, {
				null, AssertionError.class
			// Un actor no autenticado no podrá hacer un enroll sobre una hermandad(NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateEnrolling((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}

	}

	// Ancillary methods ------------------------------------------------------

	protected void templateEnrolling(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Buscamos los enrolls
			final List<Enroll> enrolls = new ArrayList<>(this.enrollService.findEnrollByMember(this.getEntityId(username)));
			for (final Enroll enroll : enrolls) {
				if (enroll.getEndMoment() == null)
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", ENROLL");
				else
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", DROP OUT: " + enroll.getEndMoment());

				//Si estamos fuera, hacemos la peticion
				if (enroll.getEndMoment() != null) {
					final Enroll enroll2 = this.enrollService.create(enroll.getBrotherhood().getId());
					this.enrollService.save(enroll2);
				}

			}

			System.out.println("\n");
			System.out.println("Enrolling brotherhoods:");
			final List<Enroll> enrolls2 = new ArrayList<>(this.enrollService.findEnrollByMember(this.getEntityId(username)));
			for (final Enroll enroll : enrolls2)
				if (enroll.getEndMoment() == null)
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", ENROLL");
				else
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", DROP OUT: " + enroll.getEndMoment());

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

	protected void templateDropingOut(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Buscamos los enrolls
			final List<Enroll> enrolls = new ArrayList<>(this.enrollService.findEnrollByMember(this.getEntityId(username)));
			for (final Enroll enroll : enrolls) {
				if (enroll.getEndMoment() == null)
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", ENROLL");
				else
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", DROP OUT: " + enroll.getEndMoment());

				//Si no estamos fuera, nos salimos
				if (enroll.getEndMoment() == null)
					this.enrollService.goOut(enroll.getId());
			}

			System.out.println("\n");
			System.out.println("Droping out brotherhoods:");
			final List<Enroll> enrolls2 = new ArrayList<>(this.enrollService.findEnrollByMember(this.getEntityId(username)));
			for (final Enroll enroll : enrolls2)
				if (enroll.getEndMoment() == null)
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", ENROLL");
				else
					System.out.println(enroll.getBrotherhood().getTitle() + ", " + enroll.getStatus() + ", DROP OUT: " + enroll.getEndMoment());

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
