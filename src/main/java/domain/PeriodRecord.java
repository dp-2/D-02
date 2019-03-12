
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord {

	//Atributos
	private Date			startYear;
	private Date			endYear;
	private Collection<Url>	photos;


	//Getters y Setters
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	@Past
	public Date getStartYear() {
		return this.startYear;
	}

	public void setStartYear(final Date startYear) {
		this.startYear = startYear;
	}
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getEndYear() {
		return this.endYear;
	}

	public void setEndYear(final Date endYear) {
		this.endYear = endYear;
	}

	@ElementCollection
	@Valid
	@NotNull
	@NotEmpty
	public Collection<Url> getPhotos() {
		return this.photos;
	}

	public void setPhotos(final Collection<Url> photos) {
		this.photos = photos;
	}

}
