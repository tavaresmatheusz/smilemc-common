package br.com.smilemc.commons.bukkit.inventory.cache;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
public class AdminMode {
	
	@Setter
	private ItemStack[] itensInInventory, armorContents;

}
