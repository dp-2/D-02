
package Acme_Madruga;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.BrotherhoodService;
import services.HistoryService;
import services.MemberService;
import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Member;
import domain.Url;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UseCase8_1Test extends AbstractTest {

	//Service-------------------------------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;
	@Autowired
	private MemberService		memberService;

	//Service-------------------------------------------

	@Autowired
	private HistoryService		historyService;


	// Tests

	@Test
	public void driver() {
		final List<Url> pictures = new ArrayList<Url>();
		// Se registra como brotherhood
		this.templateRegisterBrotherhood(null, new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "username", "password"
		}, pictures, null);
		// Un actor trata de registrarse como brotherhood
		this.templateRegisterBrotherhood("brotherhood1", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "title", "username2", "password2"
		}, pictures, IllegalArgumentException.class);
		// Se registra como member
		this.templateRegisterMember(null, new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "username3", "password3"
		}, pictures, null);
		// Un actor trata de registrarse como member
		this.templateRegisterMember("member1", new String[] {
			"address", "email@email.com", "middleName", "name", "phone", "http://photo", "surname", "username4", "password4"
		}, pictures, IllegalArgumentException.class);
	}
	// Methods

	private void templateRegisterBrotherhood(final String username, final String[] parameters, final List<Url> pictures, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Brotherhood b = this.brotherhoodService.create();
			b = this.brotherhoodAssignParameters(b, parameters, pictures);
			final Brotherhood res = this.brotherhoodService.save(b);
			Assert.notNull(this.brotherhoodService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void templateRegisterMember(final String username, final String[] parameters, final List<Url> pictures, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			Member m = this.memberService.create();
			m = this.memberAssignParameters(m, parameters, pictures);
			final Member res = this.memberService.save(m);
			Assert.notNull(this.memberService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public Brotherhood brotherhoodAssignParameters(final Brotherhood b, final String[] parameters, final List<Url> pictures) {
		b.setAddress(parameters[0]);
		b.setEmail(parameters[1]);
		b.setMiddleName(parameters[2]);
		b.setName(parameters[3]);
		b.setPhone(parameters[4]);
		b.setPhoto(parameters[5]);
		b.setSurname(parameters[6]);
		b.setTitle(parameters[7]);
		b.getUserAccount().setUsername(parameters[8]);
		b.getUserAccount().setPassword(parameters[9]);
		b.setPictures(pictures);
		return b;
	}

	public Member memberAssignParameters(final Member m, final String[] parameters, final List<Url> pictures) {
		m.setAddress(parameters[0]);
		m.setEmail(parameters[1]);
		m.setMiddleName(parameters[2]);
		m.setName(parameters[3]);
		m.setPhone(parameters[4]);
		m.setPhoto(parameters[5]);
		m.setSurname(parameters[6]);
		m.getUserAccount().setUsername(parameters[7]);
		m.getUserAccount().setPassword(parameters[8]);
		return m;
	}

}
