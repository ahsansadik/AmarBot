package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;

public class Unban extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().updateCommands()
                .addCommands(
                        Commands.slash("unban", "Unban a user")
                                .addOptions(new OptionData(OptionType.STRING, "username", "Name of the user to unban", true))
                )
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("unban")) {
            Guild guild = event.getGuild();
            Member member = event.getMember();

            if (guild == null || member == null) {
                event.reply("An error occurred. Please try again.").setEphemeral(true).queue();
                return;
            }

            if (member.hasPermission(Permission.BAN_MEMBERS)) {
                String username = event.getOption("username").getAsString().toLowerCase(); // Get username input

                guild.retrieveBanList().queue(bans -> {
                    for (Guild.Ban ban : bans) {
                        User bannedUser = ban.getUser();
                        if (bannedUser.getName().equalsIgnoreCase(username)) { // Match username
                            guild.unban(bannedUser).queue();
                            Role role = guild.getRoleById(1335887144060715039L);
                            assert role != null;
                            guild.addRoleToMember(bannedUser, role).queue();

                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setTitle("User Unbanned");
                            embed.setDescription("**User:** " + bannedUser.getAsMention() + " has been unbanned.");
                            embed.setColor(Color.GREEN);
                            event.replyEmbeds(embed.build()).queue();
                            return;
                        }
                    }
                    event.reply("No banned user found with that name.").setEphemeral(true).queue();
                });
            } else {
                event.reply("You do not have permission to unban members.").setEphemeral(true).queue();
            }
        }
    }
}



