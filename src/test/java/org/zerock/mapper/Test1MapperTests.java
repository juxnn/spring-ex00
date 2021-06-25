package org.zerock.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.Test1VO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class Test1MapperTests {

	
	@Setter(onMethod_=@Autowired)
	private Test1Mapper mapper;
	
	@Test
	public void testGetList() {
		assertNotNull(mapper);
		List<Test1VO> list = mapper.getList();
		
		assertTrue(list.size()>0);
	}
	
	@Test
	public void testInsert() {
		Test1VO test = new Test1VO();
		test.setName("wj");
		test.setAge(20);
		
		int cnt = mapper.insert(test);
		assertEquals(1, cnt);
	}

	@Test
	public void testInsertSelectKey() {
		Test1VO test = new Test1VO();
		test.setName("원준");
		test.setAge(30);
		
		assertEquals(0, test.getId());
		
		int cnt = mapper.insertSelectKey(test);
		
		assertEquals(1, cnt);
		assertNotEquals(0, test.getId());
	}
	
	@Test
	public void testRead() {
		Test1VO test = mapper.read(2);
		assertNotNull(test);
		assertEquals(2, test.getId());
		
		Test1VO test2 = new Test1VO();
		test2.setName("sk");
		test2.setAge(28);
		
		int cnt = mapper.insertSelectKey(test2);
		
		long key = test2.getId();
		
		Test1VO newTest = mapper.read(key);
		assertNotNull(newTest);
		assertEquals(key, newTest.getId());
	}
	
	@Test
	public void testUpdate() {
		long id = 5;
		
		Test1VO test = new Test1VO();
		test.setId(id);
		test.setName("최원준");
		test.setAge(99);
		
		int cnt = mapper.update(test);
		assertEquals(1, cnt);
		
		Test1VO test5 = mapper.read(5);
		assertEquals(test.getName(), test5.getName());
		assertEquals(test.getAge(), test5.getAge());

	}
	
	@Test
	public void testDelete() {
		
		int cnt = mapper.delete(0);
		assertEquals(0, cnt);
		
		Test1VO test = new Test1VO();
		test.setName("name");
		test.setAge(100);
		
		mapper.insertSelectKey(test);
		
		cnt = mapper.delete(test.getId());
		assertEquals(1, cnt);
	}
}
