
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class LegalRecord extends DomainEntity {

	//Atributos
	private String	legalName;
	private String	VATNumber;
	private String	laws;

	//relaciones
	private History	history;


	@Valid
	@ManyToOne(optional = false)
	public History getHistory() {
		return this.history;
	}

	public void setHistory(final History history) {
		this.history = history;
	}

	//Getters and Setters
	@NotBlank
	public String getLegalName() {
		return this.legalName;
	}

	public void setLegalName(final String legalName) {
		this.legalName = legalName;
	}

	@NotBlank
	public String getVATNumber() {
		return this.VATNumber;
	}

	public void setVATNumber(final String vATNumber) {
		this.VATNumber = vATNumber;
	}

	@NotBlank
	public String getLaws() {
		return this.laws;
	}

	public void setLaws(final String laws) {
		this.laws = laws;
	}

}
