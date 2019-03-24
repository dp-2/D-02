
package usecases;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Actor;
import domain.Sponsor;
import domain.Sponsorship;
import services.ActorService;
import services.SponsorService;
import services.SponsorshipService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase16_1Test extends AbstractTest {

	//	16. An actor who is authenticated as a sponsor must be able to:
	//		Manage his or her sponsorships, which includes listing,
	//		showing, creating, updating, and removing them.
	//		Note that removing a sponsorship does not actually delete it from the system,
	//		but de-activates it. A de-activated sponsorship can be re-activated later.

	// System under test ------------------------------------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private SponsorService		sponsorService;

	// Tests ------------------------------------------------------------------


	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"sponsor1", null //Sponsor puede ver sus sponsorships (POSITIVO)
			}, {
				null, IllegalArgumentException.class //Un actor no autenticado no deberia ver sponsorships (NEGATIVO) 
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
				"sponsor1", null//Sponsor puede editar sus sponsorship (POSITIVO)
			}, {
				null, AssertionError.class //Un actor no autenticado no deberia editar sponsorships (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateUpdating((String) testingData[i][0], (Class<?>) testingData[i][1]);
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
			final Actor principal = this.actorService.findPrincipal();

			//Buscamos los sponsorships
			final List<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsBySponsorId(principal.getId());
			for (final Sponsorship sponsorship : sponsorships)
				if (sponsorship.getActive() == true)
					System.out.println(sponsorship.getBanner() + ", ACTIVATED");
				else
					System.out.println(sponsorship.getBanner() + ", DESACTIVATED");
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

	protected void templateUpdating(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);
			final int sponsorId = this.getEntityId(username);
			final Sponsor principal = this.sponsorService.findOne(sponsorId);

			//Buscamos los sponsorships
			final List<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsBySponsorId(principal.getId());

			//Cogemos y editamos un sponsorship
			final Sponsorship sponsorship = sponsorships.get(0);

			if (sponsorship.getActive() == true)
				System.out.println(sponsorship.getBanner() + ", ACTIVATED");
			else
				System.out.println(sponsorship.getBanner() + ", DESACTIVATED");

			sponsorship.setActive(true);

			//Gruadamos
			final Sponsorship saved = this.sponsorshipService.save(sponsorship);

			if (saved.getActive() == true)
				System.out.println(saved.getBanner() + ", ACTIVATED");
			else
				System.out.println(saved.getBanner() + ", DESACTIVATED");

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