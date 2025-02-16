package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Kick extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().updateCommands()
                .addCommands(
                        Commands.slash("kick", "Kick a user")
                                .addOptions(new OptionData(OptionType.USER, "kick", "kick a user", true))
                )
                .queue();
    }

        @Override
        public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
            if(event.getName().equals("kick")){
                Guild guild = event.getGuild();
                Member member = event.getMember();
                if(member.hasPermission(Permission.KICK_MEMBERS)){
                    Member kickMember = event.getOption("kick").getAsMember();
                    guild.kick(kickMember).queue();
                    event.reply("User "+ kickMember.getAsMention() + "kicked successfully").queue();
                }else{
                    event.reply("You do not have the permission to kick user").queue();
                }
            }
        }
    }
