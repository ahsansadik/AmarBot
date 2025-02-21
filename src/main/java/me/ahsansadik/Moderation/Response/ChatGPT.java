package me.ahsansadik.Moderation.Response;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChatGPT extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("chatgpt")) {
            event.deferReply().queue(); // Defer response to prevent timeouts

            String prompt = event.getOption("prompt").getAsString();
            String response = OpenAIChat.getChatGPTResponse(prompt);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("ChatGPT Response");
            embed.setDescription(response.length() > 4096 ? response.substring(0, 4093) + "..." : response);
            embed.setColor(0x1DA1F2); // Set embed color

            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}

