package com.faizal.springrecipe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.faizal.springrecipe.commands.IngredientCommand;
import com.faizal.springrecipe.commands.RecipeCommand;
import com.faizal.springrecipe.commands.UnitOfMeasureCommand;
import com.faizal.springrecipe.services.IngredientService;
import com.faizal.springrecipe.services.RecipeService;
import com.faizal.springrecipe.services.UnitOfMeasureService;

@Controller
public class IngredientController {

	RecipeService recipeService;

	IngredientService ingredientService;

	UnitOfMeasureService unitOfMeasureService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@RequestMapping("/recipe/{id}/ingredients")
	public String listIngredient(@PathVariable String id, Model model) {

		RecipeCommand recipeCommand = recipeService.findCommandById(id);
		model.addAttribute("recipe", recipeCommand);

		return "recipe/ingredient/list";
	}

	@RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
	public String showIngredient(@PathVariable("recipeId") String recipeId,
			@PathVariable("ingredientId") String ingredientId, Model model) {

		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);
		model.addAttribute("ingredient", ingredientCommand);

		return "recipe/ingredient/show";
	}

	@GetMapping
	@RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
	public String updateRecipeIngredient(@PathVariable("recipeId") String recipeId,
			@PathVariable("ingredientId") String ingredientId, Model model) {

		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

		model.addAttribute("uomList", unitOfMeasureService.findAll());

		return "recipe/ingredient/ingredientform";
	}

	@PostMapping
	@RequestMapping("/recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand) {
		IngredientCommand savedCommand = ingredientService.save(ingredientCommand);

		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}

	@GetMapping
	@RequestMapping("/recipe/{id}/ingredient/new")
	public String addNewRecipeIngredient(@PathVariable("id") String recipeId, Model model) {

		RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);

		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(recipeId);
		ingredientCommand.setUom(new UnitOfMeasureCommand());

		model.addAttribute("ingredient", ingredientCommand);

		model.addAttribute("uomList", unitOfMeasureService.findAll());

		return "recipe/ingredient/ingredientform";
	}

	@GetMapping
	@RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
	public String deleteRecipeIngredient(@PathVariable("recipeId") String recipeId,
			@PathVariable("ingredientId") String ingredientId) {

		ingredientService.deleteById(recipeId, ingredientId);

		return "redirect:/recipe/" + Long.valueOf(recipeId) + "/ingredients";
	}

}
