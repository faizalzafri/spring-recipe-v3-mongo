package com.faizal.springrecipe.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.faizal.springrecipe.domain.UnitOfMeasure;
import com.faizal.springrecipe.repositories.UnitOfMeasureRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureIT {

	@Autowired
	private UnitOfMeasureRepository repository;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	// @DirtiesContext
	public void findByDescription() throws Exception {
		Optional<UnitOfMeasure> opt = repository.findByDescription("Teaspoon");
		assertEquals("Teaspoon", opt.get().getDescription());
	}

	@Test
	public void findByDescriptionCup() throws Exception {
		Optional<UnitOfMeasure> opt = repository.findByDescription("Cup");
		assertEquals("Cup", opt.get().getDescription());
	}
}
