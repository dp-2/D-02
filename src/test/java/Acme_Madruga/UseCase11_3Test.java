
package Acme_Madruga;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase11_3Test extends AbstractTest {

	//	11. An actor who is authenticated as a member must be able to:
	//		3. List the brotherhoods to which he or she belongs or has belonged.

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
		//b) Positive tests
		//c) analysis of sentence coverage: 100%
		//d) Using one of the actors that is the member2
		// Un miembro consulta el listado de Brotherhoods a las que pertenece o ha pertenecido (POSITIVO)
		this.templateListBelongedBrotherhoodsTest("member2", expectedBrotherhoods1, null);
		//b) Negative test , because a non authenticated actor doesn't have enrolls
		//c) analysis of sentence coverage: 100%
		//d) Using a non authenticated actor
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

}
