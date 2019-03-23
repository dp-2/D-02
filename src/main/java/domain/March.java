
package domain;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import cz.jirutka.validator.collection.constraints.EachSafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"parade", "member"
	})
})
public class March extends DomainEntity {

	// Properties

	private String			status;
	private String			reason;
	private List<Integer>	location;


	@NotBlank
	@Pattern(regexp = "^PENDING$|^APPROVED$|^REJECTED$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getStatus() {
		return this.status;
	}
	public void setStatus(final String status) {
		this.status = status;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getReason() {
		return this.reason;
	}
	public void setReason(final String reason) {
		this.reason = reason;
	}

	@ElementCollection
	@EachSafeHtml(whitelistType = WhiteListType.NONE)
	public List<Integer> getLocation() {
		return this.location;
	}

	public void setLocation(final List<Integer> location) {
		this.location = location;
	}


	// Relationships

	private Member	member;
	private Parade	parade;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Parade getParade() {
		return this.parade;
	}

	public void setParade(final Parade parade) {
		this.parade = parade;
	}

}
