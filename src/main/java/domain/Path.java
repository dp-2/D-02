
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "parade")
})
public class Path extends DomainEntity {

	// Properties

	// Relationships

	private Collection<Segment>	segments;
	private Parade				parade;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "path", cascade = CascadeType.ALL)
	public Collection<Segment> getSegments() {
		return this.segments;
	}
	public void setSegments(final Collection<Segment> segments) {
		this.segments = segments;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Parade getParade() {
		return this.parade;
	}

	public void setParade(final Parade parade) {
		this.parade = parade;
	}

}
