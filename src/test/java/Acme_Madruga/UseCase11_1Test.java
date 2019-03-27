
package Acme_Madruga;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
public class UseCase11_1Test extends AbstractTest {

	//	11. An actor who is authenticated as a member must be able to::
	//		1.	Manage his or her requests to march on a procession, which
	//			includes listing them by status, showing, creating them,
	//			and deleting them. Note that the requests cannot be updated,
	//			but they can be deleted as long as they are in the pending status.

	// System under test ------------------------------------------------------

	@Autowired
	private EnrollService	enrollService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverDeleting() {
		System.out.println("=====DELATING=====");
		final Object testingData[][] = {
			{
				"member2", null
			//Un miembro puede borrar su peticion de una hermandad  (POSITIVO)
			}, {
				null, AssertionError.class
			//Un actor no autenticado no puede borrar su peticion  de una hermandad (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateDeleting((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverCreating() {
		System.out.println("=====CREATING=====");
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
			this.templateCreating((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}

	}

	// Ancillary methods ------------------------------------------------------

	protected void templateCreating(final String username, final Class<?> expected) {
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

	protected void templateDeleting(final String username, final Class<?> expected) {
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

				//Si no
				if (enroll.getStatus().equals("PENDING"))
					this.enrollService.delete1(enroll);
			}

			System.out.println("\n");
			System.out.println("Deleting enrolls:");
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
