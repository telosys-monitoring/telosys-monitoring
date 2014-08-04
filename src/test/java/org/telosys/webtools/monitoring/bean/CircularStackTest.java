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

public class CircularStackTest {

	@Test
	public void testAscendant() {
		final CircularStack circularStack = new CircularStack(3);
		assertEquals(0, circularStack.getAllAscending().size());

		final Request r1 = new Request();
		r1.countLongTimeRequests = 1;
		circularStack.add(r1);
		List<Request> results = circularStack.getAllAscending();
		assertEquals(1, results.size());
		assertEquals(r1, results.get(0));

		final Request r2 = new Request();
		r2.countLongTimeRequests = 2;
		circularStack.add(r2);
		results = circularStack.getAllAscending();
		assertEquals(2, results.size());
		assertEquals(r1, results.get(0));
		assertEquals(r2, results.get(1));

		final Request r3 = new Request();
		r3.countLongTimeRequests = 3;
		circularStack.add(r3);
		results = circularStack.getAllAscending();
		assertEquals(3, results.size());
		assertEquals(r1, results.get(0));
		assertEquals(r2, results.get(1));
		assertEquals(r3, results.get(2));

		final Request r4 = new Request();
		r4.countLongTimeRequests = 4;
		circularStack.add(r4);
		results = circularStack.getAllAscending();
		assertEquals(3, results.size());
		assertEquals(r2, results.get(0));
		assertEquals(r3, results.get(1));
		assertEquals(r4, results.get(2));

		final Request r5 = new Request();
		r5.countLongTimeRequests = 5;
		circularStack.add(r5);
		results = circularStack.getAllAscending();
		assertEquals(3, results.size());
		assertEquals(r3, results.get(0));
		assertEquals(r4, results.get(1));
		assertEquals(r5, results.get(2));

	}

	@Test
	public void testDescendant() {
		final CircularStack circularStack = new CircularStack(3);
		assertEquals(0, circularStack.getAllDescending().size());

		final Request r1 = new Request();
		r1.countLongTimeRequests = 1;
		circularStack.add(r1);
		List<Request> results = circularStack.getAllDescending();
		assertEquals(1, results.size());
		assertEquals(r1, results.get(0));

		final Request r2 = new Request();
		r2.countLongTimeRequests = 2;
		circularStack.add(r2);
		results = circularStack.getAllDescending();
		assertEquals(2, results.size());
		assertEquals(r2, results.get(0));
		assertEquals(r1, results.get(1));

		final Request r3 = new Request();
		r3.countLongTimeRequests = 3;
		circularStack.add(r3);
		results = circularStack.getAllDescending();
		assertEquals(3, results.size());
		assertEquals(r3, results.get(0));
		assertEquals(r2, results.get(1));
		assertEquals(r1, results.get(2));

		final Request r4 = new Request();
		r4.countLongTimeRequests = 4;
		circularStack.add(r4);
		results = circularStack.getAllDescending();
		assertEquals(3, results.size());
		assertEquals(r4, results.get(0));
		assertEquals(r3, results.get(1));
		assertEquals(r2, results.get(2));

		final Request r5 = new Request();
		r5.countLongTimeRequests = 5;
		circularStack.add(r5);
		results = circularStack.getAllDescending();
		assertEquals(3, results.size());
		assertEquals(r5, results.get(0));
		assertEquals(r4, results.get(1));
		assertEquals(r3, results.get(2));


	}

}
