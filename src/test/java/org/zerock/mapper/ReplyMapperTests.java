package org.zerock.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {

	@Setter(onMethod_=@Autowired )
	ReplyMapper mapper;
	
	@Test
	public void test() {
		assertNotNull(mapper);
	}
	
	@Test
	public void testInsert() {
		ReplyVO vo = new ReplyVO();
		vo.setReply("불금 댓글!!");
		vo.setReplyer("user00");
		vo.setBno(124L);
		
		int cnt = mapper.insert(vo);
		assertEquals(1, cnt);	
	}
	
	@Test
	public void testRead() {
		ReplyVO vo = mapper.read(2L);
		
		assertEquals("불금 댓글!!", vo.getReply());
	}

	@Test
	public void testDelete() {
		ReplyVO vo = new ReplyVO();
		vo.setBno(124L);
		vo.setReply("댓글");
		vo.setReplyer("user00");
		
		mapper.insertSelectKey(vo);
		int cnt = mapper.delete(vo.getRno());
		
		assertEquals(1, cnt);
	}
	
	@Test
	public void testUpdate() {
		ReplyVO vo = mapper.read(1L);
		String re = "수정된 댓글";
		vo.setReply(re);
		assertEquals(1, mapper.update(vo));
		
		vo = mapper.read(1L);
		assertEquals(re, vo.getReply());
		
	}
	
	@Test
	public void testGetList() {
		Long bno = 124L;
		
		List<ReplyVO> list = mapper.getList(bno);
		assertTrue(list.size()>0);
		
		bno = 49L;
		list = mapper.getList(bno);
		assertTrue(list.size() == 0);
		}
}
