
package Acme_Madruga;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase9_2Test extends AbstractTest {

	//9. An actor who is authenticated must be able to:
	//2. Edit his or her personal data.

	//Services---------------------------------------------------------------------

	@Autowired
	private ActorService	actorService;


	@Test
	public void authorityTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"FalseBoy", "TEST", java.lang.IllegalArgumentException.class
			//Probamos con un user admin que no exista y que no debia editar sus datos(CASO NEGATIVO)
			}, {
				"member2", "TEST", null
			//Este admin si esta registrado en el sistema y puede editar sus datos personales(CASO POSITIVO)
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.EditTemplate((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}

	private void EditTemplate(final String username, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		final String result = "VACIO";
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Encontramos el actor cuyo nombre de usuario es igual a que pasamos por parámetros
			final Actor a = this.actorService.findActorByUsername(username);
			//Modificamos su nombre
			a.setName(name);
			//Guardamos el cambio
			this.actorService.save(a);
			if (username == a.getName())
				Assert.isTrue(result.equals(name));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
