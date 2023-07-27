package ansk.development.service.event_handlers;

/**
 * Encapsulates the logic of constructing the chain of event handlers.
 *
 * @author Anton Skripin
 */
public class EventHandlerBuilder {

    private EventHandler topEventHandler;

    private EventHandlerBuilder() {
    }

    public static EventHandlerBuilder initializeChain() {
        return new EventHandlerBuilder();
    }

    public EventHandlerBuilder onlyWithChatId() {
        this.topEventHandler = new WithChatIdFilter(topEventHandler);
        return this;
    }

    public EventHandlerBuilder onlyWithEnabledNotifications() {
        this.topEventHandler = new WithEnabledNotificationsFilter(topEventHandler);
        return this;
    }

    public EventHandlerBuilder onlyWithTodayEvents() {
        this.topEventHandler = new WithOnlyTodayEventsFilter(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withUnknownUserHandler() {
        this.topEventHandler = new UnknownUserHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withAdaptableNotificationPolicyHandler() {
        this.topEventHandler = new ChangeBotNotificationPolicyHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withWorkoutWithDumbbellsHandler() {
        this.topEventHandler = new GenerateWorkoutWithDumbbellsHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withStretchingWorkoutHandler() {
        this.topEventHandler = new GenerateStretchingWorkoutHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withResetTimerHandler() {
        this.topEventHandler = new ResetTimerHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withAbsWorkoutHandler() {
        this.topEventHandler = new GenerateAbsWorkoutHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withWeightFreeWorkoutHandler() {
        this.topEventHandler = new GenerateWeightFreeWorkoutHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withPushUpsWorkout() {
        this.topEventHandler = new GeneratePushUpsWorkoutHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withCancelWorkoutIfRunningHandler() {
        topEventHandler = new WithCancelableRunningWorkout(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withUnknownCommandHandler() {
        topEventHandler = new UnknownCommandHandler(topEventHandler);
        return this;
    }

    public EventHandlerBuilder withStoppableWorkoutHandler() {
        topEventHandler = new StopWorkoutHandler(topEventHandler);
        return this;
    }

    public EventHandler build() {
        return this.topEventHandler;
    }
}
