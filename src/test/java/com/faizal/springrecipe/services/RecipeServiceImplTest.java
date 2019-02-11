package com.faizal.springrecipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.faizal.springrecipe.converters.RecipeCommandToRecipe;
import com.faizal.springrecipe.converters.RecipeToRecipeCommand;
import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.exceptions.NotFoundException;
import com.faizal.springrecipe.repositories.RecipeRepository;

public class RecipeServiceImplTest {

	@Autowired
	private RecipeCommandToRecipe recipeCommandToRecipe;

	@Autowired
	private RecipeToRecipeCommand recipeToRecipeCommand;

	@Mock
	private RecipeRepository recipeRepository;

	private RecipeServiceImpl recipeServiceImpl;

	private Set<Recipe> recipeSet = new HashSet<>();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		recipeServiceImpl = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
	}

	@After
	public void tearDown() {
		recipeSet.clear();
	}

	@Test
	public void testFindAll() throws Exception {

		Recipe recipe = new Recipe();
		recipe.setId("1");
		recipeSet.add(recipe);

		when(recipeRepository.findAll()).thenReturn(recipeSet);

		assertEquals(recipeServiceImpl.findAll().size(), 1);
		verify(recipeRepository, times(1)).findAll();
	}

	@Test
	public void testFindById() throws Exception {

		Recipe recipe = new Recipe();
		recipe.setId("1");
		recipeSet.add(recipe);

		when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

		assertNotNull(recipeServiceImpl.findById("1"));
		verify(recipeRepository, times(1)).findById(anyString());
	}

	@Test
	public void testDeleteById() throws Exception {

		String id = "1";

		recipeRepository.deleteById(id);

		verify(recipeRepository, times(1)).deleteById(anyString());
	}

	@Test(expected = NotFoundException.class)
	public void testFindByIdFail() throws Exception {

		Optional<Recipe> recipe = Optional.empty();
		when(recipeRepository.findById(anyString())).thenReturn(recipe);

		Recipe returnedRecipe = recipeServiceImpl.findById("1");

		assertNull(returnedRecipe);
	}

}
