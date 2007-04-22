package org.crank.crud.criteria.test;

import static org.crank.crud.criteria.Comparison.*;
import static org.crank.crud.criteria.Group.*;

import org.crank.crud.criteria.Example;
import org.crank.crud.criteria.Group;
import org.crank.crud.criteria.Operator;
import org.crank.crud.test.model.Employee;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;


public class GroupTest {
	
	@Test
	public void testPrime () {
		Group group = and(eq("firstName",  "Rick"), eq("lastName", "Hightower"));
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower])", group.toString());
	}

	@Test
	public void nestedPrime () {
		Group group = and(
						eq("firstName", "Rick"), eq("lastName", "Hightower"), 
						or(
							eq("foo", "bar"), eq("baz", "foo")
						)
					  );
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower, (OR [foo_EQ_bar, baz_EQ_foo])])", group.toString());
	}
	
	@Test
	public void test1 () {
		Group group = and().add("firstName", Operator.EQ, "Rick")
					.add("lastName", Operator.EQ, "Hightower");
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower])", group.toString());
	}
	@Test
	public void test2 () {
		Group group = and().eq("firstName", "Rick")
					.eq("lastName", "Hightower");
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower])", group.toString());
	}
	@Test
	public void nested () {
		Group group = and()
					.eq("firstName", "Rick")
					.eq("lastName", "Hightower")
					.add(
						or().eq("foo", "bar").eq("baz", "foo")
					);
		assertEquals("(AND [firstName_EQ_Rick, lastName_EQ_Hightower, (OR [foo_EQ_bar, baz_EQ_foo])])", group.toString());
	}

	@Test ()
	public void example () {
		Employee employee = new Employee();
		employee.setActive(true);
		employee.setAge(40);
		employee.setFirstName("Rick");
		employee.setLastName("Rick");
		Example example = Example.like(employee).excludeProperty("lastName");
		assertEquals("(AND [active_EQ_true, age_EQ_40, firstName_LIKE_CONTAINS_Rick])", example.toString());

//		example = Example.createExample(employee).excludeZeroes().excludeNone();
//		employee.setAge(0);
//		employee.setLastName(null);
//		assertEquals("(AND [active_EQ_true, age_EQ_0, department_EQ_null, firstName_EQ_Rick, id_EQ_null, lastName_EQ_null, numberOfPromotions_EQ_null])", 
//				example.toString());		
//
//		example = Example.createExample(employee);
//		employee.setAge(0);
//		employee.setLastName(null);
//		assertEquals("(AND [active_EQ_true, age_EQ_0, firstName_EQ_Rick])", example.toString());	
//
//		example = Example.createExample(employee).excludeZeroes();
//		employee.setAge(0);
//		employee.setLastName(null);
//		assertEquals("(AND [active_EQ_true, firstName_EQ_Rick])", example.toString());	
		
	}	
}
