
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class DashBoardServiceTest extends AbstractTest {

	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;


	//Aqui vamos a probar que los parametros de las queries son correctos o incorrectos segun queramos,para esperar que funcione
	@Test
	public void dashboardValueTest() {
		final Object valueTest[][] = {
			{
				"MAX", 4.0, null
			//Este valor es el esperado segun nuestro populate base
			},

			{
				"MIN", 0.0, null
			//Este valor es el esperado segun nuestro populate base
			}, {

				"AVG", 9999.0, java.lang.IllegalArgumentException.class
			//Este valor NO es el de nuestro populate base
			},

			{
				"STD", 9999.0, java.lang.IllegalArgumentException.class
			//Este valor NO es el de nuestro populate base
			},
		};
		for (int i = 0; i < valueTest.length; i++)
			this.DashboardTestTemplate((String) valueTest[i][0], (Double) valueTest[i][1], (Class<?>) valueTest[i][2]);

	}

	private void DashboardTestTemplate(final String string, final Double number, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		Double result = 0.0;
		try {
			this.authenticate("admin1");
			if (string == "MAX") {
				result = this.memberService.membersBrotherhoodStats().get("MAX");
				Assert.isTrue(result.equals(number));
			} else if (string == "MIN") {
				result = this.memberService.membersBrotherhoodStats().get("MIN");
				Assert.isTrue(result.equals(number));
			} else if (string == "AVG") {
				result = this.memberService.membersBrotherhoodStats().get("AVG");
				Assert.isTrue(result.equals(number));
			} else if (string == "STD") {
				result = this.memberService.membersBrotherhoodStats().get("STD");
				Assert.isTrue(result.equals(number));
			}
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void adminTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"admin", java.lang.IllegalArgumentException.class
			//Probamos con un user admin que no exista
			}, {
				"admin1", null
			//Este admin si esta registrado en el sistema
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.adminTestTemplate((String) AccessDashBoardTest[i][0], (Class<?>) AccessDashBoardTest[i][1]);
	}

	private void adminTestTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			this.areaService.avgHermandadesPorArea();
			this.brotherhoodService.brotherhoodLargestHistory();
			this.brotherhoodService.brotherhoodLargestHistoryThanAVG();
			//			final Actor a = this.actorService.findActorByUsername("member2");
			//			this.administratorService.banActor(a);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
