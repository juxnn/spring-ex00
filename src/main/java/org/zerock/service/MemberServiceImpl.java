package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.AuthVO;
import org.zerock.domain.MemberVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.FileMapper;
import org.zerock.mapper.MemberMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.Setter;

@Service
public class MemberServiceImpl implements MemberService{

	@Setter(onMethod_=@Autowired)
	private MemberMapper mapper;
	
	@Setter(onMethod_=@Autowired)
	private BoardMapper boardMapper;
	
	@Setter(onMethod_=@Autowired)
	private ReplyMapper replyMapper;
	
	@Setter(onMethod_=@Autowired)
	private FileMapper fileMapper;
	
	
	@Setter(onMethod_=@Autowired)
	private PasswordEncoder encoder;
	
	@Override
	@Transactional
	public boolean insert(MemberVO vo) {
		
		vo.setUserpw(encoder.encode(vo.getUserpw()));		
		int cnt = mapper.insert(vo);
		
		AuthVO avo = new AuthVO();
		avo.setUserid(vo.getUserid());
		avo.setAuth("ROLE_USER");
		mapper.insertAuth(avo);
		return cnt == 1;
		
	}
	
	@Override
	public MemberVO read(String name) {
		
		return mapper.read(name);
	}
	
	@Override
	public boolean modify(MemberVO vo) {
		vo.setUserpw(encoder.encode(vo.getUserpw()));
		
		return mapper.update(vo) == 1;
	}
	
	@Override
	public boolean modify(MemberVO vo, String oldPassword) {
		MemberVO old = mapper.read(vo.getUserid());
		
		if(encoder.matches(oldPassword, old.getUserpw())) {
			return modify(vo);
		}
		return false;
	}
	
	@Override
	@Transactional
	public boolean remove(MemberVO vo) {
		
		replyMapper.removeByUserid(vo);
		
		//본인 게시물의 다른 사람 댓글 삭제
		replyMapper.removeByOther(vo);
		
		fileMapper.removeByUserid(vo);
		
		boardMapper.removeByUserid(vo);
		
		//tbl_member_auth 삭제
		mapper.removeAuth(vo);
		
		//tbl_member 삭제
		int cnt = mapper.remove(vo);
		return cnt ==1;
	}
	
	@Override
	public boolean remove(MemberVO vo, String oldPassword) {
		MemberVO old = mapper.read(vo.getUserid());
		if(encoder.matches(oldPassword, old.getUserpw())) {
			return remove(vo);
		}
					
		return false;
	}
}
