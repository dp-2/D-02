
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintValidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.History;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class HistoryServiceTest extends AbstractTest {

	//Service----------------------------------------------------------------------

	@Autowired
	private HistoryService	historyService;


	//----------------------------------------------------------------------------

	@Test
	public void driver() {
		final Object testingData[][] = {
			{
				"customer1", "announcement7", null
			}, {
				null, "announcement7", IllegalArgumentException.class
			}, {
				"reviewer1", "announcement10", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void template(final String username, final int announcementId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			this.historyService.registerPrincipal(announcementId);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test(expected = ConstraintValidator.cl)
	public void sampleValidationTest(){
		History history;
		history = new History;
//		history.setTitle(""); //Wrong name!!
//		history.setBrotherhood("brotherhood18781717")(); // Wrong brotherhood!!
		
		this.historyService.save(entity);
	}
}
