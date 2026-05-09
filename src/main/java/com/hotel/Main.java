package com.hotel;

import com.hotel.cli.CommandHandler;

/**
 * Entry point of the Hotel Management System.
 * Created a CommandHandler to starts the system.
 */
public class Main {

    public static void main(String[] args) {


        CommandHandler commandHandler = new CommandHandler();
        //start system
        commandHandler.start();
    }
}