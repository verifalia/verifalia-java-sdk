package com.verifalia.api.common;

import com.verifalia.api.emailvalidations.models.Validation;

/**
 * Listener object for the server polling loop
 */
public interface ServerPollingLoopEventListener {
	
	/**
	 * Polling loop event type
	 */
	enum ServerPollingLoopEvent {
		/**
		 * This event type happens once when polling loop is started.
		 */
		ServerPollingLoopStarted,

		/**
		 * This event type happens each time right before making request to server.
		 */
		BeforePollServer,
		
		/**
		 * This event type happens each time right after making request to server.
		 */
		AfterPollServer,
		
		/**
		 * This event type happens once when polling loop is finished.
		 */
		ServerPollingLoopFinished
	}
	
	/**
	 * Called when polling loop event happens
	 * @param event Identified the event which have happened
	 * @param currentResult Current poll result, if applicable. May be <b>null</b> when there is no result yet.
	 */
	void onPollingLoopEvent(ServerPollingLoopEvent event, Validation currentResult);
}
