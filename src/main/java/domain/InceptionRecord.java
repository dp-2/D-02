
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class InceptionRecord extends DomainEntity {

	//Atributos
	private String			title;
	private String			text;
	private Collection<Url>	photos;

	//relaciones
	private History			history;


	//Getters y setters de los atributos propios

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Valid
	@OneToOne
	public History getHistory() {
		return this.history;
	}

	public void setHistory(final History history) {
		this.history = history;
	}

	//Getters y Setters
	@ElementCollection
	@Valid
	@NotNull
	@NotEmpty
	public Collection<Url> getPhotos() {
		return this.photos;
	}
	//puede que el notEmpty de problemas a la hora de crearlo

	public void setPhotos(final Collection<Url> photos) {
		this.photos = photos;
	}

}
