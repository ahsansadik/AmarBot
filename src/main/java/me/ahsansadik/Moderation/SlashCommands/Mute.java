package me.ahsansadik.Moderation.SlashCommands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Mute extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(event.getName().equals("mute")) {

            Member member = event.getMember();
            Guild guild = event.getGuild();

            Role role = guild.getRoleById(1335887144060715039L);

            if(member.getRoles().contains(role)){

                Member mutedMember = event.getOption("mute").getAsMember();

                Role mutedRole = guild.getRoleById(1335887992484794400L);
                Role defaultRole = guild.getRoleById(1335887144060715039L);

                guild.removeRoleFromMember(mutedMember,defaultRole).queue();
                guild.addRoleToMember(mutedMember,mutedRole).queue();

                event.reply("User" + mutedMember.getAsMention() + " muted").queue();
            }else {
                event.reply("You do not have the permission to mute user").queue();
            }
        }
    }
}