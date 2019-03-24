
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.History;
import domain.InceptionRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	//Service-------------------------------------------
	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private HistoryService			historyService;


	// Tests

	@Test
	public void testFindOne() {
		final Integer id = new ArrayList<InceptionRecord>(this.inceptionRecordService.findAll()).get(0).getId();
		this.findOneTest(null, id, null);
	}

	@Test
	public void testFindOneIdNegative() {
		final Integer id = -1;
		this.findOneTest(null, id, null);
	}

	@Test
	public void testFindAll() {
		this.findAllTest(null, null);
	}

	@Test
	public void testSave() {
		final List<String> photos = new ArrayList<String>();
		photos.add("http://photo1");
		photos.add("http://photo2");
		this.saveTest("brotherhood1", new String[] {
			"history1", "text", "title"
		}, photos, null);
	}

	@Test
	public void testSaveWrongUser() {
		final List<String> photos = new ArrayList<String>();
		photos.add("http://photo1");
		photos.add("http://photo2");
		this.saveTest("brotherhood2", new String[] {
			"history1", "text", "title"
		}, photos, IllegalArgumentException.class);
	}

	@Test
	public void testUpdate() {
		final List<String> photos = new ArrayList<String>();
		photos.add("http://photo1");
		photos.add("http://photo2");
		this.updateTest("brotherhood1", "inceptionRecord1", new String[] {
			"history1", "text", "title"
		}, photos, null);
	}
	@Test
	public void testUpdateWrongUser() {
		final List<String> photos = new ArrayList<String>();
		photos.add("http://photo1");
		photos.add("http://photo2");
		this.updateTest("brotherhood2", "inceptionRecord1", new String[] {
			"history1", "text", "title"
		}, photos, IllegalArgumentException.class);
	}

	@Test
	public void testDelete() {
		this.deleteTest("brotherhood1", "inceptionRecord1", null);
	}

	@Test
	public void testDeleteWrongUser() {
		this.deleteTest("brotherhood2", "inceptionRecord1", IllegalArgumentException.class);
	}

	// Methods

	private void findOneTest(final String username, final Integer id, final Class<?> expected) {
		Class<?> caught = null;
		try {
			this.inceptionRecordService.findOne(id);
		} catch (final Throwable t) {
			super.authenticate(username);
			caught = t.getClass();
			super.authenticate(null);
		}
		super.checkExceptions(expected, caught);
	}

	private void findAllTest(final String username, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			this.inceptionRecordService.findAll();
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void saveTest(final String username, final String[] parameters, final List<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final String historyBeanName = parameters[0];
			final History history = this.historyService.findOne(super.getEntityId(historyBeanName));
			final InceptionRecord res = this.inceptionRecordService.createAndSave(history);
			this.inceptionRecordService.flush();
			Assert.notNull(this.inceptionRecordService.findOne(res.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	private void updateTest(final String username, final String inceptionRecordBeanName, final String[] parameters, final List<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(super.getEntityId(inceptionRecordBeanName));
			final Integer inceptionRecordId = inceptionRecord.getId();
			final Integer inceptionRecordVersion = inceptionRecord.getVersion();
			inceptionRecord = this.inceptionRecordAssignParameters(inceptionRecord, parameters, photos);
			final InceptionRecord res = this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();
			Assert.isTrue(res.getId() == inceptionRecordId && res.getVersion() == inceptionRecordVersion + 1);
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	public void deleteTest(final String username, final String inceptionRecordBeanName, final Class<?> expected) {
		Class<?> caught = null;
		try {
			super.authenticate(username);
			final InceptionRecord inceptionRecord = this.inceptionRecordService.findOne(super.getEntityId(inceptionRecordBeanName));
			this.inceptionRecordService.delete(inceptionRecord);
			Assert.isNull(this.inceptionRecordService.findOne(inceptionRecord.getId()));
			super.authenticate(null);
		} catch (final Throwable t) {
			caught = t.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	// Others

	public InceptionRecord inceptionRecordAssignParameters(final InceptionRecord i, final String[] parameters, final List<String> photos) {
		final History history = this.historyService.findOne(super.getEntityId(parameters[0]));
		i.setHistory(history);
		i.setPhotos(photos);
		i.setText(parameters[1]);
		i.setTitle(parameters[2]);
		return i;
	}

}
