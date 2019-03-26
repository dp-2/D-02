
package usecases.test_v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.BrotherhoodService;
import services.EnrollService;
import services.HistoryService;
import services.MemberService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Brotherhood;
import domain.Member;
import domain.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase11_3Test extends AbstractTest {

	//Service-------------------------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private MemberService		memberService;
	@Autowired
	private EnrollService		enrollService;
	@Autowired
	private ActorService		actorService;

	//Service-------------------------------------------

	@Autowired
	private HistoryService		historyService;


	// Tests

	@Test
	public void driver() {
		final List<Brotherhood> expectedBrotherhoods1 = new ArrayList<Brotherhood>();
		expectedBrotherhoods1.add(this.brotherhoodService.findOne(super.getEntityId("brotherhood1")));
		expectedBrotherhoods1.add(this.brotherhoodService.findOne(super.getEntityId("brotherhood2")));
		final List<Brotherhood> expectedBrotherhoods2 = new ArrayList<Brotherhood>();

		// Un miembro consulta el listado de Brotherhoods a las que pertenece o ha pertenecido (POSITIVO)
		this.templateListBelongedBrotherhoodsTest("member2", expectedBrotherhoods1, null);
		// Un usuario sin autenticar consulta el listado de Brotherhoods a las que pertenece o ha pertenecido (NEGATIVO)
		this.templateListBelongedBrotherhoodsTest("brotherhood1", expectedBrotherhoods2, IllegalArgumentException.class);
	}
	// Methods

	private void templateListBelongedBrotherhoodsTest(final String username, final List<Brotherhood> expectedResult, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final Actor actor = this.actorService.findPrincipal();
			final Collection<Brotherhood> brotherhoods = this.enrollService.findBrotherhoodsByMemberId(actor.getId());
			Assert.isTrue(expectedResult.size() == brotherhoods.size() && expectedResult.containsAll(brotherhoods));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	// Others

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
