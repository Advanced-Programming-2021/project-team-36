package view.CommandLine;

import Utils.ParserException;

import java.util.Map;

public interface Handler {
    void run(Map<String, String> cmd) throws ParserException;
}
