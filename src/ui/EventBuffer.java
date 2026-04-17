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
    private boolean rollingEventFinished;
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
        if (!rollingEventInProgress) {
            dequeueEvent();
        }
        if (rollingEventInProgress) advanceCurrentEventTyping();
        // System.out.println(eventQueue.size());

    }

    public void updateVisibleEventWindow() {
        if (eventThree.isEmpty() && eventTwo.isEmpty() && eventOne.isEmpty()) {
            return;
        }
        if (!eventOne.isEmpty() && eventTwo.isEmpty() && eventThree.isEmpty()) {
            eventTwo = eventOne;
            eventOne = "";
            isEventUpdated = true;
            return;
        }
        if (!eventOne.isEmpty() && !eventTwo.isEmpty() && eventThree.isEmpty()) {
            eventThree = eventTwo;
            eventTwo = eventOne;
            eventOne = "";
            isEventUpdated = true;
            return;
        }
        if (!eventOne.isEmpty() && !eventTwo.isEmpty() && !eventThree.isEmpty()) {
            eventThree = eventTwo;
            eventTwo = eventOne;
            eventOne = "";
            isEventUpdated = true;
        }

    }

    public void advanceCurrentEventTyping() {
        if (rollingEventInProgress) {
            rollingEventLength = rollingEvent.length();
            if (rollingEventLength <= rollingEventProgress) {
              //  System.out.println("Event done printing");
                rollingEventInProgress = false;
                rollingEvent = "";
                rollingEventProgress = 0;
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
                updateVisibleEventWindow();
                rollingEventInProgress = true;
                //System.out.println("Event in progress");
            }
        }
    }
}
