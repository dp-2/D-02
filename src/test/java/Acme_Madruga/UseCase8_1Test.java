
package Acme_Madruga;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.BrotherhoodService;
import services.MemberService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Member;
import domain.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase8_1Test extends AbstractTest {

	//8. An actor who is not authenticated must be able to:
	//1. Register to the system as a member or a brotherhood.

	//Service-------------------------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private MemberService		memberService;


	// Tests

	@Test
	public void driver() {
		final List<Url> pictures = new ArrayList<Url>();
		// Se registra como hermandad(CASO POSITIVO)
		this.templateRegisterBrotherhood(null, new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "username", "password"
		}, pictures, null);
		// Un hermandad trata de registrarse estando logueada (CASO NEGATIVO)
		this.templateRegisterBrotherhood("brotherhood1", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "username2", "password2"
		}, pictures, IllegalArgumentException.class);
		// Se registra como miembro (CASO POSITVO)
		this.templateRegisterMember(null, new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "username3", "password3"
		}, pictures, null);
		// Un miembro trata de registrarse estando logueado(CASO NEGATIVO)
		this.templateRegisterMember("member1", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "username4", "password4"
		}, pictures, IllegalArgumentException.class);
	}
	// Methods

	private void templateRegisterBrotherhood(final String username, final String[] parameters, final List<Url> pictures, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//Nos autenticamos
			super.authenticate(username);
			Brotherhood b = this.brotherhoodService.create();
			//Asignamos los parámetros para crear la hermandad
			b = this.brotherhoodAssignParameters(b, parameters, pictures);
			//Guardamos la hermandad
			final Brotherhood res = this.brotherhoodService.save(b);
			//Comprobamos que existe
			Assert.notNull(this.brotherhoodService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void templateRegisterMember(final String username, final String[] parameters, final List<Url> pictures, final Class<?> expected) {
		Class<?> caught = null;
		try {
			//Nos autenticamos
			super.authenticate(username);
			Member m = this.memberService.create();
			//Asignamos los parametros de entrada al miembro creado
			m = this.memberAssignParameters(m, parameters, pictures);
			final Member res = this.memberService.save(m);
			//Comprobamos qu existe
			Assert.notNull(this.memberService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others
	//Método que asigna los valores de entrada a las propiedades de una hermandad.
	public Brotherhood brotherhoodAssignParameters(final Brotherhood b, final String[] parameters, final List<Url> pictures) {
		b.setAddress(parameters[0]);
		b.setEmail(parameters[1]);
		b.setMiddleName(parameters[2]);
		b.setName(parameters[3]);
		b.setPhone(parameters[4]);
		b.setPhoto(parameters[5]);
		b.setSurname(parameters[6]);
		b.setTitle(parameters[7]);
		b.getUserAccount().setUsername(parameters[8]);
		b.getUserAccount().setPassword(parameters[9]);
		b.setPictures(pictures);
		return b;
	}
	//Método que asigna los valores de entrada a las propiedades de un miembro
	public Member memberAssignParameters(final Member m, final String[] parameters, final List<Url> pictures) {
		m.setAddress(parameters[0]);
		m.setEmail(parameters[1]);
		m.setMiddleName(parameters[2]);
		m.setName(parameters[3]);
		m.setPhone(parameters[4]);
		m.setPhoto(parameters[5]);
		m.setSurname(parameters[6]);
		m.getUserAccount().setUsername(parameters[7]);
		m.getUserAccount().setPassword(parameters[8]);
		return m;
	}

}
