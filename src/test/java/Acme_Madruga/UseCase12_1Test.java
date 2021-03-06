
package Acme_Madruga;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.Authority;
import security.UserAccount;
import services.AdministratorService;
import utilities.AbstractTest;
import domain.Administrator;

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
	private AdministratorService	administratorService;


	// Tests ------------------------------------------------------------------

	@Test
	public void driver() {
		// Un admin registra a otro admin 

		//b) Positive tests
		//c) analysis of sentence coverage: 100%
		//d) Using one of the actors that is the admin1
		this.templateRegisterAdmin("admin1", new String[] {
			"address", "email@email.com", "middleName", "name", "650190424", "http://photo", "surname", "username", "password"
		}, null);

		//b) Negative test , because a non authenticated actor doesn't crate admins
		//c) analysis of sentence coverage: 100%
		//d) Using a non authenticated actor

		// Un actor trata de registrarse como admin
		this.templateRegisterAdmin(null, new String[] {
			"address", "email@email.com", "middleName", "name", "650190424", "http://photo", "surname", "username2", "password2"
		}, IllegalArgumentException.class);
	}
	// Ancillary Methods

	private void templateRegisterAdmin(final String username, final String[] parameters, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Administrator administrator = this.administratorService.create();
			administrator = this.adminAssignParameters(administrator, parameters);
			final Administrator res = this.administratorService.save(administrator);
			Assert.notNull(this.administratorService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	public Administrator adminAssignParameters(final Administrator a, final String[] parameters) {
		a.setAddress(parameters[0]);
		a.setEmail(parameters[1]);
		a.setMiddleName(parameters[2]);
		a.setName(parameters[3]);
		a.setPhone(parameters[4]);
		a.setPhoto(parameters[5]);
		a.setSurname(parameters[6]);

		final UserAccount account = new UserAccount();
		final Authority authority = new Authority();
		authority.setAuthority("ADMIN");
		final List<Authority> authorities = new ArrayList<>();
		authorities.add(authority);
		a.setUserAccount(account);

		a.getUserAccount().setUsername(parameters[7]);
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		a.getUserAccount().setPassword(encoder.encodePassword(parameters[8], null));
		return a;
	}
}
