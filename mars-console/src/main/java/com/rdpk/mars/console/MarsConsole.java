package com.rdpk.mars.console;

import java.io.IOException;
import java.util.Scanner;

public class MarsConsole {

  public static void main(String[] args) throws IOException {

    MarsServerClient client;
    Scanner scanner = new Scanner(System.in);

    System.out.println("*** Welcome to Mars! Press CRTL+C to quit");

    System.out.print("Enter plateau's name: ");
    String plateauName = scanner.nextLine();
    client = new MarsServerClient(plateauName);

    while(true) {

      System.out.print("Enter your command: ");
      String command = scanner.nextLine();

      System.out.println("  -> command [" + command + "]");

      ProtocolCommandParser parser = new ProtocolCommandParser(command.trim());
      parser.parse();

      System.out.println("  -> type [" + parser.type + "]");

      switch (parser.type) {
        case CREATE: System.out.println(client.create(parser.create())); break;
        case ACTIVATE: System.out.println(client.activate(parser.activate())); break;
        case MOVE: System.out.println(client.move(parser.move())); break;
        default: System.out.println("Bye!"); return;
      }

    }

  }

}
