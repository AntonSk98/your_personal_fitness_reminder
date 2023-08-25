package ansk.development;

import ansk.development.bot.FitnessReminderBot;

/**
 * An entry-point class that launches the bot.
 *
 * @author Anton Skripin
 */
public class YourPersonalFitnessReminder {
    public static void main(String[] args) {
        FitnessReminderBot
                .getFitnessBot()
                .withGreetingOnStartup()
                .withFitnessReminders()
                .withPeriodicSessionChecks();
    }
}