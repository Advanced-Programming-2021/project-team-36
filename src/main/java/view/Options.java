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
}
