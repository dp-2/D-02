
package Acme_Parade;

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
public class UseCase2_1and3_1Test extends AbstractTest {

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

	@Test
	public void editAndDeleteRecordsHistory() {
		this.authenticate("brotherhood1");
		final Brotherhood br = (Brotherhood) this.actorService.findActorByUsername("brotherhood1");
		final Integer id = br.getId();
		this.historyService.findOneByBrotherhoodId(id);
		final History h = this.historyService.findOneByBrotherhoodId(id);
		final List<MiscellaneousRecord> m = (List<MiscellaneousRecord>) this.miscellaneousService.findAll();
		final MiscellaneousRecord m1 = m.get(0);

		final List<LegalRecord> l = (List<LegalRecord>) this.legalService.findAll();
		final LegalRecord l1 = l.get(0);
		m1.setTitle("TEST");
		m1.setText("Test");
		m1.setHistory(h);
		this.legalService.delete(l1);
		this.miscellaneousService.save(m1);
		this.unauthenticate();
		Assert.isTrue(m1.getTitle() == "TEST");

	}
}
