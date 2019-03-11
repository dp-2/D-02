
package services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Parade;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	//Service-------------------------------------------------------------------------

	@Autowired
	private ParadeService paradeService;


	//Tests---------------------------------------------------------------------------

	@Test
	public void createAndSaveParadePositive() {
		System.out.println("====PROCESSION TEST createAndSaveParadePositive()====");

		this.authenticate("brotherhood1");

		try {
			final Parade parade = this.paradeService.create();
			final Date future = new Date();
			future.setYear(future.getYear() + 5);

			parade.setTitle("Title Test");
			parade.setDescription("Description Test");
			parade.setMomentOrganised(future);

			Assert.notNull(parade);

			System.out.println("--Final Procesions--");
			final List<Parade> parades = this.paradeService.findParadesFinal();
			for (final Parade parade2 : parades)
				System.out.println(parade2.getTicker() + " " + parade2.getTitle() + " " + parade2.isFfinal());

			System.out.println("Nuestra parade que hemos guardado:");
			final Parade savedNofinal = this.paradeService.save(parade);
			System.out.println(savedNofinal.getTicker() + " " + savedNofinal.getTitle() + " " + savedNofinal.isFfinal());

			System.out.println("Lo guardamos ahora en modo final:");

			final Parade savedfinal = this.paradeService.save(savedNofinal);
			savedfinal.setFfinal(true);

			System.out.println("--Final Procesions--");
			final List<Parade> parades2 = this.paradeService.findParadesFinal();
			for (final Parade parade3 : parades2)
				System.out.println(parade3.getTicker() + " " + parade3.getTitle() + " " + parade3.isFfinal());

			System.out.println("Successfully!");
		} catch (final Exception e) {
			System.out.println("Error: " + e.getMessage());
			System.out.println("Unlucky!");
		}

		this.unauthenticate();
	}

	@Test
	public void createAndSaveParadeNegative() {
		System.out.println("====PROCESSION TEST createAndSaveParadeNegative()====");

		this.authenticate("member1");

		try {
			final Parade parade = this.paradeService.create();
			final Date future = new Date();
			future.setYear(future.getYear() + 5);

			parade.setTitle("Title Test");
			parade.setDescription("Description Test");
			parade.setMomentOrganised(future);

			Assert.notNull(parade);

			System.out.println("--Final Procesions--");
			final List<Parade> parades = this.paradeService.findParadesFinal();
			for (final Parade parade2 : parades)
				System.out.println(parade2.getTicker() + " " + parade2.getTitle() + " " + parade2.isFfinal());

			System.out.println("Nuestra parade que hemos guardado:");
			final Parade savedNofinal = this.paradeService.save(parade);
			System.out.println(savedNofinal.getTicker() + " " + savedNofinal.getTitle() + " " + savedNofinal.isFfinal());

			System.out.println("Lo guardamos ahora en modo final:");

			final Parade savedfinal = this.paradeService.save(savedNofinal);
			savedfinal.setFfinal(true);

			System.out.println("--Final Procesions--");
			final List<Parade> parades2 = this.paradeService.findParadesFinal();
			for (final Parade parade3 : parades2)
				System.out.println(parade3.getTicker() + " " + parade3.getTitle() + " " + parade3.isFfinal());

			System.out.println("Successfully!");
		} catch (final Exception e) {
			System.out.println("Error: " + e.getMessage());
			System.out.println("Unlucky!");
		}

		this.unauthenticate();
	}

	@Test
	public void deleteParadePositive() {
		System.out.println("====PROCESSION TEST deleteParadePositive()====");

		this.authenticate("brotherhood1");

		try {
			final Parade parade = this.paradeService.create();
			final Date future = new Date();
			future.setYear(future.getYear() + 5);

			parade.setTitle("Title Test");
			parade.setDescription("Description Test");
			parade.setMomentOrganised(future);

			Assert.notNull(parade);

			System.out.println("Nuestra parade que hemos guardado en modo borrador:");
			final Parade savedNofinal = this.paradeService.save(parade);
			System.out.println(savedNofinal.getTicker() + " " + savedNofinal.getTitle() + " " + savedNofinal.isFfinal());

			this.paradeService.delete(savedNofinal);

			System.out.println("Successfully!");
		} catch (final Exception e) {
			System.out.println("Error: " + e.getMessage());
			System.out.println("Unlucky!");
		}

		this.unauthenticate();
	}

	@Test
	public void deleteParadeNegative() {
		System.out.println("====PROCESSION TEST deleteParadePositive()====");

		this.authenticate("brotherhood1");

		try {
			final Parade parade = this.paradeService.create();
			final Date future = new Date();
			future.setYear(future.getYear() + 5);

			parade.setTitle("Title Test");
			parade.setDescription("Description Test");
			parade.setMomentOrganised(future);

			Assert.notNull(parade);

			System.out.println("Nuestra parade que hemos guardado en modo borrador:");
			final Parade savedNofinal = this.paradeService.save(parade);
			savedNofinal.setFfinal(true);
			System.out.println(savedNofinal.getTicker() + " " + savedNofinal.getTitle() + " " + savedNofinal.isFfinal());

			this.paradeService.delete(savedNofinal);

			System.out.println("Successfully!");
		} catch (final Exception e) {
			System.out.println("Error: " + e.getMessage());
			System.out.println("Unlucky!");
		}

		this.unauthenticate();
	}
}
