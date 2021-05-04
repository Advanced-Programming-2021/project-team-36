package view;

import org.apache.commons.cli.Option;

public class Options {
    static Option username(boolean required){
        Option ret = new Option("u", "username", true, "username");
        ret.setRequired(required);
        return ret;
    }
    static Option password(boolean required){
        Option ret = new Option("p", "password", true, "password");
        ret.setRequired(required);
        return ret;
    }
    static Option nickname(boolean required){
        Option ret = new Option( "n", "nickname", true, "nickname");
        ret.setRequired(required);
        return ret;
    }
    static Option requirePassword(boolean required){
        Option ret = new Option("password", false, "password required");
        ret.setRequired(required);
        return ret;
    }
    static Option currentPassword(boolean required){
        Option ret = new Option("current", true, "current password");
        ret.setRequired(required);
        return ret;
    }
    static Option newPassword(boolean required){
        Option ret = new Option("new", true, "new password");
        ret.setRequired(required);
        return ret;
    }

    static Option card(boolean required){
        Option ret = new Option("card", true, "card name");
        ret.setRequired(required);
        return ret;
    }
    static Option deck(boolean required){
        Option ret = new Option("deck", true, "deck name");
        ret.setRequired(required);
        return ret;
    }
    static Option side(){
        Option ret = new Option("side", false, "add to side deck");
        ret.setRequired(false);
        return ret;
    }
    static Option all(boolean required){
        Option ret = new Option("all", false, "all");
        ret.setRequired(required);
        return ret;
    }

    static Option cards(boolean required){
        Option ret = new Option("cards", false, "cards");
        ret.setRequired(required);
        return ret;
    }
    static Option secondPlayer(boolean required){
        Option ret = new Option("second-player", true, "second-player");
        ret.setRequired(required);
        return ret;
    }
    static Option round(boolean required){
        Option ret = new Option("round", true, "round");
        ret.setRequired(required);
        return ret;
    }

    static Option newRound(boolean required){
        Option ret = new Option("new", false, "new round");
        ret.setRequired(required);
        return ret;
    }
    static Option ai(boolean required){
        Option ret = new Option("ai", false, "ai");
        ret.setRequired(required);
        return ret;
    }

    static Option monsterZone(boolean required){
        Option ret = new Option("monster", true, "monster");
        ret.setRequired(required);
        return ret;
    }
    static Option spellZone(boolean required){
        Option ret = new Option("spell", true, "spell");
        ret.setRequired(required);
        return ret;
    }
    static Option fieldZone(boolean required){
        Option ret = new Option("field", true, "field");
        ret.setRequired(required);
        return ret;
    }
    static Option handZone(boolean required){
        Option ret = new Option("hand", true, "hand");
        ret.setRequired(required);
        return ret;
    }
    static Option opponent(boolean required){
        Option ret = new Option("opponent", false, "opponent");
        ret.setRequired(required);
        return ret;
    }
    static Option Deselect(boolean required){
        Option ret = new Option("d", false, "deselect");
        ret.setRequired(required);
        return ret;
    }
    static Option cardPosition(boolean required){
        Option ret = new Option("position", true, "card position");
        ret.setRequired(required);
        return ret;
    }
    static Option selected(boolean required){
        Option ret = new Option("selected", false, "selected");
        ret.setRequired(required);
        return ret;
    }
}