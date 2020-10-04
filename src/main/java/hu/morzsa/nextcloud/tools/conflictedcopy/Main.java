package hu.morzsa.nextcloud.tools.conflictedcopy;

import org.apache.commons.cli.*;

public class Main {

    public static void main(String args[]) throws Exception{

        Options options = new Options();

        Option recursiveOption = new Option("r", "recursive", false, "Recursive mode.");
        recursiveOption.setRequired(false);
        options.addOption(recursiveOption);

        Option pathOption = new Option("p", "targetPath", true, "Path for scan and replace conflicted copies. Default: current targetPath.");
        pathOption.setRequired(false);
        options.addOption(pathOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        ;
        String targetPath = cmd.getOptionValue("targetPath");

        if (targetPath == null){
            targetPath = System.getProperty("user.dir");
        }

        PathProcessor pathProcessor = new PathProcessor(targetPath, cmd.hasOption("r"));
        pathProcessor.start();

    }

}
