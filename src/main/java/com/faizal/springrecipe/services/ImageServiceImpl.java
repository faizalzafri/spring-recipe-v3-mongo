package com.faizal.springrecipe.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.faizal.springrecipe.domain.Recipe;
import com.faizal.springrecipe.repositories.RecipeRepository;

@Service
public class ImageServiceImpl implements ImageService {

	private RecipeRepository recipeRepository;

	public ImageServiceImpl(RecipeRepository recipeRepository) {
		// TODO Auto-generated constructor stub
		this.recipeRepository = recipeRepository;
	}

	@Override
	@Transactional
	public void save(String recipeId, MultipartFile file) {

		try {
			Recipe recipe = recipeRepository.findById(recipeId).get();

			Byte[] byteObjects = new Byte[file.getBytes().length];

			int i = 0;

			for (byte b : file.getBytes()) {
				byteObjects[i++] = b;
			}

			recipe.setImage(byteObjects);

			recipeRepository.save(recipe);
		} catch (IOException e) {
			// todo handle better
			System.out.println(e.getMessage());
		}
	}

}
