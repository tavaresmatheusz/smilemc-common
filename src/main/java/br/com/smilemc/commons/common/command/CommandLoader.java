package br.com.smilemc.commons.common.command;

import java.io.File;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.util.ClassGetter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/*
 * https://github.com/mcardy/CommandFramework
 * Took from https://github.com/Battlebits
 * 
 */

@RequiredArgsConstructor
public class CommandLoader {

	@NonNull
	private CommandFramework framework;

	public int loadCommandsFromPackage(String packageName) {
		int i = 0;
		for (Class<?> commandClass : ClassGetter.getClassesForPackage(framework.getJarClass(), packageName)) {
			if (CommandClass.class.isAssignableFrom(commandClass)) {
				try {
					CommandClass commands = (CommandClass) commandClass.newInstance();
					framework.registerCommands(commands);
				} catch (Exception e) {
					e.printStackTrace();
					Common.log("Erro ao carregar comandos da classe " + commandClass.getSimpleName() + "!");
				}
				i++;
			}
		}
		return i;
	}

	public int loadCommandsFromPackage(File jarFile, String packageName) {
		int i = 0;

		for (Class<?> commandClass : ClassGetter.getClassesForPackageByFile(jarFile, packageName)) {
			if (CommandClass.class.isAssignableFrom(commandClass)) {
				try {
					CommandClass commands = (CommandClass) commandClass.newInstance();
					framework.registerCommands(commands);
				} catch (Exception e) {
					e.printStackTrace();
					Common.log("Erro ao carregar comandos da classe " + commandClass.getSimpleName() + "!");
				}
				i++;
			}
		}

		return i;
	}
}
