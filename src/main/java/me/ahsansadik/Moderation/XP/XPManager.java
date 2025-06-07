package me.ahsansadik.Moderation.XP;

import me.ahsansadik.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class XPManager extends ListenerAdapter {

    public static class UserData {
        public int xp = 0;
        public int level = 1;
    }

    // 🔁 Now tracking XP per guild (Map<GuildID, Map<UserID, UserData>>)
    private static final Map<String, Map<String, UserData>> guildXPMap = new HashMap<>();

    /**
     * Returns UserData for a specific user in a specific guild.
     */
    public static UserData getXP(String guildId, String userId) {
        return guildXPMap
                .getOrDefault(guildId, Collections.emptyMap())
                .getOrDefault(userId, new UserData());
    }

    /**
     * Saves XP data for a specific user in a specific guild.
     */
    private static void saveXP(String guildId, String userId, UserData data) {
        guildXPMap
                .computeIfAbsent(guildId, k -> new HashMap<>())
                .put(userId, data);
    }

    /**
     * Adds XP to a member and handles level-up detection.
     */
    public static void addXP(Member member, int amount) {
        String guildId = member.getGuild().getId();
        String userId = member.getId();

        Map<String, UserData> userMap = guildXPMap.computeIfAbsent(guildId, k -> new HashMap<>());
        UserData data = userMap.getOrDefault(userId, new UserData());

        int oldLevel = data.level;

        data.xp += amount;
        int nextXP = 5 * (data.level * data.level) + 50 * data.level + 100;

        if (data.xp >= nextXP) {
            data.level++;
            data.xp = 0;

            // 🎉 Trigger level-up handler
            if (Main.levelUpHandler != null) {
                Main.levelUpHandler.handleLevelUp(member.getGuild(), member, data.level);
            }
        }

        saveXP(guildId, userId, data);
    }

    /**
     * Get an unmodifiable XP map for a guild (used for /leaderboard).
     */
    public static Map<String, UserData> getGuildXPData(String guildId) {
        return Collections.unmodifiableMap(
                guildXPMap.getOrDefault(guildId, new HashMap<>())
        );
    }
}
