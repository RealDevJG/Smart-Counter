package me.devjg.devjg_smartcounter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class CmdCountTicks extends CommandBase {
  List<String> aliases = new ArrayList<String>();

  CmdCountTicks() {
    this.aliases.add("ct");
    this.aliases.add("smartcounter");
  }

  @Override
  public String getCommandName() {
    return "tickcounter";
  }

  @Override
  public String getCommandUsage(ICommandSender sender) {
    return "/tickcounter";
  }

  @Override
  public List<String> getCommandAliases() {
    return this.aliases;
  }

  @Override
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    SmartCounter.isEnabled = !SmartCounter.isEnabled;
  }

  @Override
  public int getRequiredPermissionLevel() {
    return -1;
  }
}
