package me.ahsansadik.Moderation.XP;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class XPListener extends ListenerAdapter {
    private final Random random = new Random();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Member member = event.getMember();
        if (member == null) return;

        int randomXP = 5 + new Random().nextInt(10); // 5–15 XP
        XPManager.addXP(member, randomXP);
    }
}
