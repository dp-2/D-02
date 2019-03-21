
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "brotherhood")
})
public class History extends DomainEntity {

	//Atributos
	private String		title;

	//Relaciones
	private Brotherhood	brotherhood;


	//Getters y setters de los atributos propios

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	//Getters y setters de las relaciones
	@Valid
	@OneToOne(optional = false)
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}
	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

}
