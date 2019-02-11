package com.faizal.springrecipe.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.faizal.springrecipe.commands.RecipeCommand;
import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.exceptions.NotFoundException;
import com.faizal.springrecipe.services.RecipeService;

@Controller
public class RecipeController {

	private RecipeService recipeService;

	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping(value = "/recipe/{id}/show")
	public String showRecipe(@PathVariable("id") String id, Model model) {

		Recipe recipe = recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		return "recipe/show";

	}

	@GetMapping(value = "/recipe/new")
	public String addRecipeForm(Model model) {

		model.addAttribute("recipe", new RecipeCommand());
		return "recipe/recipeform";

	}

	@GetMapping(value = "/recipe/{id}/update")
	public String updateRecipeForm(@PathVariable("id") String id, Model model) {

		model.addAttribute("recipe", recipeService.findCommandById(id));
		return "recipe/recipeform";

	}

	@GetMapping(value = "/recipe/{id}/delete")
	public String deleteRecipe(@PathVariable("id") String id) {

		recipeService.deleteById(id);
		return "redirect:/";

	}

	@PostMapping("/recipe/save")
	public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			bindingResult.getAllErrors().forEach(objectError -> {
				System.err.println(objectError.toString());
			});

			return "recipe/recipeform";
		}

		RecipeCommand savedCommand = recipeService.save(command);

		return "redirect:/recipe/" + savedCommand.getId() + "/show";
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(Exception exception) {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("404error");
		modelAndView.addObject("exception", exception.getMessage());

		return modelAndView;
	}

}
