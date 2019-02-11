package com.faizal.springrecipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.faizal.springrecipe.commands.IngredientCommand;
import com.faizal.springrecipe.commands.RecipeCommand;
import com.faizal.springrecipe.services.IngredientService;
import com.faizal.springrecipe.services.RecipeService;
import com.faizal.springrecipe.services.UnitOfMeasureService;

public class IngredientControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	IngredientService ingredientService;

	@Mock
	UnitOfMeasureService unitOfMeasureService;

	IngredientController controller;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testListIngredient() throws Exception {
		RecipeCommand recipeCommand = new RecipeCommand();

		when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);

		mockMvc.perform(get("/recipe/1/ingredients")).andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/list")).andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testShowIngredient() throws Exception {

		IngredientCommand ingredientCommand = new IngredientCommand();

		when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
		mockMvc.perform(get("/recipe/1/ingredient/1/show")).andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/show")).andExpect(model().attributeExists("ingredient"));
	}

	@Test
	public void testSaveOrUpdate() throws Exception {

		IngredientCommand iCommand = new IngredientCommand();
		iCommand.setId("2");
		iCommand.setRecipeId("1");

		when(ingredientService.save(any())).thenReturn(iCommand);

		mockMvc.perform(post("/recipe/1/ingredient").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "")
				.param("description", "Some Description")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/1/ingredient/2/show"));
	}

	@Test
	public void testUpdateRecipeIngredient() throws Exception {

		when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString()))
				.thenReturn(new IngredientCommand());

		when(unitOfMeasureService.findAll()).thenReturn(new HashSet<>());

		mockMvc.perform(get("/recipe/1/ingredient/2/update")).andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/ingredientform"))
				.andExpect(model().attributeExists("uomList")).andExpect(model().attributeExists("ingredient"));
	}

	@Test
	public void testAddNewRecipeIngredient() throws Exception {

		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("1");

		when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);

		when(unitOfMeasureService.findAll()).thenReturn(new HashSet<>());

		mockMvc.perform(get("/recipe/1/ingredient/new")).andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/ingredientform"))
				.andExpect(model().attributeExists("uomList")).andExpect(model().attributeExists("ingredient"));

		verify(recipeService, times(1)).findCommandById(anyString());
	}

	@Test
	public void testDeleteRecipeIngredient() throws Exception {
		mockMvc.perform(get("/recipe/1/ingredient/1/delete")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/1/ingredients"));

		verify(ingredientService).deleteById(anyString(), anyString());

	}
}
