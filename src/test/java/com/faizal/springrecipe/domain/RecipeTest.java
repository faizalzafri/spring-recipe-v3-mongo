package com.faizal.springrecipe.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RecipeTest {

	private Recipe recipe;

	@Before
	public void setUp() {
		recipe = new Recipe();
	}

	@Test
	public void getId() throws Exception {
		String idValue = "4";

		recipe.setId(idValue);

		assertEquals(idValue, recipe.getId());
	}
}
