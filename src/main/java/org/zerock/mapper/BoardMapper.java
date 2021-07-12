package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.MemberVO;

public interface BoardMapper {

//	@Select("SELECT * FROM tbl_board")
	public List<BoardVO> getList();
	
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	//INSERT INTO tbl_board (title, content, writer)
	//VALUES (#{title}, #{content}, #{writer})
	public int insert(BoardVO board);
	
	public int insertSelectKey(BoardVO board);
	
	public BoardVO read(long bon);
	
	public int delete(long bno);
	
	public int update(BoardVO board);

	public int getTotalCount(Criteria cri);

	public void removeByUserid(MemberVO vo);
}
