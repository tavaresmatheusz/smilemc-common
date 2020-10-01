package br.com.smilemc.commons.bukkit.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import br.com.smilemc.commons.bukkit.api.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItensCache {

	ADMIN_MODE(new ItemStack[] { new ItemBuilder().type(Material.CHEST).name("§aReports").build() },
			new ItemStack[] { null }, new Integer[] { 4 });

	private ItemStack[] itens, armors;
	private Integer[] slots;
	
	
}
