
package usecases.test_v2;

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
import services.HistoryService;
import services.LegalRecordService;
import services.LinkRecordService;
import services.MiscellaneousRecordService;
import services.PeriodRecordService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.History;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase2_1_3_1Test extends AbstractTest {

	//Service----------------------------------------------------------------------

	@Autowired
	private HistoryService				historyService;

	@Autowired
	private ActorService				actorService;
	@Autowired
	private MiscellaneousRecordService	miscellaneousService;
	@Autowired
	private BrotherhoodService			brotherhoodService;
	@Autowired
	private LinkRecordService			linkService;
	@Autowired
	private PeriodRecordService			personalService;
	@Autowired
	private LegalRecordService			legalService;


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

			this.historyService.findAll().get(0);

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

	@Test
	public void createRecordHistory() {
		this.authenticate("brotherhood1");
		final Brotherhood br = (Brotherhood) this.actorService.findActorByUsername("brotherhood1");
		final Integer id = br.getId();
		this.historyService.findOneByBrotherhoodId(id);
		final MiscellaneousRecord m = this.miscellaneousService.create();
		final LinkRecord l = this.linkService.create();
		final PeriodRecord p = this.personalService.create();
		final LegalRecord lr = this.legalService.create();
		m.setTitle("TEST");
		l.setTitle("TEST");
		p.setTitle("TEST");
		lr.setTitle("TEST");
		m.setText("TEXT");
		this.miscellaneousService.save(m);
		this.unauthenticate();
		//System.out.println(m.getTitle());
		Assert.isTrue(m.getTitle() == "TEST");
		Assert.isTrue(l.getTitle() == "TEST");
		Assert.isTrue(p.getTitle() == "TEST");
		Assert.isTrue(lr.getTitle() == "TEST");

	}
	//
	//	@Override
	//	@Before
	//	public void setUp() {
	//		this.authenticate("brotherhood2");
	//		final Brotherhood br = (Brotherhood) this.actorService.findActorByUsername("brotherhood2");
	//		final History h = this.historyService.findOneByBrotherhoodId(br.getId());
	//		final MiscellaneousRecord m = this.miscellaneousService.create();
	//		m.setHistory(h);
	//		m.setText("TEST");
	//		m.setTitle("TEST");
	//
	//		this.miscellaneousService.save(m);
	//		this.historyService.save(h);
	//		this.unauthenticate();
	//	}
	//	
	//	@Test
	//	public void editMiscellaneousTest() {
	//		this.authenticate("brotherhood1");
	//		final MiscellaneousRecord m = this.miscellaneousService.findOne(6715);
	//		m.setTitle("Si sale este texto es que todo ha ido correctamente");
	//		this.unauthenticate();
	//		System.out.println(m.getTitle());
	//		Assert.isTrue(m.getTitle() == "Si sale este texto es que todo ha ido correctamente");
	//
	//	}

	@Test
	public void editMIscellaneousRecordHistory() {
		this.authenticate("brotherhood1");
		final MiscellaneousRecord m = this.miscellaneousService.findOne(6715);
		m.setTitle("Si sale este texto es que todo ha ido correctamente");
		this.unauthenticate();
		System.out.println(m.getTitle());
		Assert.isTrue(m.getTitle() == "Si sale este texto es que todo ha ido correctamente");

	}

	//------------------------------------------------------------------------------------------

	//	@Test
	//	public void testEditRecordsHistory() {
	//		final Object AccessDashBoardTest[][] = {
	//			{
	//				//				"brotherhood", "TitleChange", java.lang.IllegalArgumentException.class
	//				//			//Probamos con un user admin que no exista
	//				//			}, {
	//				"brotherhood1", "TitleChange", null
	//			//Este admin si esta registrado en el sistema
	//			},
	//
	//		};
	//		for (int i = 0; i < AccessDashBoardTest.length; i++)
	//			this.template((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	//	}
	//	private void template(final String username, final String title, final Class<?> expected) {
	//		Class<?> caught;
	//
	//		caught = null;
	//		try {
	//			this.authenticate(username);
	//
	//			final List<History> res = this.historyService.findAll();
	//			final History h = res.get(0);
	//			final List<MiscellaneousRecord> Mrecords = (List<MiscellaneousRecord>) this.miscellaneousService.findAllByHistoryId(h.getId());
	//			final MiscellaneousRecord m1 = Mrecords.get(0);
	//			if (m1.getHistory() == h) {
	//				m1.setTitle(title);
	//				this.miscellaneousService.save(m1);
	//				this.historyService.save(h);
	//				final String nuevoTitulo = h.getTitle();
	//				Assert.isTrue(nuevoTitulo == title);
	//				Assert.isTrue(m1.getTitle() == title);
	//			} else
	//				System.out.println("MAL");
	//
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//		}
	//
	//		this.checkExceptions(expected, caught);
	//	}
}
