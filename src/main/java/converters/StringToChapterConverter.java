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

import repositories.ChapterRepository;
import domain.Chapter;

@Component
@Transactional
public class StringToChapterConverter implements Converter<String, Chapter> {

	@Autowired
	ChapterRepository	chapterRepository;


	@Override
	public Chapter convert(final String text) {
		Chapter result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.chapterRepository.findOne(id);
		} catch (final Exception oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
