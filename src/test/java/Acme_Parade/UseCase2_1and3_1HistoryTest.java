
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
public class UseCase2_1and3_1HistoryTest extends AbstractTest {

	//2. An actor who is not authenticated must be able to:
	//1. Display the history of every brotherhood that he or she can display.
	//3. An actor who is authenticated as a brotherhood must be able to:
	//1. Manage their history, which includes listing, displaying, creating, updating, and deleting its records.

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
			//Probamos con un usuario que no exista y por lo tanto no debe mostrar la history(CASO NEGATIVO)
			}, {
				"brotherhood1", null
			//Este hermandad si esta registrado en el sistema y deberia ver la historia(CASO POSITIVO)
			}, {
				null, null
			//Entramos como un actor no autenticado y deberia ver la history(CASO POSITIVO)
			}, {
				"DonHacker", java.lang.IllegalArgumentException.class
			//Este admin no  esta registrado en el sistema y no deberia mostrar la history(CASO NEGATIVO)
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.AuthorityMethod((String) AccessDashBoardTest[i][0], (Class<?>) AccessDashBoardTest[i][1]);
	}

	//Metodo para comprobar el display
	private void AuthorityMethod(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos
			this.authenticate(username);
			//Cogemos una historia
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
			//Probamos con un user brotherhood que no exista y por lo tanto no tiene historia(CASO NEGATIVO)
			}, {
				"brotherhood1", "TitleChange", null
			//Esta hermandad si esta registrado en el sistema y puede editar su historia(CASO POSITIVO)
			}, {
				"member1", "TitleChange", java.lang.IllegalArgumentException.class
			//Un miembro no debe editar una historia(CASO NEGATIVO)
			}, {
				"DonHacker", "TitleChange", java.lang.IllegalArgumentException.class
			//Este usuario no existe no deberia editar una historia(CASO NEGATIVO)
			},

		};
		for (int i = 0; i < AccessDashBoardTest.length; i++)
			this.AuthorityMethod2((String) AccessDashBoardTest[i][0], (String) AccessDashBoardTest[i][1], (Class<?>) AccessDashBoardTest[i][2]);
	}
	private void AuthorityMethod2(final String username, final String title, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			//Nos autenticamos
			this.authenticate(username);

			final List<History> res = this.historyService.findAll();
			//Cogemos una historia
			final History h = res.get(0);
			//Modificamos el titulo
			h.setTitle(title);
			//Guardamos
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
		//Cogemos la historia del actor logueado
		this.historyService.findOneByBrotherhoodId(id);
		//Creamos sus records
		final MiscellaneousRecord m = this.miscellaneousService.create();
		final LinkRecord l = this.linkService.create();
		final PeriodRecord p = this.personalService.create();
		final LegalRecord lr = this.legalService.create();
		m.setTitle("TEST");
		l.setTitle("TEST");
		p.setTitle("TEST");
		lr.setTitle("TEST");
		m.setText("TEXT");
		//Guardamos
		this.miscellaneousService.save(m);
		this.unauthenticate();

		Assert.isTrue(m.getTitle() == "TEST");
		Assert.isTrue(l.getTitle() == "TEST");
		Assert.isTrue(p.getTitle() == "TEST");
		Assert.isTrue(lr.getTitle() == "TEST");

	}

	@Test
	public void editAndDeleteRecordsHistory() {
		//Nos autenticamos
		this.authenticate("brotherhood1");
		final Brotherhood br = (Brotherhood) this.actorService.findActorByUsername("brotherhood1");
		final Integer id = br.getId();
		//cogemos la historia del actor logueado
		this.historyService.findOneByBrotherhoodId(id);
		final History h = this.historyService.findOneByBrotherhoodId(id);
		final List<MiscellaneousRecord> m = (List<MiscellaneousRecord>) this.miscellaneousService.findAll();
		final MiscellaneousRecord m1 = m.get(0);

		final List<LegalRecord> l = (List<LegalRecord>) this.legalService.findAll();
		final LegalRecord l1 = l.get(0);
		m1.setTitle("TEST");
		m1.setText("Test");
		m1.setHistory(h);
		//Borramos
		this.legalService.delete(l1);
		//Guardamo el cambio
		this.miscellaneousService.save(m1);
		this.unauthenticate();
		Assert.isTrue(m1.getTitle() == "TEST");

	}
}
