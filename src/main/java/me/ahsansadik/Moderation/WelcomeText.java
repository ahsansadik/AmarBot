package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WelcomeText extends ListenerAdapter {
    private final Map<Long, Long> welcomeChannelMap = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("set_welcome_channel")) {
            TextChannel channel = event.getGuild().getTextChannelById(event.getChannel().getIdLong());
            if (channel != null) {
                welcomeChannelMap.put(event.getGuild().getIdLong(), channel.getIdLong());
                event.reply("Default welcome channel set successfully to " + channel.getAsMention()).queue();
            } else {
                event.reply("An unknown error occurred").queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Long channelId = welcomeChannelMap.get(event.getGuild().getIdLong());
        TextChannel channel = (channelId != null) ? event.getGuild().getTextChannelById(channelId) : event.getGuild().getDefaultChannel().asTextChannel();
        if (channel != null) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Welcome!");
            embed.setDescription("Welcome " + event.getUser().getAsMention() + " to the server! 🎉");
            embed.setColor(Color.CYAN);
            embed.setThumbnail(event.getUser().getEffectiveAvatarUrl());

            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }
}

