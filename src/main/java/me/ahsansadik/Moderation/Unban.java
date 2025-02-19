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
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("unban")) {
            Guild guild = event.getGuild();
            Member member = event.getMember();

            if (guild == null || member == null) {
                event.reply("An error occurred. Please try again.").setEphemeral(true).queue();
                return;
            }

            if (!member.hasPermission(Permission.BAN_MEMBERS)) {
                event.reply("You do not have permission to unban members.").setEphemeral(true).queue();
                return;
            }

            String username = event.getOption("username").getAsString().toLowerCase();

            // Defer reply to prevent timeout
            event.deferReply().queue();

            guild.retrieveBanList().queue(bans -> {
                for (Guild.Ban ban : bans) {
                    User bannedUser = ban.getUser();
                    if (bannedUser.getName().equalsIgnoreCase(username)) {

                        // Unban user and respond immediately
                        guild.unban(bannedUser).queue(success -> {
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle("User Unbanned")
                                    .setDescription("**User:** " + bannedUser.getAsMention() + " has been unbanned.")
                                    .setColor(Color.GREEN);

                            // Send the response before doing anything else
                            event.getHook().editOriginalEmbeds(embed.build()).queue();

                            // Optional: Add role back when the user rejoins
                            guild.findMembers(m -> m.getId().equals(bannedUser.getId())).onSuccess(members -> {
                                if (!members.isEmpty()) {
                                    Role role = guild.getRoleById(1335887144060715039L);
                                    if (role != null) {
                                        guild.addRoleToMember(members.get(0), role).queue();
                                    }
                                }
                            });

                        }, error -> event.getHook().editOriginal("Failed to unban the user: " + error.getMessage()).queue());

                        return;
                    }
                }

                // If no user was found in the ban list
                event.getHook().editOriginal("No banned user found with that name.").queue();
            });
        }
    }


}




