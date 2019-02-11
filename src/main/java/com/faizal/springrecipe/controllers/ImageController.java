package com.faizal.springrecipe.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.faizal.springrecipe.commands.RecipeCommand;
import com.faizal.springrecipe.services.ImageService;
import com.faizal.springrecipe.services.RecipeService;

@Controller
public class ImageController {

	private ImageService imageService;
	private RecipeService recipeService;

	public ImageController(ImageService imageService, RecipeService recipeService) {
		super();
		this.imageService = imageService;
		this.recipeService = recipeService;
	}

	@PostMapping("recipe/{id}/image")
	public String handleImagePost(@PathVariable("id") String id, @RequestParam("imagefile") MultipartFile file) {

		imageService.save(id, file);

		return "redirect:/recipe/" + id + "/show";

	}

	@GetMapping("/recipe/{id}/image")
	public String showUploadForm(@PathVariable String id, Model model) {
		model.addAttribute("recipe", recipeService.findCommandById(id));
		return "recipe/imageuploadform";
	}

	@GetMapping("/recipe/{id}/recipeimage")
	public void renderImageFromDb(@PathVariable String id, HttpServletResponse response) throws IOException {

		RecipeCommand reCommand = recipeService.findCommandById(id);

		if (reCommand.getImage() != null) {
			byte[] byteArray = new byte[reCommand.getImage().length];
			int i = 0;

			for (Byte wrappedByte : reCommand.getImage()) {
				byteArray[i++] = wrappedByte; // auto unboxing
			}

			response.setContentType("image/jpeg");
			InputStream is = new ByteArrayInputStream(byteArray);
			IOUtils.copy(is, response.getOutputStream());
		}
	}
}
