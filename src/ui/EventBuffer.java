package ui;

import engine.GameClock;
import structures.ArrayQueue;

public class EventBuffer {

    private String eventOne = "";
    private String eventTwo = "";
    private String eventThree = "";
    private String rollingEvent;
    private boolean rollingEventInProgress;
    private boolean isEventUpdated;
    private int rollingEventLength;
    private int rollingEventProgress;
    ArrayQueue<String> eventQueue = new ArrayQueue<>();

    public String getEventOne() {
        return eventOne;
    }

    public String getEventTwo() {
        return eventTwo;
    }

    public String getEventThree() {
        return eventThree;
    }

    public boolean isNewEvent() {
        return rollingEventInProgress;
    }

    public void updateRecentEventsPanel() {
        if (!rollingEventInProgress) dequeueEvent();
        if(rollingEventInProgress && !isEventUpdated) updateVisibleEventWindow();
        if (rollingEventInProgress) advanceCurrentEventTyping();

    }

    public void updateVisibleEventWindow() {
        eventThree = eventTwo;
        eventTwo = eventOne;
        eventOne = "";
        isEventUpdated = true;
    }

    public void advanceCurrentEventTyping() {
        if (rollingEventInProgress) {
            rollingEventLength = rollingEvent.length();
            if (rollingEventLength <= rollingEventProgress) {
                System.out.println("Event done printing");
                rollingEventInProgress = false;
                rollingEvent = null;
                return;
            }

            rollingEventProgress = rollingEventProgress + 3;
            eventOne = rollingEvent.substring(0, Math.min(rollingEventProgress, rollingEventLength));
        }
    }

    public void enqueueEvent(String event) {
        eventQueue.enqueue("> " + GameClock.getCurrentTime(false) + " - " + event);
    }

    public void dequeueEvent() {
        if (!rollingEventInProgress) {
            if (!eventQueue.isEmpty()) {
                rollingEvent = eventQueue.dequeue();
                isEventUpdated = false;
                rollingEventInProgress = true;
            }
        }
    }
}
