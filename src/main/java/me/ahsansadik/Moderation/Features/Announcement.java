package me.ahsansadik.Moderation.Features;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Announcement extends ListenerAdapter {

    private static final long ANNOUNCEMENT_CHANNEL_ID = 1342371751912275978L; // Update with your channel ID

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("announcement")) {

            TextInput title = TextInput.create("title-field", "Title", TextInputStyle.SHORT)
                    .setRequired(true)
                    .setMinLength(1)
                    .setMaxLength(100)
                    .build();

            TextInput subhead = TextInput.create("subhead-field", "Subhead", TextInputStyle.SHORT) // FIXED ID
                    .setRequired(true)
                    .setMinLength(1)
                    .build();

            TextInput body = TextInput.create("main-body", "Main Body", TextInputStyle.PARAGRAPH)
                    .setRequired(true)
                    .setMinLength(1)
                    .build();

            Modal modal = Modal.create("announcement_modal", "Create Announcement")
                    .addActionRow(title)
                    .addActionRow(subhead)
                    .addActionRow(body)
                    .build();
            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("announcement_modal")) {
            // ✅ Fixed: Using correct IDs
            ModalMapping titleMapping = event.getValue("title-field");
            ModalMapping subheadMapping = event.getValue("subhead-field");
            ModalMapping bodyMapping = event.getValue("main-body");

            String title = (titleMapping != null) ? titleMapping.getAsString() : "Untitled";
            String subhead = (subheadMapping != null) ? subheadMapping.getAsString() : "";
            String body = (bodyMapping != null) ? bodyMapping.getAsString() : "No content provided.";

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("📢 " + title)
                    .setColor(Color.BLUE)
                    .setDescription(body)
                    .setFooter("Posted by " + event.getUser().getName(), event.getUser().getAvatarUrl());

            if (!subhead.isEmpty()) {
                embed.addField("Subhead", subhead, false);
            }

            event.getJDA().getTextChannelById(ANNOUNCEMENT_CHANNEL_ID)
                    .sendMessageEmbeds(embed.build())
                    .queue(
                            success -> event.reply("✅ Announcement posted successfully!").setEphemeral(true).queue(),
                            failure -> event.reply("❌ Failed to post the announcement. Please check permissions.").setEphemeral(true).queue()
                    );
        }
    }
}
