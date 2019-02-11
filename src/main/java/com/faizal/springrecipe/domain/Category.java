package com.faizal.springrecipe.domain;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {

	private String id;
	private String description;
	private Set<Recipe> recipes;

}
