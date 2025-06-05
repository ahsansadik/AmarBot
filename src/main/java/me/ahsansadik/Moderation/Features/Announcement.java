package me.ahsansadik.Moderation.Features;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Announcement extends ListenerAdapter {

    private final Map<Long, Long> announcementChannelMap = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("set_announcement_channel")) {
            TextChannel channel = event.getChannel().asTextChannel();
            if (channel != null) {
                announcementChannelMap.put(event.getGuild().getIdLong(), channel.getIdLong());
                event.reply("Announcement channel set successfully to " + channel.getAsMention()).queue();
            } else {
                event.reply("An unknown error occurred").queue();
            }
        } else if (event.getName().equals("announcement")) {
            if (!announcementChannelMap.containsKey(event.getGuild().getIdLong())) {
                event.reply("❌ No announcement channel has been set. Please use `/set_announcement_channel` first.").setEphemeral(true).queue();
                return;

            }

            TextInput title = TextInput.create("title-field", "Title", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build();

            TextInput subhead = TextInput.create("subhead-field", "Subhead", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setMinLength(1)
                    .build();

            TextInput body = TextInput.create("main-body", "Main Body", TextInputStyle.PARAGRAPH)
                    .setRequired(true)
                    .setMinLength(1)
                    .build();

            TextInput imageUrl = TextInput.create("image-url", "Image URL (Optional)", TextInputStyle.SHORT)
                    .setRequired(false)
                    .build();

            Modal modal = Modal.create("announcement_modal", "Create Announcement")
                    .addActionRow(title)
                    .addActionRow(subhead)
                    .addActionRow(body)
                    .addActionRow(imageUrl)
                    .build();

            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("announcement_modal")) {
            Long channelId = announcementChannelMap.get(event.getGuild().getIdLong());
            if (channelId == null) {
                event.reply("❌ No announcement channel has been set. Please use `/set_announcement_channel` first.").setEphemeral(true).queue();
                return;
            }

            TextChannel channel = event.getJDA().getTextChannelById(channelId);
            if (channel == null) {
                event.reply("❌ The set announcement channel is no longer available. Please set a new one.").setEphemeral(true).queue();
                return;
            }

            ModalMapping titleMapping = event.getValue("title-field");
            ModalMapping subheadMapping = event.getValue("subhead-field");
            ModalMapping bodyMapping = event.getValue("main-body");

            ModalMapping imageUrlMapping = event.getValue("image-url");
            String imageUrl = (imageUrlMapping != null) ? imageUrlMapping.getAsString().trim() : "";


            String title = (titleMapping != null) ? titleMapping.getAsString() : "Untitled";
            String subhead = (subheadMapping != null) ? subheadMapping.getAsString() : "";
            String body = (bodyMapping != null) ? bodyMapping.getAsString() : "No content provided.";

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("📢 " + title)
                    .setColor(Color.BLUE)
                    .setDescription(body)
                    .setFooter("Posted by " + event.getUser().getName(), event.getUser().getAvatarUrl());

            if (!subhead.isEmpty()) {
                embed.setDescription("**" + subhead + "**\n\n" + body);
            }

            if (!imageUrl.isEmpty()) {
                embed.setImage(imageUrl);
            }

            channel.sendMessageEmbeds(embed.build())
                    .queue(
                            success -> event.reply("✅ Announcement posted successfully!").setEphemeral(true).queue(),
                            failure -> event.reply("❌ Failed to post the announcement. Please check permissions.").setEphemeral(true).queue()
                    );
        }
    }
}
