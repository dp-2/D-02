
package usecases.test_v1;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.MarchRepository;
import services.MarchService;
import services.MemberService;
import services.ParadeService;
import utilities.AbstractTest;
import domain.March;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase7Test extends AbstractTest {

	/*
	 * A member may request to march on a procession.
	 * The status of a request can be either "PENDING", "APPROVED", or "REJECTED".
	 * Approved requests must record the row and column at which the corresponding member
	 * must march (both are positive, non-null numbers); rejected requests must record the
	 * reason why.
	 */

	//Services

	@Autowired
	private MarchService	marchService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private ParadeService	paradeService;

	@Autowired
	private MarchRepository	marchRepository;


	@Test
	public void driver() {
		this.before();
		// Un usuario crea y guarda una march request (POSITIVO)
		this.templateSave("member2", new String[] {
			"parade4", "member2", null, "PENDING"
		}, null, null);
		//Un usuario crea y guarda una petición para otro usuario (NEGATIVO)
		this.templateSave("member2", new String[] {
			"parade1", "member1", null, "PENDING"
		}, null, IllegalArgumentException.class);
		// Un usuario aprueba una march request (POSITIVO)
		this.templateUpdate("brotherhood1", "march4", new String[] {
			"parade1", "member4", null, "APPROVED"
		}, null, null);
		// Un usuario aprueba una march request que no es suya (NEGATIVO)
		this.templateUpdate("brotherhood3", "march2", new String[] {
			"parade1", "member2", null, "APPROVED"
		}, null, IllegalArgumentException.class);
		// Un usuario rechaza una request con motivo (POSITIVO)
		this.templateUpdate("brotherhood1", "march5", new String[] {
			"parade3", "member1", "reason", "REJECTED"
		}, null, null);
		// Un usuario rechaza una request sin reason (NEGATIVO)
		this.templateUpdate("brotherhood1", "march3", new String[] {
			"parade3", "member2", null, "REJECTED"
		}, null, IllegalArgumentException.class);

	}

	// Methods

	public void before() {
		// Se preparan las march para los tests
		for (final March m : this.marchService.findAll()) {
			m.setStatus("PENDING");
			m.setLocation(new ArrayList<Integer>());
			this.marchRepository.save(m);
		}
		this.marchService.flush();
		this.authenticate("member1");
		this.marchService.delete(this.marchService.findOne(super.getEntityId("march1")));
		this.unauthenticate();
		this.marchService.flush();
	}
	private void templateSave(final String username, final String[] parameters, final Integer[] locations, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Integer paradeId = super.getEntityId(parameters[0]);
			final Integer memberId = super.getEntityId(parameters[1]);
			March march = this.marchService.create(paradeId, memberId);
			march = this.marchAssignParametersNoLocations(march, parameters);
			final March res = this.marchService.save(march);
			this.marchService.flush();
			System.out.println(res.getMember().getUserAccount().getUsername());
			Assert.notNull(this.marchService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void templateUpdate(final String username, final String marchBeanName, final String[] parameters, final Integer[] locations, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			March march = this.marchService.findOne(super.getEntityId(marchBeanName));
			final int marchId = march.getId();
			final int marchVersion = march.getVersion();
			march = this.marchAssignParametersNoLocations(march, parameters);
			final March res = this.marchService.save(march);
			Assert.isTrue(res.getId() == marchId && res.getVersion() == marchVersion + 1);
			// Comprueba que si está aprobada se asignan las posiciones automáticamente
			if (res.getStatus().equals("APPROVED")) {
				Assert.notNull(res.getLocation());
				Assert.notEmpty(res.getLocation());
				for (final Integer i : res.getLocation())
					Assert.isTrue(i >= 0);
			}
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public March marchAssignParametersNoLocations(final March m, final String[] parameters) {
		m.setParade(this.paradeService.findOne(super.getEntityId(parameters[0])));
		m.setMember(this.memberService.findOne(super.getEntityId(parameters[1])));
		m.setReason(parameters[2]);
		m.setStatus(parameters[3]);
		// Al salir del formulario la location es null si está vacía
		if (m.getLocation() != null)
			if (m.getLocation().isEmpty())
				m.setLocation(null);
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
