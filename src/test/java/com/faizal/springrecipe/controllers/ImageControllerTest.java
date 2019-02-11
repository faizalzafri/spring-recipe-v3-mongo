package com.faizal.springrecipe.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.faizal.springrecipe.commands.RecipeCommand;
import com.faizal.springrecipe.services.ImageService;
import com.faizal.springrecipe.services.RecipeService;

public class ImageControllerTest {

	@Mock
	private ImageService imageService;

	@Mock
	private RecipeService recipeService;

	ImageController imageController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		imageController = new ImageController(imageService, recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(imageController).setControllerAdvice(new ControllerExceptionHandler())
				.build();
	}

	@Test
	public void testHandleImagePost() throws Exception {

		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("1");

		when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);

		mockMvc.perform(get("/recipe/1/image")).andExpect(status().isOk()).andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyString());
	}

	@Test
	public void testFormGet() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "texting.txt", "text/plain",
				"testFile2".getBytes());
		mockMvc.perform(multipart("/recipe/1/image").file(multipartFile)).andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/recipe/1/show"));

		verify(imageService, times(1)).save(anyString(), any());
	}

	@Test
	public void testGetImageNumberFormat() throws Exception {

		when(recipeService.findCommandById(anyString())).thenThrow(NumberFormatException.class);

		mockMvc.perform(get("/recipe/1/recipeimage")).andExpect(status().isBadRequest())
				.andExpect(view().name("400error"));
	}

}
