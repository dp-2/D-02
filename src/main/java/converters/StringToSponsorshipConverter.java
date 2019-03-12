
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import services.SponsorshipService;
import domain.Sponsorship;

@Component
@Transactional
public class StringToSponsorshipConverter implements Converter<String, Sponsorship> {

	@Autowired
	SponsorshipService	sponsorshipService;


	@Override
	public Sponsorship convert(final String text) {
		Sponsorship result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.sponsorshipService.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
