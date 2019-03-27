
package Acme_Parade;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ActorService;
import services.CreditCardService;
import services.ParadeService;
import services.SponsorService;
import services.SponsorshipService;
import utilities.AbstractTest;
import domain.Actor;
import domain.CreditCard;
import domain.Parade;
import domain.Sponsor;
import domain.Sponsorship;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase16_1Test extends AbstractTest {

	//	16. An actor who is authenticated as a sponsor must be able to:
	//		1.	Manage his or her sponsorships, which includes listing,
	//			showing, creating, updating, and removing them.
	//			Note that removing a sponsorship does not actually delete it from the system,
	//			but de-activates it. A de-activated sponsorship can be re-activated later.

	// System under test ------------------------------------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private CreditCardService	cardService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"sponsor1", null
			//Sponsor1 puede ver sus sponsorships (POSITIVO)
			}, {
				null, IllegalArgumentException.class
			//Un actor no autenticado no deberia ver sponsorships (NEGATIVO) 
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
				"sponsor1", "parade3", null
			//Sponsor puede crear sus sponsorships (POSITIVO)
			}, {
				null, "parade3", AssertionError.class
			//Un actor no autenticado no deberia crear sponsorships (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateCreating((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			j++;
		}
	}

	@Test
	public void driverUpdating() {
		System.out.println("=====UPDATING=====");
		final Object testingData[][] = {
			{
				"sponsor1", null
			//Sponsor puede editar sus sponsorship (POSITIVO)
			}, {
				null, AssertionError.class
			//Un actor no autenticado no deberia editar sponsorships (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateUpdating((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}

	}

	@Test
	public void driverDesactivating() {
		System.out.println("=====DESACTIVATING=====");
		final Object testingData[][] = {
			{
				"sponsor1", null
			//Sponsor puede editar sus sponsorship (POSITIVO)
			}, {
				null, AssertionError.class
			//Un actor no autenticado no deberia editar sponsorships (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateDesactivating((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}

	}

	@Test
	public void driverActivating() {
		System.out.println("=====ACTIVATING=====");
		final Object testingData[][] = {
			{
				"sponsor1", null
			//Sponsor puede editar sus sponsorship (POSITIVO)
			}, {
				null, AssertionError.class
			//Un actor no autenticado no deberia editar sponsorships (NEGATIVO) 
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateActivating((String) testingData[i][0], (Class<?>) testingData[i][1]);
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

	protected void templateCreating(final String username, final String parade, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);
			final Sponsor principal = this.sponsorService.findOne(this.getEntityId(username));

			//Creamos el sponsorship
			final Parade paradeBD = this.paradeService.findOne(this.getEntityId(parade));
			//Creamos la tarjeta que va a tener ese sponsorship
			final CreditCard creditCard = this.cardService.create();
			creditCard.setCVVCode(222);
			creditCard.setExpirationMonth(8);
			creditCard.setExpirationYear(2020);
			creditCard.setHolderName("Test");
			creditCard.setMakeName("VISA");
			creditCard.setNumber("1111222233334444");
			final CreditCard savedCard = this.cardService.save(creditCard);
			//Creamos el sponsorship
			final Sponsorship sponsorship = this.sponsorshipService.create(principal.getId());
			sponsorship.setBanner("http://test.com");
			sponsorship.setTarget("http://test.com");
			sponsorship.setActive(false);
			sponsorship.setParade(paradeBD);
			sponsorship.setCreditCard(savedCard);

			//Guardamos
			this.sponsorshipService.save(sponsorship);

			//Nos desautenticamos
			this.unauthenticate();

			System.out.println("\n");
			System.out.println("Creados correctamente.");
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

			//Buscamos los sponsorships del sponsor con el que nos logueamos
			final List<Sponsorship> sponsorships = this.sponsorshipService.findSponsorshipsBySponsorId(principal.getId());

			//Cogemos y editamos un sponsorship
			final Sponsorship sponsorship = sponsorships.get(0);

			if (sponsorship.getActive() == true)
				System.out.println(sponsorship.getBanner() + ", ACTIVATED");
			else
				System.out.println(sponsorship.getBanner() + ", DESACTIVATED");
			//Modificamos la propiedad active
			sponsorship.setActive(false);

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

	protected void templateDesactivating(final String username, final Class<?> expected) {
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
			//Modificamos el sponsorship para desactivarlo
			sponsorship.setActive(false);

			//Guardamos
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

	protected void templateActivating(final String username, final Class<?> expected) {
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
			//Modificamos el sporsorship activandolo
			sponsorship.setActive(true);

			//Guardamos
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
