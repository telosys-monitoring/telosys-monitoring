/**
 *  Copyright (C) 2008-2014  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.webtools.monitoring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.servlet.ServletException;

import org.junit.Test;
import org.telosys.webtools.monitoring.bean.Request;
import org.telosys.webtools.monitoring.monitor.MonitorInitValues;
import org.telosys.webtools.monitoring.monitor.MonitorInitValuesManager;
import org.telosys.webtools.monitoring.monitor.MonitorAttributeNames;

public class RequestsMonitorMultiThreadTest {

	@Test
	public void initDefaultsWithErrorManagement() {
		try {
			initDefaults();
		} catch(final Throwable t) {
			t.printStackTrace(System.err);
			fail(t.getMessage());
		}
	}

	public void initDefaults() throws ServletException, InterruptedException {
		System.out.println("Test - Begin");

		final RequestsMonitor requestsMonitor = new RequestsMonitor();

		final MonitorInitValues initValues = new MonitorInitValues();
		initValues.durationThreshold = -999;
		initValues.logSize = 100;
		initValues.topTenSize = 100;
		initValues.longestSize = 100;
		initValues.traceFlag = false;
		requestsMonitor.initValues = initValues;

		final MonitorInitValuesManager monitorWebXmlManager = new MonitorInitValuesManager();
		monitorWebXmlManager.reset(initValues, requestsMonitor.data);

		final int nbRequestsBySender = 5;
		final int nbThreads = 1500;
		final int delayRequestSending = 0;
		final int delayAction = 2;

		final Counter counter = new Counter();
		final Random random = new Random();

		final CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch doneSignal = new CountDownLatch(nbThreads);

		final List<Thread> threads = new ArrayList<Thread>();
		final List<SendRequest> sendRequests = new ArrayList<SendRequest>();
		for(int i=0; i<nbThreads; i++) {
			final SendRequest sendRequest =
					new SendRequest(i, startSignal, doneSignal, requestsMonitor, counter, random, nbRequestsBySender, delayRequestSending);
			sendRequests.add(sendRequest);
			final Thread thread = new Thread(sendRequest);
			threads.add(thread);
		}

		// Run
		for(final Thread thread : threads) {
			thread.start();
		}
		startSignal.countDown();

		// Random actions
		int count = 0;
		while(doneSignal.getCount() > 0) {
			Thread.sleep(delayAction);
			randomActions(requestsMonitor, random, count);
			count++;
		}
		doneSignal.await();

		System.out.println("Test - End");

		System.out.println("countAllRequest: " + requestsMonitor.data.countAllRequest);
		System.out.println("countLongTimeRequests: " + requestsMonitor.data.countLongTimeRequests);
		System.out.println("logLines: " + requestsMonitor.data.logLines.getAllAscending().size());
		System.out.println("by_time: " + requestsMonitor.data.topRequests.getAllDescending().size());
		System.out.println("by_url : " + requestsMonitor.data.longestRequests.getAllDescending().size());

		// Verify counts in requests
		long countAll = -1;
		long countLongest = -1;
		for(final Request request : requestsMonitor.data.logLines.getAllAscending()) {
			if((countAll != -1) && (countLongest != -1)) {
				assertTrue(countAll < request.countAllRequest);
				assertTrue(countLongest < request.countLongTimeRequests);
			}
			countAll = request.countAllRequest;
			countLongest = request.countLongTimeRequests;
			assertEquals(request.countAllRequest, request.countLongTimeRequests);
		}
	}

	public void randomActions(final RequestsMonitor requestsMonitor, final Random random, final int count) {
		if((count % 10) == 5) {
			// reset
			requestsMonitor.dispatch.getAction().action(getParams("action", "reset"), requestsMonitor.data, requestsMonitor.initValues);
		}
		if(random.nextInt(50) == 25) {
			// clear
			requestsMonitor.dispatch.getAction().action(getParams("action", "clear"), requestsMonitor.data, requestsMonitor.initValues);
		}
		if(random.nextInt(50) == 25) {
			// stop
			requestsMonitor.dispatch.getAction().action(getParams("action", "stop"), requestsMonitor.data, requestsMonitor.initValues);
		}
		if(random.nextInt(50) == 25) {
			// start
			requestsMonitor.dispatch.getAction().action(getParams("action", "start"), requestsMonitor.data, requestsMonitor.initValues);
		}
		// log size
		requestsMonitor.dispatch.getAction().action(getParams(MonitorAttributeNames.ATTRIBUTE_NAME_LOG_SIZE, ""+(random.nextInt(150)+1)), requestsMonitor.data, requestsMonitor.initValues);
		// by time size
		requestsMonitor.dispatch.getAction().action(getParams(MonitorAttributeNames.ATTRIBUTE_NAME_BY_TIME_SIZE, ""+(random.nextInt(150)+1)), requestsMonitor.data, requestsMonitor.initValues);
		// by url size
		requestsMonitor.dispatch.getAction().action(getParams(MonitorAttributeNames.ATTRIBUTE_NAME_BY_URL_SIZE, ""+(random.nextInt(150)+1)), requestsMonitor.data, requestsMonitor.initValues);
	}

	private Map<String,String> getParams(final String key, final String name) {
		final Map<String,String> params = new HashMap<String, String>();
		params.put(key, name);
		return params;
	}

}
