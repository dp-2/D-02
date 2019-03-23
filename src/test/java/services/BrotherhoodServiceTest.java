
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	//Services

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Tests

	@Test
	public void testSuite() {
		final List<Url> pictures = new ArrayList<Url>();
		final Date establishedMoment = new Date(System.currentTimeMillis() - 1000);
		// Se crea y guarda un brotherhood
		this.saveTest(null, new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "username", "password"
		}, pictures, null);
		// Se crea y guarda un brotherhood estando autenticado
		this.saveTest("brotherhood1", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "fusion", "acho"
		}, pictures, IllegalArgumentException.class);
		// Se edita una brotherhood
		this.updateTest("brotherhood1", "brotherhood1", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "fision", "pacho"
		}, pictures, false, false, 0, establishedMoment, null);
		// Una brotherhood intenta editar los datos de otra brotherhood
		this.updateTest("brotherhood1", "brotherhood2", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "username", "password"
		}, pictures, false, false, 0, establishedMoment, IllegalArgumentException.class);
		// Una brotherhood se elimina
		//this.deleteTest("brotherhood1", "brotherhood1", null);
		// Una brotherhood intenta eliminar otra brotherhood
		//this.deleteTest("brotherhood2", "brotherhood1", IllegalArgumentException.class);
	}

	@Test
	public void surpriseMotherfucker() {
		final List<Url> pictures = new ArrayList<Url>();
		final Date establishedMoment = new Date(System.currentTimeMillis() - 1000);
		// Se crea y guarda guarda un brotherhood
		this.updateTest("brotherhood2", "brotherhood1", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "username", "password"
		}, pictures, false, false, 0, establishedMoment, IllegalArgumentException.class);
	}

	// Templates

	private void updateTest(final String username, final String brotherhoodBeanName, final String[] parameters, final List<Url> pictures, final Boolean banned, final Boolean spammer, final Integer score, final Date establishedMoment,
		final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Brotherhood brotherhood = this.brotherhoodService.findOne(super.getEntityId(brotherhoodBeanName));
			final int brotherhoodId = brotherhood.getId();
			final int brotherhoodVersion = brotherhood.getVersion();
			brotherhood = this.brotherhoodAssignParametersUpdate(brotherhood, parameters, pictures, banned, spammer, score, establishedMoment);
			final Brotherhood res = this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();
			Assert.isTrue(res.getId() == brotherhoodId && res.getVersion() == brotherhoodVersion + 1);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void saveTest(final String username, final String[] parameters, final List<Url> pictures, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Brotherhood brotherhood = this.brotherhoodService.create();
			brotherhood = this.brotherhoodAssignParametersSave(brotherhood, parameters, pictures);
			final Brotherhood res = this.brotherhoodService.save(brotherhood);
			this.brotherhoodService.flush();
			Assert.notNull(this.brotherhoodService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	public void deleteTest(final String username, final String brotherhoodBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Brotherhood brotherhood = this.brotherhoodService.findOne(super.getEntityId(brotherhoodBeanName));
			this.brotherhoodService.delete(brotherhood);
			this.brotherhoodService.flush();
			Assert.isNull(this.brotherhoodService.findOne(brotherhood.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public Brotherhood brotherhoodAssignParametersSave(final Brotherhood brotherhood, final String[] parameters, final List<Url> pictures) {
		brotherhood.setAddress(parameters[0]);
		brotherhood.setEmail(parameters[1]);
		brotherhood.setMiddleName(parameters[2]);
		brotherhood.setName(parameters[3]);
		brotherhood.setPhone(parameters[4]);
		brotherhood.setPhoto(parameters[5]);
		brotherhood.setSurname(parameters[6]);
		brotherhood.setTitle(parameters[7]);
		brotherhood.getUserAccount().setUsername(parameters[8]);
		brotherhood.getUserAccount().setPassword(parameters[9]);
		brotherhood.setPictures(pictures);
		return brotherhood;
	}

	public Brotherhood brotherhoodAssignParametersUpdate(final Brotherhood brotherhood, final String[] parameters, final List<Url> pictures, final Boolean banned, final Boolean spammer, final Integer score, final Date establishedMoment) {
		brotherhood.setAddress(parameters[0]);
		brotherhood.setEmail(parameters[1]);
		brotherhood.setMiddleName(parameters[2]);
		brotherhood.setName(parameters[3]);
		brotherhood.setPhone(parameters[4]);
		brotherhood.setPhoto(parameters[5]);
		brotherhood.setSurname(parameters[6]);
		brotherhood.setTitle(parameters[7]);
		brotherhood.getUserAccount().setUsername(parameters[8]);
		brotherhood.getUserAccount().setPassword(parameters[9]);
		brotherhood.setPictures(pictures);
		brotherhood.setBanned(banned);
		brotherhood.setSpammer(spammer);
		brotherhood.setScore(score);
		brotherhood.setEstablishedMoment(establishedMoment);
		return brotherhood;
	}

}
