package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Kick extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
            if(event.getName().equals("kick")){
                Guild guild = event.getGuild();
                Member member = event.getMember();
                if(member.hasPermission(Permission.KICK_MEMBERS)){
                    Member kickMember = event.getOption("kick").getAsMember();
                    guild.kick(kickMember).queue();
                    event.reply("User "+ kickMember.getAsMention() + " kicked successfully").queue();
                }else{
                    event.reply("You do not have the permission to kick user").queue();
                }
            }
        }
    }
