package org.flowerplatform.js_client.server.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/testService")
public class TestService {

	@GET @Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public TestBean get() {
		return new TestBean2();
	}
	
	@POST @Path("/set")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TestBean set(TestBean bean) {
		return bean;
	}
	
	//////////////////////////////////////////////////
	// Test cases for typed/untyped collections
	//////////////////////////////////////////////////
	
	@GET @Path("/listUntyped")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> listUntyped() {
		List<Object> list = new ArrayList<Object>();
		list.add(new TestBean());
		list.add(new TestBean2());
		return list;
	}
	
	@GET @Path("/listTyped")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TestBean> listTyped() {
		List<TestBean> list = new ArrayList<TestBean>();
		list.add(new TestBean());
		list.add(new TestBean2());
		return list;
	}
	
	@GET @Path("/mapUntyped")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> mapUntyped() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Base Bean", new TestBean());
		map.put("Extended Bean", new TestBean2());
		return map;
	}
	
	@GET @Path("/mapTyped")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, TestBean> mapTyped() {
		Map<String, TestBean> map = new HashMap<String, TestBean>();
		map.put("Base Bean", new TestBean());
		map.put("Extended Bean", new TestBean2());
		return map;
	}
	
}
