package view.CommandLine;

import org.apache.commons.cli.CommandLine;

public interface Handler {
    void run(CommandLine cmd);
}
