package com.github.bluemonster122.init;

import java.util.ArrayList;
import java.util.List;

import com.github.bluemonster122.utils.IRegister;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes implements IRegister {

	List<IRecipe> RECIPES = new ArrayList<>();

	@Override
	public void register() {
		registerRecipes();
	}

	public void registerRecipes() {
		RECIPES.parallelStream().forEach(recipe -> GameRegistry.addRecipe(recipe));
	}

}
