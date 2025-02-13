package me.ahsansadik;

import me.ahsansadik.Moderation.*;
import me.ahsansadik.Moderation.WelcomeText;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args) {
        JDABuilder.createDefault("TOKEN")
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DefaultRoleAssign())
                .addEventListeners(new WelcomeText())
                .addEventListeners(new Ban())
                .addEventListeners(new Unban())
                .addEventListeners(new BanList())
                .addEventListeners(new Mute())
                .addEventListeners(new Unmute())
                .addEventListeners(new Warning())
                .addEventListeners(new WarningsCommand())
                .addEventListeners(new ClearWarningsCommand())
                .addEventListeners(new SlowModeCommand())
                .addEventListeners(new Kick())
                .build();
    }
}