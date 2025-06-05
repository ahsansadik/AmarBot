package me.ahsansadik.Moderation.Features;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteLogger extends ListenerAdapter {

    private final Map<String, Integer> inviteTracker = new HashMap<>();
    private TextChannel logChannel;

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (logChannel == null) {
            logChannel = guild.getDefaultChannel() instanceof TextChannel ? (TextChannel) guild.getDefaultChannel() : null;
        }

        if (logChannel == null) return;

        guild.retrieveInvites().queue(invites -> {
            for (Invite invite : invites) {
                int previousUses = inviteTracker.getOrDefault(invite.getCode(), 0);
                if (invite.getUses() > previousUses) {
                    inviteTracker.put(invite.getCode(), invite.getUses());
                    User inviter = invite.getInviter();

                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("New Member Joined!")
                            .setColor(Color.GREEN)
                            .setThumbnail(member.getEffectiveAvatarUrl())
                            .addField("Member:", member.getAsMention(), false)
                            .addField("Invited By:", inviter != null ? inviter.getAsMention() : "Unknown", false)
                            .addField("Invite Code:", invite.getCode(), false)
                            .addField("Total Uses:", String.valueOf(invite.getUses()), false)
                            .setFooter("Invite Tracker", guild.getIconUrl());

                    if (inviter != null) {
                        embed.setAuthor(inviter.getName(), null, inviter.getEffectiveAvatarUrl());
                    }

                    logChannel.sendMessageEmbeds(embed.build()).queue();
                    return;
                }
            }
        });
    }

    public void updateInvites(Guild guild) {
        RestAction<List<Invite>> action = guild.retrieveInvites();
        action.queue(invites -> {
            for (Invite invite : invites) {
                inviteTracker.put(invite.getCode(), invite.getUses());
            }
        });
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("set_invite_log_channel")) {
            TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();
            logChannel = channel;
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle("Invite Log Channel Updated")
                    .setDescription("Invite logs will now be sent to " + channel.getAsMention())
                    .setColor(Color.BLUE)
                    .build()).queue();
        }
    }
}
