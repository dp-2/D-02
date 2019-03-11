/*
 * StringToCurriculaConverter.java
 *
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Parade;
import repositories.ParadeRepository;

@Component
@Transactional
public class StringToParadeConverter implements Converter<String, Parade> {

	@Autowired
	ParadeRepository paradeRepository;


	@Override
	public Parade convert(final String text) {
		Parade result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.paradeRepository.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
