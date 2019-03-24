
package services;

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
import domain.History;
import domain.MiscellaneousRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class HistoryServiceTest extends AbstractTest {

	//Service----------------------------------------------------------------------

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private ActorService				actorService;
	@Autowired
	private MiscellaneousRecordService	miscellaneousService;
	@Autowired
	private BrotherhoodService			brotherhoodService;


	@Test
	public void authorityTest() {
		final Object AccessDashBoardTest[][] = {
			{
				"brotherhood", java.lang.IllegalArgumentException.class
			//Probamos con un user admin que no exista
			}, {
				"brotherhood1", null
			//Este admin si esta registrado en el sistema
			}, {
				"member1", null
			//Este admin si esta registrado en el sistema
			}, {
				"DonHacker", java.lang.IllegalArgumentException.class
			//Este admin si esta registrado en el sistema
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.AuthorityMethod((String) AccessDashBoardTest[i][0], (Class<?>) AccessDashBoardTest[i][1]);
	}

	private void AuthorityMethod(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			this.historyService.findAll();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void testEditTitleHistory() {
		final Object AccessDashBoardTest[][] = {
			{
				"brotherhood", "TitleChange", java.lang.IllegalArgumentException.class
			//Probamos con un user admin que no exista
			}, {
				"brotherhood1", "TitleChange", null
			//Este admin si esta registrado en el sistema
			}, {
				"member1", "TitleChange", java.lang.IllegalArgumentException.class
			//Este admin si esta registrado en el sistema
			}, {
				"DonHacker", "TitleChange", java.lang.IllegalArgumentException.class
			//Este admin si esta registrado en el sistema
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.AuthorityMethod2((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}
	private void AuthorityMethod2(final String username, final String title, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			final List<History> res = this.historyService.findAll();
			final History h = res.get(0);
			h.setTitle(title);
			this.historyService.save(h);
			final String nuevoTitulo = h.getTitle();
			Assert.isTrue(nuevoTitulo == title);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	//	@Test
	//	public void createRecordTest() {
	//		final Object AccessDashBoardTest[][] = {
	//			{
	//				"brotherhood1", "history1", null
	//			//Probamos con un user admin que no exista
	//			},
	//
	//		};
	//		for (int i = 0; i < AccessDashBoardTest.length; i++)
	//			this.createRecordMethod((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	//	}
	//
	//	private void createRecordMethod(final String username, final String history, final Class<?> expected) {
	//		Class<?> caught;
	//
	//		caught = null;
	//		try {
	//			this.authenticate(username);
	//			final MiscellaneousRecord m = this.miscellaneousService.create();
	//			final int id = m.getId();
	//			m.setTitle("Test");
	//			m.setText("Test");
	//			this.miscellaneousService.save(m);
	//			Assert.isTrue(this.miscellaneousService.findAll().contains(m));
	//
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//			System.out.println(oops.getClass());
	//		}
	//
	//		this.checkExceptions(expected, caught);
	//	}
	//	//----------------------------------------------------------------------------
	@Test
	public void createRecordHistory() {
		this.authenticate("brotherhood1");
		final Brotherhood br = (Brotherhood) this.actorService.findActorByUsername("brotherhood1");
		final Integer id = br.getId();
		this.historyService.findOneByBrotherhoodId(id);
		final MiscellaneousRecord m = this.miscellaneousService.create();
		m.setTitle("TEST");
		m.setText("TEXT");
		this.miscellaneousService.save(m);
		this.unauthenticate();
		//System.out.println(m.getTitle());
		Assert.isTrue(m.getTitle() == "TEST");

	}

	@Test
	public void editMIscellaneousRecordHistory() {
		this.authenticate("brotherhood1");
		final MiscellaneousRecord m = this.miscellaneousService.findOne(6715);
		m.setTitle("Si sale este texto es que todo ha ido correctamente");
		this.unauthenticate();
		System.out.println(m.getTitle());
		Assert.isTrue(m.getTitle() == "Si sale este texto es que todo ha ido correctamente");

	}
}
