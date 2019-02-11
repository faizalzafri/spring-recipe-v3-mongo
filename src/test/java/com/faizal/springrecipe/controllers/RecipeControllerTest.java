package com.faizal.springrecipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.faizal.springrecipe.commands.RecipeCommand;
import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.exceptions.NotFoundException;
import com.faizal.springrecipe.services.RecipeService;

public class RecipeControllerTest {

	@Mock
	private RecipeService recipeService;

	private RecipeController controller;

	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		controller = new RecipeController(recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ControllerExceptionHandler())
				.build();
	}

	@Test
	public void testShowRecipe() throws Exception {

		Recipe recipe = new Recipe();
		recipe.setId("1");

		when(recipeService.findById(anyString())).thenReturn(recipe);

		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isOk()).andExpect(view().name("recipe/show"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testAddRecipeForm() throws Exception {

		mockMvc.perform(get("/recipe/new")).andExpect(status().isOk()).andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testUpdateRecipeForm() throws Exception {

		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("2");

		when(recipeService.findCommandById(any())).thenReturn(recipeCommand);

		mockMvc.perform(get("/recipe/2/update")).andExpect(status().isOk()).andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testDelete() throws Exception {

		mockMvc.perform(get("/recipe/2/delete")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}

	@Test
	public void testSaveOrUpdateRecipe() throws Exception {

		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("1");

		when(recipeService.save(any())).thenReturn(recipeCommand);

		mockMvc.perform(post("/recipe/save").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "1")
				.param("description", "SomeDescription").param("directions", "someDirections"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/1/show"));
	}

	@Test
	public void testSaveOrUpdateRecipeValidationFail() throws Exception {

		RecipeCommand command = new RecipeCommand();
		command.setId("2");

		when(recipeService.save(any())).thenReturn(command);

		mockMvc.perform(post("/recipe/save").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "")
				.param("cookTime", "3000")).andExpect(status().isOk()).andExpect(model().attributeExists("recipe"))
				.andExpect(view().name("recipe/recipeform"));
	}

	@Test
	public void testShowRecipeNotFound() throws Exception {

		when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isNotFound()).andExpect(view().name("404error"));
	}

	@Test
	public void testShowRecipeNumberFormat() throws Exception {

		when(recipeService.findById(anyString())).thenThrow(NumberFormatException.class);

		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isBadRequest()).andExpect(view().name("400error"));
	}

}
