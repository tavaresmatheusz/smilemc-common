package br.com.smilemc.commons.bukkit.inventory.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.smilemc.commons.bukkit.api.item.ItemBuilder;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.report.Report;
import lombok.Getter;

@Getter
public class ReportGUI {
	Inventory inventory;

	public ReportGUI(int page) {
		inventory = Bukkit.createInventory(null, 5 * 9, "§7Reports");
		
		int inList = 0;
		
		if (page == 1) {
			int max = (4 * 9) - 1;
			for (Report report : Common.getReportManager().getReportList()) {
				if (inList >= max)
					break;
				Common.debug("reports: " + inList + " - o que ele pensa: " +  ((4 * 9) - 1) * (page - 1)); 
				ItemStack item = new ItemBuilder().type(Material.SKULL_ITEM).durability(3).skin(report.getTarget())
						.name("§e" + report.getTarget())
						.lore("§fDetalhes do report:", "", "§fMotivo: §e" + report.getReason(),
								"§fReportado por: §e" + report.getReportedBy(), "§fReportado em: " + report.getTime(),
								"", "§eClique com o botão DIREITO para §cexcluir§e este report!",
								"§eClique com o botão ESQUERDO para §air§e até este jogador!")
						.build();
				inventory.addItem( item);
				inList++;
			}
			if (inList >= max)
				inventory.setItem(44, new ItemBuilder().type(Material.ARROW).name("§aPágina 2").build());
		} else {

			for (int min = ((4 * 9) - 1) * (page - 1), max = (4 * 9) - 1; min < max; min++) {
				Report report = Common.getReportManager().getReportList().get(min);
				if (report == null)
					break;
				ItemStack item = new ItemBuilder().type(Material.SKULL).durability(3).skin(report.getTarget())
						.name("§e" + report.getTarget())
						.lore("§fDetalhes do report:", "", "§fMotivo: §e" + report.getReason(),
								"§fReportado por: §e" + report.getReportedBy(), "§fReportado em: " + report.getTime(),
								"", "§eClique com o botão DIREITO para §cexcluir§e este report!",
								"§eClique com o botão ESQUERDO para §air§e até este jogador!")
						.build();
				inventory.addItem(item);
				inList++;
			}
			inventory.setItem(43, new ItemBuilder().type(Material.ARROW).name("§aPágina " + (page - 1)).build());
			if (inList >= (4 * 9) - 1) {
				inventory.setItem(44, new ItemBuilder().type(Material.ARROW).name("§aPágina " + (page + 1)).build());
			}
		}
		
	}

}
