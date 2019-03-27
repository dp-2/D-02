
package Acme_Madruga;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.AreaService;
import services.BrotherhoodService;
import services.MemberService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
public class UseCase12_3Test extends AbstractTest {

	//12.3 Display a dashboard with the following information:
	//	 The average, the minimum, the maximum, and the standard deviation of the
	//	number of members per brotherhood.
	//	 The largest brotherhoods.
	//	 The smallest brotherhoods.
	//	 The ratio of requests to march in a procession, grouped by their status.
	//	 The processions that are going to be organised in 30 days or less.
	//	3
	//	 The ratio of requests to march grouped by status.
	//	 The listing of members who have got at least 10% the maximum number of
	//	request to march accepted.
	//	 A histogram of positions.

	@Autowired
	private AreaService			areaService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private ActorService		actorService;


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

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
