
package usecases.test_v1;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Administrator;
import security.Authority;
import security.UserAccount;
import services.AdministratorService;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UseCase12_1Test extends AbstractTest {

	// 12. An actor who is authenticated as a administrator must be able to:
	//		1. Register new administrators.

	// System under test ------------------------------------------------------

	@Autowired
	private AdministratorService administratorService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driverRegister() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"admin1", null //Administrador pricipal el que realizara la accion (POSITIVO)
			}, {
				"member1", null //Actor que no podrá realizar la accion (NEGATIVO)
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateRegister((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateRegister(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Nos autenticamos
			this.authenticate(username);

			//Creamos un admin
			final Administrator administrator = this.administratorService.create();
			administrator.setName("name");
			administrator.setEmail("hhdhfkjfsd@sdhd.com");
			administrator.setPhone("650190425");
			administrator.setSurname("surname");
			administrator.setPhoto("http://fkjdhsfkjsd.com");

			final UserAccount account = new UserAccount();
			account.setUsername("admintest");
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			account.setPassword(encoder.encodePassword("admintest", null));
			account.setEnabled(true);

			final Authority authority = new Authority();
			authority.setAuthority("ADMIN");

			final List<Authority> authorities = new ArrayList<>();
			authorities.add(authority);

			account.setAuthorities(authorities);

			final Administrator saved = this.administratorService.save(administrator);
			//mostramos su username
			System.out.println(saved.getUserAccount().getUsername());

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
