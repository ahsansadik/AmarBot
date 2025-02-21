package me.ahsansadik.Moderation.Features;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LogChannel extends ListenerAdapter {
    private final Map<Long, Long> logChannelMap = new HashMap<>();
    private final Map<Long, String> messageHistory = new HashMap<>(); // Stores message ID and content

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("set_log_channel")) {
            TextChannel channel = event.getGuild().getTextChannelById(event.getChannel().getIdLong());
            if (channel != null) {
                logChannelMap.put(event.getGuild().getIdLong(), channel.getIdLong());
                event.reply("Log channel set successfully to " + channel.getAsMention()).queue();
            } else {
                event.reply("An unknown error occurred").queue();
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            TextChannel logChannel = getLogChannel(event);
            if (logChannel != null) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("📩 Message Sent")
                        .setColor(Color.GREEN)
                        .addField("User", event.getAuthor().getName(), false)
                        .addField("Channel", event.getChannel().getName(), false)
                        .addField("Content", event.getMessage().getContentRaw(), false);
                logChannel.sendMessageEmbeds(embed.build()).queue();
            }

            // Store message content in history
            messageHistory.put(event.getMessageIdLong(), event.getMessage().getContentRaw());
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        TextChannel logChannel = getLogChannel(event);
        if (logChannel != null) {
            String previousContent = messageHistory.getOrDefault(event.getMessageIdLong(), "Unknown (Message might not be logged before)");
            String newContent = event.getMessage().getContentRaw();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("✏️ Message Edited")
                    .setColor(Color.ORANGE)
                    .addField("User", event.getAuthor().getName(), false)
                    .addField("Channel", event.getChannel().getName(), false)
                    .addField("Previous Content", previousContent, false)
                    .addField("New Content", newContent, false);
            logChannel.sendMessageEmbeds(embed.build()).queue();

            // Update message history with the new content
            messageHistory.put(event.getMessageIdLong(), newContent);
        }
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        TextChannel logChannel = getLogChannel(event);
        if (logChannel != null) {
            String deletedContent = messageHistory.getOrDefault(event.getMessageIdLong(), "Unknown (Message might not be logged before)");

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("🗑️ Message Deleted")
                    .setColor(Color.RED)
                    .addField("Channel", event.getChannel().getName(), false)
                    .addField("Deleted Content", deletedContent, false);
            logChannel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    private TextChannel getLogChannel(MessageReceivedEvent event) {
        Long channelId = logChannelMap.get(event.getGuild().getIdLong());
        return (channelId != null) ? event.getGuild().getTextChannelById(channelId) : null;
    }

    private TextChannel getLogChannel(MessageUpdateEvent event) {
        Long channelId = logChannelMap.get(event.getGuild().getIdLong());
        return (channelId != null) ? event.getGuild().getTextChannelById(channelId) : null;
    }

    private TextChannel getLogChannel(MessageDeleteEvent event) {
        Long channelId = logChannelMap.get(event.getGuild().getIdLong());
        return (channelId != null) ? event.getGuild().getTextChannelById(channelId) : null;
    }
}
