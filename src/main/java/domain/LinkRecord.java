
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class LinkRecord extends DomainEntity {

	//Atributo
	private String	linkBrotherhood;

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

	@URL
	public String getLinkBrotherhood() {
		return this.linkBrotherhood;
	}

	public void setLinkBrotherhood(final String linkBrotherhood) {
		this.linkBrotherhood = linkBrotherhood;
	}

}
