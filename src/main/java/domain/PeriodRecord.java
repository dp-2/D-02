
package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "history")
})
public class PeriodRecord extends DomainEntity {

	//Atributos
	private String			title;
	private String			text;
	private Date			startYear;
	private Date			endYear;
	private List<String>	photos;

	//relaciones
	private History			history;


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

	@NotNull
	@Valid
	@ManyToOne(optional = true)
	public History getHistory() {
		return this.history;
	}

	public void setHistory(final History history) {
		this.history = history;
	}

	//Getters y Setters
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Past
	public Date getStartYear() {
		return this.startYear;
	}

	public void setStartYear(final Date startYear) {
		this.startYear = startYear;
	}
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	public Date getEndYear() {
		return this.endYear;
	}

	public void setEndYear(final Date endYear) {
		this.endYear = endYear;
	}

	@ElementCollection
	@Valid
	@NotEmpty
	public List<String> getPhotos() {
		return this.photos;
	}

	public void setPhotos(final List<String> photos) {
		this.photos = photos;
	}

}
