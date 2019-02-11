package com.faizal.springrecipe.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.repositories.RecipeRepository;

public class ImageServiceImplTest {

	@Mock
	RecipeRepository recipeRepository;

	ImageService imageService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		imageService = new ImageServiceImpl(recipeRepository);

	}

	@Test
	public void testHandleImagePost() throws Exception {
		String id = "1";
		MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "texting.txt", "text/plain",
				"testFile2".getBytes());
		Recipe recipe = new Recipe();
		recipe.setId(id);

		Optional<Recipe> optRec = Optional.of(recipe);

		when(recipeRepository.findById(anyString())).thenReturn(optRec);

		ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

		imageService.save(id, multipartFile);

		verify(recipeRepository).save(argumentCaptor.capture());
		Recipe savedRecipe = argumentCaptor.getValue();
		assertEquals(id, savedRecipe.getId());
		assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
	}

}
