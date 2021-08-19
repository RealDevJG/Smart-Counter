package me.devjg.devjg_smartcounter;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class CmdCountTicks extends CommandBase {
    List<String> alias = new ArrayList<String>();

    CmdCountTicks() {
        this.alias.add("ct");
        this.alias.add("countticks");
        this.alias.add("counttick");
    }

    @Override
    public String getCommandName() {
        return "countticks";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/countticks";
    }

    @Override
    public List<String> getCommandAliases(){
        return this.alias;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        SmartCounter.isEnabled = !SmartCounter.isEnabled;
    }

    @Override
    public int getRequiredPermissionLevel(){
        return -1;
    }
}
