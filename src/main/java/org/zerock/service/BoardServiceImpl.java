package org.zerock.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.FileVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.FileMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.Setter;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.profiles.ProfileFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

//@AllArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{
	
	private String bucketName;
	private String profileName;
	private S3Client s3;
	
	@Setter(onMethod_=@Autowired)
	private BoardMapper mapper;
	
	@Setter(onMethod_=@Autowired)
	private ReplyMapper replyMapper;
	
	@Setter(onMethod_=@Autowired)
	private FileMapper fileMapper;
	
	public BoardServiceImpl() {
		this.bucketName = "juxn1";
		this.profileName = "spring1";	
		/*  
		 * create
		 *  /home/tomcat/.aws/credentials
		 */
		
		Path contentLocation = new File(System.getProperty("user.home") + "/.aws/credentials").toPath();
		ProfileFile pf = ProfileFile.builder()
				.content(contentLocation)
				.type(ProfileFile.Type.CREDENTIALS)
				.build();
		ProfileCredentialsProvider pcp = ProfileCredentialsProvider.builder()
				.profileFile(pf)
				.profileName(profileName)
				.build();
		
		this.s3 = S3Client.builder()
				.credentialsProvider(pcp)
				.build();
		
	}

//	@Autowired
//	public BoardServiceImpl(BoardMapper mapper) {
//		this.mapper = mapper;
//	}
	
	@Override
	public void register(BoardVO board) {
		mapper.insertSelectKey(board);
	}
	
	@Override
	@Transactional
	public void register(BoardVO board, MultipartFile file) {
		register(board);
		
		if(file!= null && file.getSize()>0) {
			FileVO vo = new FileVO();
			vo.setBno(board.getBno());
			vo.setFileName(file.getOriginalFilename());
			
			fileMapper.insert(vo);
			upload(board, file);
		}
	}
	
	
	private void upload(BoardVO board, MultipartFile file) {
		
		try(InputStream is = file.getInputStream()){
		
		PutObjectRequest objectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(board.getBno() + "/" + file.getOriginalFilename())
				.contentType(file.getContentType())
				.acl(ObjectCannedACL.PUBLIC_READ)
				.build();
		
		s3.putObject(objectRequest,
				RequestBody.fromInputStream(is, file.getSize()));
		
		} catch	(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BoardVO get(Long bno) {
		return mapper.read(bno);
	}
	
	@Override
	public boolean modify(BoardVO board) {
		return mapper.update(board) == 1;
	}
	
	@Override
	@Transactional
	public boolean modify(BoardVO board, MultipartFile file, String fileCheck) {
		if(file != null & file.getSize()>0) {
			//s3 ?????? ??? ??? ?????????
			BoardVO oldBoard = mapper.read(board.getBno());
			removeFile(oldBoard);
			upload(board, file);
			//tbl_board_file??? ?????? ??? ?????????
			fileMapper.deleteByBno(board.getBno());
			
			FileVO vo = new FileVO();
			vo.setBno(board.getBno());
			vo.setFileName(file.getOriginalFilename());
			fileMapper.insert(vo);			
		}
		if(fileCheck.equals("delete")) {
			//????????????(s3)
			BoardVO vo = mapper.read(board.getBno());
			removeFile(vo);
			//????????????(db)
			fileMapper.deleteByBno(board.getBno());
		}else {
			modify(board);						
		}
		return false;
	}
	
	
	@Override
	@Transactional
	public boolean remove(Long bno) {
		
		//?????? ??????
		replyMapper.deleteByBno(bno);
		
		//?????? ??????(s3)
		BoardVO vo = mapper.read(bno);
		removeFile(vo);
		
		//?????? ??????(db)
		fileMapper.deleteByBno(bno);
		
		
		//????????? ??????
		int cnt = mapper.delete(bno);
		
		
		return cnt == 1;
	}
	/*	
	@Override
	public List<BoardVO> getList() {
		return mapper.getList();
	}
 	*/
	
	private void removeFile(BoardVO vo) {
		
		String key = vo.getBno() + "/" + vo.getFileName();
		
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.deleteObject(deleteObjectRequest);
		
	}

	@Override
	public List<BoardVO> getList(Criteria cri){
		return mapper.getListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criteria cri) {
		return mapper.getTotalCount(cri);
	}
	

}
