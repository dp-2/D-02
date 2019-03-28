
package Acme_Madruga;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.BrotherhoodService;
import services.DFloatService;
import services.MemberService;
import services.ParadeService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.DFloat;
import domain.Member;
import domain.Parade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase8_2Test extends AbstractTest {

	//8. An actor who is not authenticated must be able to:
	//		2. List the brotherhoods in the system and navigate to their members, the processions
	//		that they organise, and the floats that they own.

	//Service-------------------------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private MemberService		memberService;
	@Autowired
	private ParadeService		paradeService;
	@Autowired
	private DFloatService		dfloatService;


	// Tests

	@Test
	public void driverListing() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				null
			//Probamos el unico caso que es si lo muestre (POSITIVO) 
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can list the brotherhoods
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateListing((Class<?>) testingData[i][0]);
			j++;
		}
	}

	@Test
	public void driverListing2() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null
			//Todos pueden ver los miembros de brotherhood 1(POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can list the members of a brotherhood
			}, {
				"brotherhood2", null
			//Todos pueden ver los miembros de brotherhood 2(POSITIVO) 
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can list the members of a brotherhood
			}, {
				"brotherhood200", AssertionError.class
			//No se puede acceder a una brotherhood inexistente (NEGATIVO)
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) Users can't list the members of a brotherhood that doesn't exist
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateListing2((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverListing3() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null
			//Todos pueden ver los parades de brotherhood 1(POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can list the parades of a brotherhood
			}, {
				"brotherhood2", null
			//Todos pueden ver los parades de brotherhood 2(POSITIVO) 
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can list the parades of a brotherhood
			}, {
				"brotherhood200", AssertionError.class
			//No se puede acceder a una brotherhood inexistente (NEGATIVO) 
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) Users can't list the parades of a brotherhood that doesn't exist
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateListing3((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	@Test
	public void driverListing4() {
		System.out.println("=====LISTING=====");
		final Object testingData[][] = {
			{
				"brotherhood1", null
			//Todos pueden ver los dfloats de brotherhood 1(POSITIVO)
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can list the floats of a brotherhood
			}, {
				"brotherhood2", null
			//Todos pueden ver los dfloats de brotherhood 2(POSITIVO) 
			//b) Positive test
			//c) analysis of sentence coverage: 100%
			//d) Any user can list the floats of a brotherhood
			}, {
				"brotherhood200", AssertionError.class
			//No se puede acceder a una brotherhood inexistente (NEGATIVO) 
			//b) Negative test
			//c) analysis of sentence coverage: 100%
			//d) Users can't list the floats of a brotherhood that doesn't exists
			}
		};
		int j = 1;
		for (int i = 0; i < testingData.length; i++) {
			System.out.println("Casuistica" + j);
			this.templateListing4((String) testingData[i][0], (Class<?>) testingData[i][1]);
			j++;
		}
	}

	// Ancillary methods ------------------------------------------------------

	protected void templateListing(final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Buscamos los dfloat del brotherhood
			final Collection<Brotherhood> brotherhood = this.brotherhoodService.findAll();
			for (final Brotherhood d : brotherhood)
				System.out.println(d.getTitle());

			System.out.println("\n");
			System.out.println("Mostrados correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateListing2(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Encontramos el brotherhood
			final Brotherhood principal = this.brotherhoodService.findOne(this.getEntityId(username));
			//Buscamos los member del brotherhood
			final Collection<Member> member = this.memberService.listMembersByBrotherhood(principal.getId());
			for (final Member d : member)
				System.out.println(d.getName());

			System.out.println("\n");
			System.out.println("Mostrados correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateListing3(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Encontramos el brotherhood
			final Brotherhood principal = this.brotherhoodService.findOne(this.getEntityId(username));
			//Buscamos los member del brotherhood
			final Collection<Parade> parade = this.paradeService.findParadesByBrotherhoodId(principal.getId());
			for (final Parade d : parade)
				System.out.println(d.getTitle());

			System.out.println("\n");
			System.out.println("Mostrados correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}

	protected void templateListing4(final String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			//Encontramos el brotherhood
			final Brotherhood principal = this.brotherhoodService.findOne(this.getEntityId(username));
			//Buscamos los member del brotherhood
			final Collection<DFloat> dfloat = this.dfloatService.findAllDFloatsByBrotherhood(principal);
			for (final DFloat d : dfloat)
				System.out.println(d.getTitle());

			System.out.println("\n");
			System.out.println("Mostrados correctamente.");
			System.out.println("-----------------------------");
		} catch (final Throwable oops) {
			caught = oops.getClass();

			System.out.println(caught);
			System.out.println("-----------------------------");
		}
		this.checkExceptions(expected, caught);
	}
}
