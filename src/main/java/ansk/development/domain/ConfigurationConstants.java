package ansk.development.domain;

import java.util.concurrent.TimeUnit;

/**
 * Class with configuration constants
 */
public final class ConfigurationConstants {

    public static final String BOT_USERNAME = "ansk98_fitness_bot";

    public static final String BOT_TOKEN = "6206262291:AAHUEWiLXvloVIrPJUrMDUhFv2eE7fTTdRE";

    public static boolean IGNORE_OLD_UPDATE_EVENTS = true;

    public static final String ROOT_USERNAME = "AntonSk98";

    public static final String ROOT_CHAT_ID = "402877944";

    public static final String[] ALLOWED_USERNAMES = new String[]{"AntonSk98", "DeusEx0"};

    public static final String[] ALLOWED_CHAT_IDS = new String[]{"402877944", "1210572708"};

    public static final String REMINDER_MESSAGE = "Time to move a bit! Here is a list of exercises to be done!";

    public static final String MORNING_ROUTINE_MESSAGE = "Time to do some warm-up and stretching exercises! Enjoy it!";

    public static final String SUPPRESS_BOT = "This bot is disabled and no new notifications will be delivered";

    public static final String ENABLE_BOT = "This bot is enabled. Expect some exercise to appear";

    public static final String MESSAGE_TO_UNKNOWN_USER = String.format("Hi! This is a private bot. If you would like to use it, please write to @%s", ROOT_USERNAME);

    public static final String MESSAGE_ABOUT_UNKNOWN_USER = "One of the users tried to access your bot. The username is:";

    public static final int SEND_EXERCISE_INTERVAL_IN_MS = 30000;

    public static final int INITIAL_DELAY = 60;

    public static final int NOTIFICATION_INTERVAL = 120;

    public static final TimeUnit NOTIFICATION_INTERVAL_TIME_UNIT = TimeUnit.MINUTES;

    public static final  int CHECK_SESSION_JOB_INITIAL_DELAY = 5;

    public static final int CHECK_SESSION_JOB_INTERVAL = 5;

    public static final TimeUnit CHECK_SESSION_BOT_TIME_UNIT = TimeUnit.MINUTES;

    private ConfigurationConstants() {

    }
}
