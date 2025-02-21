package me.ahsansadik.Moderation.SlashCommands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DefaultRoleAssign extends ListenerAdapter {
    public Role defaultrole;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("default_role")) {
            defaultrole = event.getOption("role").getAsRole();
            if(defaultrole!= null){
                event.reply("Default role added successfully").queue();
            }else{
                event.reply("An unknown error occurred").queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (defaultrole != null) {
            event.getGuild().addRoleToMember(event.getUser(), defaultrole).queue();
        }
    }
}
