package com.faizal.springrecipe.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.services.RecipeService;

public class IndexControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	Model model;

	IndexController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		controller = new IndexController(recipeService);

	}

	@Test
	public void testMockMVC() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@Test
	public void getIndexPage() throws Exception {

		ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

		// Creating a Set of Recipe to be returned by call to the controller - START
		Set<Recipe> recipeSet = new HashSet<>();
		recipeSet.add(new Recipe());

		Recipe newRecipe = new Recipe();
		newRecipe.setId("1");

		recipeSet.add(newRecipe);
		// Creating a Set of Recipe to be returned by call to the controller - END

		// set previously created Set to be returned
		when(recipeService.findAll()).thenReturn(recipeSet);

		// when
		String viewName = controller.getIndexPage(model);
		// then - START
		assertEquals("index", viewName);
		verify(recipeService, times(1)).findAll();
		verify(model, times(1)).addAttribute("recipes", recipeSet);
		// then - END

		verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());

		Set<Recipe> setController = argumentCaptor.getValue();
		assertEquals(2, setController.size());

	}
}
