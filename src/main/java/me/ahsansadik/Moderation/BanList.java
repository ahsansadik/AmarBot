package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Guild.Ban;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;


import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BanList extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().updateCommands()
                .addCommands(
                        Commands.slash("banlist", "Shows a list of banned users")
                )
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("banlist")) return;

        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("This command can only be used in a server!").setEphemeral(true).queue();
            return;
        }

        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("You do not have permission to view the ban list.").setEphemeral(true).queue();
            return;
        }

        // Fetch ban list asynchronously
        CompletableFuture<List<Ban>> banListFuture = guild.retrieveBanList().submit();

        banListFuture.thenAccept(bans -> {
            if (bans.isEmpty()) {
                event.reply("✅ There are no banned users in this server.").queue();
                return;
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("🚫 Banned Users")
                    .setColor(Color.RED);

            int count = 0;
            StringBuilder banListText = new StringBuilder();

            for (Ban ban : bans) {
                banListText.append("🔹 **").append(ban.getUser().getAsMention()).append("** (ID: ")
                        .append(ban.getUser().getId()).append(")\n");
                count++;

                // Discord message limit is 4096 characters, so we limit how many we show
                if (count >= 10) {
                    banListText.append("\n...and more!");
                    break;
                }
            }

            embed.setDescription(banListText.toString());
            event.replyEmbeds(embed.build()).queue();
        }).exceptionally(error -> {
            event.reply("❌ Failed to retrieve the ban list: " + error.getMessage()).setEphemeral(true).queue();
            return null;
        });
    }
}
