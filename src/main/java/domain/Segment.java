
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	// Properties

	private double	latitudeOrigin;
	private double	latitudeDestination;
	private double	longitudeOrigin;
	private double	longitudeDestination;
	private Date	timeOrigin;
	private Date	timeDestination;


	@Range(min = -90, max = 90)
	public double getLatitudeOrigin() {
		return this.latitudeOrigin;
	}
	public void setLatitudeOrigin(final double latitudeOrigin) {
		this.latitudeOrigin = latitudeOrigin;
	}

	@Range(min = -90, max = 90)
	public double getLatitudeDestination() {
		return this.latitudeDestination;
	}
	public void setLatitudeDestination(final double latitudeDestination) {
		this.latitudeDestination = latitudeDestination;
	}

	@Range(min = -180, max = 180)
	public double getLongitudeOrigin() {
		return this.longitudeOrigin;
	}
	public void setLongitudeOrigin(final double longitudeOrigin) {
		this.longitudeOrigin = longitudeOrigin;
	}

	@Range(min = -180, max = 180)
	public double getLongitudeDestination() {
		return this.longitudeDestination;
	}
	public void setLongitudeDestination(final double longitudeDestination) {
		this.longitudeDestination = longitudeDestination;
	}

	@NotNull
	public Date getTimeOrigin() {
		return this.timeOrigin;
	}
	public void setTimeOrigin(final Date timeOrigin) {
		this.timeOrigin = timeOrigin;
	}

	@NotNull
	public Date getTimeDestination() {
		return this.timeDestination;
	}
	public void setTimeDestination(final Date timeDestination) {
		this.timeDestination = timeDestination;
	}


	// Relationships

	private Path	path;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Path getPath() {
		return this.path;
	}
	public void setPath(final Path path) {
		this.path = path;
	}

}
