package com.rdpk.mars.console;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rdpk.mars.console.CommandType.*;

class ProtocolCommandParser {

  static Pattern create = Pattern.compile("(\\d*)\\s(\\d*)");
  static Pattern activate = Pattern.compile("(\\d*)\\s(\\d*)\\s([N|S|E|W])");
  static Pattern move = Pattern.compile("[M|L|R]*");

  private final String protocolCommand ;
  private final Matcher createMatcher;
  private final Matcher activateMatcher;
  private final Matcher moveMatcher;

  CommandType type = null;

  ProtocolCommandParser(String protocolCommand) {
    this.protocolCommand = protocolCommand;
    this.createMatcher = create.matcher(protocolCommand);
    this.activateMatcher = activate.matcher(protocolCommand);
    this.moveMatcher = move.matcher(protocolCommand);
  }

  void parse() {
    if (createMatcher.matches()) {
      type = CREATE;
    } else if (activateMatcher.matches()) {
      type = ACTIVATE;
    } else if (moveMatcher.matches()) {
      type = MOVE;
    } else {
      throw new IllegalArgumentException("Sorry, invalid command");
    }
  }

  Tuple<Integer, Integer> create() {
    return new Tuple<>(new Integer(createMatcher.group(1)), new Integer(createMatcher.group(2)));
  }

  ActivateRoover activate() {
   return new ActivateRoover(new Integer(activateMatcher.group(1)), new Integer(activateMatcher.group(2)),
            activateMatcher.group(3).charAt(0));
  }

  String move() {
    return protocolCommand;
  }

}
