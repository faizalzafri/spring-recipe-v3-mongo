package com.faizal.springrecipe.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IngredientTest {

	private Ingredient ingredient;

	@Before
	public void setUp() {
		ingredient = new Ingredient();
	}

	@Test
	public void getId() throws Exception {
		String idValue = "4";

		ingredient.setId(idValue);

		assertEquals(idValue, ingredient.getId());
	}
}
