package me.ahsansadik.Moderation.Features;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;

public class BadWordFilter extends ListenerAdapter {
    private final List<String> badWords = Arrays.asList("assfuck", "cuntfucker", "niggers", "niggerhole", "nigger", "balllicker", "nlgger", "porchmonkey", "Porch-monkey",
            "cunt", "asswhore", "fuck", "assjockey", "Dothead", "blacks", "cumqueen", "fatfucker", "Jigaboo", "jiggabo", "nlggor",
            "snownigger", "Spearchucker", "Timber-nigger", "shitnigger", "asslick", "shithead", "asshole", "asshole", "cuntlicker",
            "kunt", "spaghettinigger", "Towel-head", "Chernozhopy", "asslicker", "Bluegum", "twat", "ABCD", "bitchslap", "bulldyke",
            "choad", "cumshot", "fatass", "jigger", "kyke", "cumskin", "asian", "asscowboy", "assmuncher", "banging", "Burrhead",
            "Camel-Jockey", "coon", "crotchrot", "cumfest", "dicklicker", "fag", "fagot", "felatio", "fatfuck", "goldenshower",
            "hore", "jackoff", "jigg", "jigga", "jizjuice", "jizm", "jiz", "jigger", "jizzim", "kumming", "kunilingus", "Moolinyan",
            "motherfucking", "motherfuckings", "phuk", "Sheboon", "shitforbrains", "slanteye", "spick", "fuuck", "antinigger",
            "aperest", "Americoon", "ABC", "Aunt-Jemima", "queer", "anal", "asspirate", "addict", "bitch", "ass", "Buddhahead",
            "chode", "phuking", "phukking", "bastard", "bulldike", "dripdick", "assassination", "A-rab", "Buckra", "bootycall",
            "assholes", "assbagger", "cheesedick", "cooter", "cum", "cumquat", "cunnilingus", "datnigga", "deepthroat", "dick",
            "dickforbrains", "dickbrain", "dickless", "dike", "diddle", "dixiedyke", "Eskimo", "fannyfucker", "fatso", "fckcum",
            "Golliwog", "Goyim", "homobangers", "hooters", "Indognesial", "Indonesial", "jew", "jijjiboo", "knockers", "kummer",
            "mothafucka", "mooncricket", "Moon-Cricket", "Oven-Dodger", "Peckerwood", "phuked", "piccaninny", "picaninny", "phuq",
            "Polock", "poorwhitetrash", "prick", "pu55y", "Pshek", "slut", "jizzum", "cunteyed", "Spic", "Swamp-Guinea",
            "stupidfucker", "stupidfuck", "titfuck", "Twinkie", "cock", "Abeed", "analannie", "asshore", "Beaner", "Bootlip",
            "Burr-head", "buttfucker", "butt-fucker", "Uncle-Tom", "cocksmoker", "Africoon", "AmeriKKKunt", "antifaggot",
            "assklown", "asspuppies", "blackman", "jism", "blumpkin", "retard", "Gringo", "douchebag", "Piefke", "areola",
            "backdoorman", "Abbie", "bigbutt", "buttface", "cumbubble", "cumming", "Dego", "dong", "doggystyle", "doggiestyle",
            "erection", "feces", "goddamned", "gonzagas", "Greaser", "Greaseball", "handjob", "Half-breed", "horney", "jihad",
            "kumquat", "Lebo", "Moskal", "Mountain-Turk", "nofuckingway", "orgies", "orgy", "pecker", "poontang", "poon",
            "Polentone", "pu55i", "shitfuck", "shiteater", "shitdick", "sluts", "slutt", "Mangal", "Hymie", "stiffy", "titfucker",
            "twink", "asspacker", "barelylegal", "beaner", "Bozgor", "bumfuck", "shit for brains", "butchdyke", "butt-fuckers",
            "buttpirate", "cameljockey", "Carcamano", "Chankoro", "Choc-ice", "Chug", "Ciapaty-or-ciapak", "Cina", "cocksucer",
            "crackwhore", "Bougnoule", "unfuckable", "Africoon-Americoon", "Africoonia", "Americunt", "apesault", "Assburgers",
            "fucktardedness", "sheepfucker", "Wuhan-virus", "Wetback", "Aseng", "bumblefuck", "fastfuck", "itch", "nizzle",
            "Oriental", "cisgender", "ballsack", "penis", "zigabo", "Bule", "breastman", "bountybar", "Bounty-bar", "bondage",
            "bombing", "bullshit", "asses", "cancer", "cunilingus", "cummer", "dicklick", "ejaculation", "faeces", "fairy", "hoes",
            "idiot", "Laowai", "Leb", "muff", "muffdive", "Oreo", "orgasm", "orgasim", "osama", "peepshow", "Petrol-sniffer",
            "perv", "prickhead", "shitfit", "spermbag", "suckmytit", "suckmydick", "suckmyass", "suckme", "suckdick", "Yuon",
            "motherfucker", "groe", "Ali Baba", "retarded", "assfucker", "assmunch", "assranger", "Ayrab", "assclown", "buttfuck",
            "butt-fuck", "buttman", "Chink", "cocksucker", "cooly", "Coon-ass", "crotchmonkey", "Bohunk", "cockcowboy",
            "cocksmith", "catfucker", "fucktardedly", "trans-testicle", "Wigger", "whiskeydick", "aboriginal", "asskisser",
            "whitelist", "Latinx", "yambag", "boob", "beef curtains", "clunge", "af", "wokeness", "bitchez", "Iceberg Fuckers",
            "Zhyd", "bellend", "arsehole", "tatas", "assassinate", "boonga", "booby", "bullcrap", "defecate", "Dhoti", "dope",
            "hobo", "bigass", "hussy", "illegal", "ky", "moneyshot", "molestor", "nooner", "nookie", "nookey", "Paleface",
            "pansy", "peehole", "phonesex", "period", "pornking", "pornflick", "porn", "pooper", "sexwhore", "shitface", "shit",
            "slav", "slimeball", "sniggers", "snowback", "spermherder", "spankthemonkey", "spitter", "strapon", "Tacohead",
            "suckoff", "titbitnipply", "Turco-Albanian", "tranny", "trannie", "zhidovka", "zhid", "Bakra");

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw().toLowerCase();
        for (String badWord : badWords) {
            if (message.contains(badWord)) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Your message contained inappropriate language and was deleted.").queue();
                break;
            }
        }
    }
}
