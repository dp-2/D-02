
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "history")
})
public class LinkRecord extends DomainEntity {

	//Atributo	
	private String	title;
	private String	text;
	private String	linkBrotherhood;

	//relaciones
	private History	history;


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

	@NotNull
	@Valid
	@ManyToOne(optional = true)
	public History getHistory() {
		return this.history;
	}

	public void setHistory(final History history) {
		this.history = history;
	}

	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getLinkBrotherhood() {
		return this.linkBrotherhood;
	}

	public void setLinkBrotherhood(final String linkBrotherhood) {
		this.linkBrotherhood = linkBrotherhood;
	}

}
