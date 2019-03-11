
package forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.DFloat;
import domain.DomainEntity;
import domain.Parade;

public class ParadeFloatForm extends DomainEntity {

	private Parade	parade;
	private DFloat		dFloat;


	@NotNull
	@Valid
	public Parade getParade() {
		return this.parade;
	}
	public void setParade(final Parade parade) {
		this.parade = parade;
	}

	@NotNull
	@Valid
	public DFloat getDFloat() {
		return this.dFloat;
	}
	public void setDFloat(final DFloat dFloat) {
		this.dFloat = dFloat;
	}

}
