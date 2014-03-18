package endpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import endpoint.DatastoreRouter;
import endpoint.actions.ActionType;

public class DatastoreRouterTest {

	@Test
	public void testIndex() {
		DatastoreRouter router = new DatastoreRouter("GET", "/api/devices");

		assertEquals(ActionType.INDEX, router.getAction());
		assertNull(router.getId());
	}

	@Test
	public void testShow() {
		DatastoreRouter router = new DatastoreRouter("GET", "/api/devices/100");

		assertEquals(ActionType.SHOW, router.getAction());
		assertEquals((Long) 100l, router.getId());		
	}
	
	@Test
	public void testCreate() {
		DatastoreRouter router = new DatastoreRouter("POST", "/api/devices");

		assertEquals(ActionType.CREATE, router.getAction());
		assertNull(router.getId());
	}

	@Test
	public void testUpdate() {
		DatastoreRouter router = new DatastoreRouter("PUT", "/api/devices/100");

		assertEquals(ActionType.UPDATE, router.getAction());
		assertEquals((Long) 100l, router.getId());
	}
	
	@Test
	public void testCustomAction() {
		DatastoreRouter router = new DatastoreRouter("PUT", "/api/devices/100/active");

		assertEquals(ActionType.CUSTOM, router.getAction());
		assertEquals("active", router.getCustomAction());
		assertEquals((Long) 100l, router.getId());
	}
	
}