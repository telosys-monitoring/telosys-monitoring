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
package org.telosys.webtools.monitoring.bean;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class TopRequestsTest {

	@Test
	public void testAdd1Descending() {
		final TopRequests topRequests = new TopRequests(3);
		List<Request> requests;

		final Request r1 = new Request();
		r1.elapsedTime = 10;
		topRequests.add(r1);
		requests = topRequests.getAllDescending();
		assertEquals(1, requests.size());
		assertEquals(r1, requests.get(0));

		final Request r2 = new Request();
		r2.elapsedTime = 12;
		topRequests.add(r2);
		requests = topRequests.getAllDescending();
		assertEquals(2, requests.size());
		assertEquals(r2, requests.get(0));
		assertEquals(r1, requests.get(1));

		final Request r3 = new Request();
		r3.elapsedTime = 14;
		topRequests.add(r3);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r3, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r1, requests.get(2));

		final Request r4 = new Request();
		r4.elapsedTime = 16;
		topRequests.add(r4);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r4, requests.get(0));
		assertEquals(r3, requests.get(1));
		assertEquals(r2, requests.get(2));

		final Request r5 = new Request();
		r5.elapsedTime = 16;
		topRequests.add(r5);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r4, requests.get(0));
		assertEquals(r5, requests.get(1));
		assertEquals(r3, requests.get(2));
	}

	@Test
	public void testAdd1Ascending() {
		final TopRequests topRequests = new TopRequests(3);
		List<Request> requests;

		final Request r1 = new Request();
		r1.elapsedTime = 10;
		topRequests.add(r1);
		requests = topRequests.getAllAscending();
		assertEquals(1, requests.size());
		assertEquals(r1, requests.get(0));

		final Request r2 = new Request();
		r2.elapsedTime = 12;
		topRequests.add(r2);
		requests = topRequests.getAllAscending();
		assertEquals(2, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r2, requests.get(1));

		final Request r3 = new Request();
		r3.elapsedTime = 14;
		topRequests.add(r3);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r3, requests.get(2));

		final Request r4 = new Request();
		r4.elapsedTime = 16;
		topRequests.add(r4);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r2, requests.get(0));
		assertEquals(r3, requests.get(1));
		assertEquals(r4, requests.get(2));

		final Request r5 = new Request();
		r5.elapsedTime = 16;
		topRequests.add(r5);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r3, requests.get(0));
		assertEquals(r4, requests.get(1));
		assertEquals(r5, requests.get(2));
	}

	@Test
	public void testAdd2Descending() {
		final TopRequests topRequests = new TopRequests(3);

		final Request r1 = new Request();
		r1.elapsedTime = 10;
		topRequests.add(r1);
		List<Request> requests = topRequests.getAllDescending();
		assertEquals(1, requests.size());
		assertEquals(r1, requests.get(0));

		final Request r2 = new Request();
		r2.elapsedTime = 8;
		topRequests.add(r2);
		requests = topRequests.getAllDescending();
		assertEquals(2, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r2, requests.get(1));

		final Request r3 = new Request();
		r3.elapsedTime = 6;
		topRequests.add(r3);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r3, requests.get(2));

		final Request r4 = new Request();
		r4.elapsedTime = 4;
		topRequests.add(r4);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r3, requests.get(2));

		final Request r5 = new Request();
		r5.elapsedTime = 2;
		topRequests.add(r5);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r3, requests.get(2));
	}

	@Test
	public void testAdd2Ascending() {
		final TopRequests topRequests = new TopRequests(3);

		final Request r1 = new Request();
		r1.elapsedTime = 10;
		topRequests.add(r1);
		List<Request> requests = topRequests.getAllAscending();
		assertEquals(1, requests.size());
		assertEquals(r1, requests.get(0));

		final Request r2 = new Request();
		r2.elapsedTime = 8;
		topRequests.add(r2);
		requests = topRequests.getAllAscending();
		assertEquals(2, requests.size());
		assertEquals(r2, requests.get(0));
		assertEquals(r1, requests.get(1));

		final Request r3 = new Request();
		r3.elapsedTime = 6;
		topRequests.add(r3);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r3, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r1, requests.get(2));

		final Request r4 = new Request();
		r4.elapsedTime = 4;
		topRequests.add(r4);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r3, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r1, requests.get(2));

		final Request r5 = new Request();
		r5.elapsedTime = 2;
		topRequests.add(r5);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r3, requests.get(0));
		assertEquals(r2, requests.get(1));
		assertEquals(r1, requests.get(2));
	}

	@Test
	public void testAdd3Descending() {
		final TopRequests topRequests = new TopRequests(3);

		final Request r1 = new Request();
		r1.elapsedTime = 10;
		topRequests.add(r1);
		List<Request> requests = topRequests.getAllDescending();
		assertEquals(1, requests.size());
		assertEquals(r1, requests.get(0));

		final Request r2 = new Request();
		r2.elapsedTime = 8;
		topRequests.add(r2);
		requests = topRequests.getAllDescending();
		assertEquals(2, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r2, requests.get(1));

		final Request r3 = new Request();
		r3.elapsedTime = 12;
		topRequests.add(r3);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r3, requests.get(0));
		assertEquals(r1, requests.get(1));
		assertEquals(r2, requests.get(2));

		final Request r4 = new Request();
		r4.elapsedTime = 6;
		topRequests.add(r4);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r3, requests.get(0));
		assertEquals(r1, requests.get(1));
		assertEquals(r2, requests.get(2));

		final Request r5 = new Request();
		r5.elapsedTime = 14;
		topRequests.add(r5);
		requests = topRequests.getAllDescending();
		assertEquals(3, requests.size());
		assertEquals(r5, requests.get(0));
		assertEquals(r3, requests.get(1));
		assertEquals(r1, requests.get(2));
	}

	@Test
	public void testAdd3Ascending() {
		final TopRequests topRequests = new TopRequests(3);

		final Request r1 = new Request();
		r1.elapsedTime = 10;
		topRequests.add(r1);
		List<Request> requests = topRequests.getAllAscending();
		assertEquals(1, requests.size());
		assertEquals(r1, requests.get(0));

		final Request r2 = new Request();
		r2.elapsedTime = 8;
		topRequests.add(r2);
		requests = topRequests.getAllAscending();
		assertEquals(2, requests.size());
		assertEquals(r2, requests.get(0));
		assertEquals(r1, requests.get(1));

		final Request r3 = new Request();
		r3.elapsedTime = 12;
		topRequests.add(r3);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r2, requests.get(0));
		assertEquals(r1, requests.get(1));
		assertEquals(r3, requests.get(2));

		final Request r4 = new Request();
		r4.elapsedTime = 6;
		topRequests.add(r4);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r2, requests.get(0));
		assertEquals(r1, requests.get(1));
		assertEquals(r3, requests.get(2));

		final Request r5 = new Request();
		r5.elapsedTime = 14;
		topRequests.add(r5);
		requests = topRequests.getAllAscending();
		assertEquals(3, requests.size());
		assertEquals(r1, requests.get(0));
		assertEquals(r3, requests.get(1));
		assertEquals(r5, requests.get(2));
	}

}
