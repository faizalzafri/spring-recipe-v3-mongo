package com.faizal.springrecipe.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class NotesTest {

	private Notes notes;

	@Before
	public void setUp() {
		notes = new Notes();
	}

	@Test
	public void getId() throws Exception {
		String idValue = "4";

		notes.setId(idValue);

		assertEquals(idValue, notes.getId());
	}
}
