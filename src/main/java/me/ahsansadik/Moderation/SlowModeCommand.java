package me.ahsansadik.Moderation;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import net.dv8tion.jda.api.interactions.commands.build.Commands;


public class SlowModeCommand extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().updateCommands()
                .addCommands(
                        Commands.slash("slow_mode", "Sets slow mode for this channel")
                                .addOptions(new OptionData(OptionType.INTEGER, "duration", "Slow mode duration in seconds (0-21600)", true))
                )
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("slow_mode")) return;

        int duration = event.getOption("duration").getAsInt();
        TextChannel channel = event.getChannel().asTextChannel();

        if (duration < 0 || duration > 21600) {
            event.replyEmbeds(new EmbedBuilder()
                    .setColor(0xFF0000)
                    .setTitle("❌ Invalid Duration")
                    .setDescription("Slow mode duration must be between **0 and 21600 seconds** (6 hours).")
                    .build()
            ).setEphemeral(true).queue();
            return;
        }

        channel.getManager().setSlowmode(duration).queue();

        event.replyEmbeds(new EmbedBuilder()
                .setColor(0x00FFFF)
                .setTitle("🕒 Slow Mode Updated")
                .setDescription("Slow mode has been set to **" + duration + "** seconds in this channel.")
                .build()
        ).queue();
    }
}
