
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.March;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MarchServiceTestC7 extends AbstractTest {

	//Services

	@Autowired
	private MarchService	marchService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private ParadeService	paradeService;


	// Tests

	@Test
	public void testFindOne() {
		final Integer id = new ArrayList<March>(this.marchService.findAll()).get(0).getId();
		this.findOneTest(null, id, null);
	}

	@Test
	public void testFindOneIdNegative() {
		final Integer id = -1;
		this.findOneTest(null, id, IllegalArgumentException.class);
	}

	@Test
	public void testFindAll() {
		this.findAllTest(null, null);
	}

	// Un member crea una march request
	@Test
	public void testCreate() {
		this.createTest("member2", new String[] {
			"parade2", "member2"
		}, null);
	}

	// El usuario no tiene el perfil de autoridad de Member
	@Test
	public void testCreateWrongAuthority() {
		this.createTest("brotherhood1", new String[] {
			"parade2", "brotherhood1"
		}, IllegalArgumentException.class);
	}

	// Un usuario crea y guarda una march request
	@Test
	public void testSave() {
		this.saveTest("member2", new String[] {
			"parade4", "member2", null, "PENDING"
		}, new Integer[] {
			1, 5
		}, null);
	}
	// Un usuario sin autoridad de miembro intenta crear y guardar una request march
	@Test
	public void testSaveWrongAuthority() {
		this.saveTest("brotherhood1", new String[] {
			"parade2", "brotherhood1", null, "PENDING"
		}, new Integer[] {
			1, 5
		}, IllegalArgumentException.class);
	}

	// Un usuario pide marchar en una procesion de una hermandad a la que no pertenece
	@Test
	public void testSaveNotEnrolled() {
		this.saveTest("member4", new String[] {
			"parade2", "member4", null, "PENDING"
		}, new Integer[] {
			1, 5
		}, IllegalArgumentException.class);
	}

	@Test
	public void testSaveAlreadyRequested() {
		this.saveTest("member1", new String[] {
			"parade1", "member1", null, "PENDING"
		}, new Integer[] {
			1, 5
		}, DataIntegrityViolationException.class);
	}

	// Un usuario crea y guarda una petición para otro usuario
	@Test
	public void testSaveWrongUser() {
		this.saveTest("member1", new String[] {
			"parade1", "member2", null, "PENDING"
		}, new Integer[] {
			1, 5
		}, IllegalArgumentException.class);
	}

	// Un usuario modifica una march request
	@Test
	public void testUpdate() {
		this.updateTest("brotherhood1", "march1", new String[] {
			"parade1", "member1", null, "APPROVED"
		}, new Integer[] {
			43, 324
		}, null);
	}

	// Un usuario rechaza una request con motivo
	@Test
	public void testUpdateRejectWithReason() {
		this.updateTest("brotherhood1", "march1", new String[] {
			"parade1", "member1", "reason", "REJECTED"
		}, new Integer[] {
			1, 5
		}, null);
	}

	// Una hermandad actualiza una petición de otra hermandad
	@Test
	public void testUpdateWrongUser() {
		this.updateTest("brotherhood2", "march1", new String[] {
			"parade1", "member1", null, "APPROVED"
		}, new Integer[] {
			1, 5
		}, IllegalArgumentException.class);
	}

	// Un usuario rechaza una request sin reason
	@Test
	public void testUpdateRejectWithoutReason() {
		this.updateTest("brotherhood1", "march1", new String[] {
			"parade1", "member1", null, "REJECTED"
		}, new Integer[] {
			1, 5
		}, IllegalArgumentException.class);
	}

	// Un usuario elimina una de sus request march
	@Test
	public void testDelete() {
		this.deleteTest("member4", "march4", null);
	}

	// Un usuario elimina una request march que no es suya
	@Test
	public void testDeleteWrongUser() {
		this.deleteTest("member2", "march1", IllegalArgumentException.class);
	}

	// Methods

	private void findOneTest(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.marchService.findOne(id);
		} catch (final Throwable t) {
			super.authenticate(username);
			caught = t.getClass();
			super.authenticate(null);
		}
		super.checkExceptions(expected, caught);
	}

	private void findAllTest(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.marchService.findAll();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void createTest(final String username, final String[] parameters, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.marchService.create(super.getEntityId(parameters[0]), super.getEntityId(parameters[1]));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void saveTest(final String username, final String[] parameters, final Integer[] locations, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Integer paradeId = super.getEntityId(parameters[0]);
			final Integer memberId = super.getEntityId(parameters[1]);
			March march = this.marchService.create(paradeId, memberId);
			march = this.marchAssignParameters(march, parameters, locations);
			this.marchService.save(march);
			this.marchService.flush();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void updateTest(final String username, final String marchBeanName, final String[] parameters, final Integer[] locations, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			March march = this.marchService.findOne(super.getEntityId(marchBeanName));
			march = this.marchAssignParameters(march, parameters, locations);
			this.marchService.save(march);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	public void deleteTest(final String username, final String marchBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final March march = this.marchService.findOne(super.getEntityId(marchBeanName));
			this.marchService.delete(march);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public March marchAssignParameters(final March m, final String[] parameters, final Integer[] locations) {
		System.out.println(m.getStatus());
		final List<Integer> locationsList = new ArrayList<Integer>();
		locationsList.add(locations[0]);
		locationsList.add(locations[1]);
		m.setLocation(locationsList);
		m.setParade(this.paradeService.findOne(super.getEntityId(parameters[0])));
		m.setMember(this.memberService.findOne(super.getEntityId(parameters[1])));
		m.setReason(parameters[2]);
		m.setStatus(parameters[3]);
		final March march = this.marchService.findOne(m.getId());
		System.out.println(march.getStatus());
		System.out.println();

		return m;
	}
	// Sentence coverage

	/* Se ha contemplado la casuística presente en el caso de uso */
	/*
	 * Se han contemplado los datos necesarios para comprobar que los mecanismos implementados
	 * para eviatar el incumplimiento de las reglas de negocio funcionan correctamente
	 */

	// Data coverage

	// El coverage es decir qué se está probando, como lo está comentado en los tests, pero más completo

}
