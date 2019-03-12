
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class History extends DomainEntity {

	//Atributos
	private String							title;
	private String							text;

	//Relaciones
	private Brotherhood						brotherhood;

	private InceptionRecord					inceptionRecord;
	private Collection<PeriodRecord>		periodRecords;
	private Collection<LinkRecord>			linkRecords;
	private Collection<MiscellaneousRecord>	miscellaneousRecords;
	private Collection<LegalRecord>			legalRecords;


	//Getters y setters de los atributos propios

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	//Getters y setters de las relaciones
	@Valid
	@OneToOne(optional = false)
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}
	public void setHandyWorker(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@NotNull
	@Valid
	@OneToOne(optional = false, cascade = CascadeType.REMOVE)
	public InceptionRecord getInceptionRecord() {
		return this.inceptionRecord;
	}

	public void setInceptionRecord(final InceptionRecord inceptionRecord) {
		this.inceptionRecord = inceptionRecord;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "history")
	public Collection<PeriodRecord> getPeriodRecords() {
		return this.periodRecords;
	}

	public void setPeriodRecords(final Collection<PeriodRecord> periodRecords) {
		this.periodRecords = periodRecords;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "history")
	public Collection<LinkRecord> getLinkRecords() {
		return this.linkRecords;
	}

	public void setLinkRecords(final Collection<LinkRecord> linkRecords) {
		this.linkRecords = linkRecords;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "history")
	public Collection<MiscellaneousRecord> getMiscellaneousRecords() {
		return this.miscellaneousRecords;
	}

	public void setMiscellaneousRecords(final Collection<MiscellaneousRecord> miscellaneousRecords) {
		this.miscellaneousRecords = miscellaneousRecords;
	}

	@NotNull
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "history")
	public Collection<LegalRecord> getLegalRecords() {
		return this.legalRecords;
	}

	public void setLegalRecords(final Collection<LegalRecord> legalRecords) {
		this.legalRecords = legalRecords;
	}

}
