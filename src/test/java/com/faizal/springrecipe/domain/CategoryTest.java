package com.faizal.springrecipe.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {

	private Category category;

	@Before
	public void setUp() {
		category = new Category();
	}

	@Test
	public void getId() throws Exception {
		String idValue = "4";

		category.setId(idValue);

		assertEquals(idValue, category.getId());
	}
}
