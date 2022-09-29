package com.kim.biz.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.kim.biz.board.BoardService;
import com.kim.biz.board.BoardVO;
import com.kim.biz.board.impl.BoardDAO;
import com.kim.biz.member.MemberVO;
import com.kim.biz.member.impl.MemberDAO;

@Controller
@SessionAttributes("data") // "data"��� �̸��� �����Ͱ� Model ��ü�� �����̵ȴٸ�, �װ��� session�� ����Ű�ڴ�.
public class BoardController {
	
	@Autowired
	private BoardService boardService; // ����Ͻ� ������Ʈ. DAO�� ���� �̿� xxx

	@ModelAttribute("scMap")
	public Map<String,String> searchConditionMap(){
		Map<String,String> scMap=new HashMap<String,String>();
		scMap.put("����", "TITLE");
		scMap.put("�ۼ���", "WRITER");
		return scMap;
	}
	
	@RequestMapping("/main.do")
	public String selectAllBoard(
			@RequestParam(value="searchCondition",defaultValue="TITLE",required=false)String searchCondition,
			@RequestParam(value="searchContent",defaultValue="",required=false)String searchContent,
			BoardVO vo, Model model) {
		System.out.println("�˻�����: "+searchCondition);
		System.out.println("�˻���: "+searchContent);

		vo.setTitle(searchCondition);
		vo.setContent(searchContent);
		
		List<BoardVO> datas=boardService.selectAllBoard(vo);
		System.out.println("main.do �α� datas :"+datas);
		model.addAttribute("datas", datas);
		
		return "main.jsp";
	}
	
	@RequestMapping("/board.do")
	public String board(BoardVO bVO,Model model) {
		bVO=boardService.selectOneBoard(bVO);

		model.addAttribute("data", bVO);
		return "board.jsp";
	}
	
	@RequestMapping("/update.do")
	public String update(@ModelAttribute("data")BoardVO bVO) {
		System.out.println("updateB.do �α�: "+bVO); // @SA,@MA ���п� writer ���� ���� �α׿� ��µȴ�.
		
		MultipartFile uploadFile=bVO.getUploadFile();
		if(!uploadFile.isEmpty()) { // ���ε��� ���� ���翩�� Ȯ��
			String fileName=uploadFile.getOriginalFilename(); // ���ε��� ���ϸ�
			bVO.setBimg(fileName);
			try {
				uploadFile.transferTo(new File("C:\\0607JUNG\\workspace2\\test9\\src\\main\\webapp\\images\\"+fileName));
				// ���ε��� ������ ������ ��ο� ����
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		boardService.updateBoard(bVO);
		return "redirect:main.do";
	}
	@RequestMapping("/insertB.do")
	public String insert(BoardVO bVO) {
		System.out.println("insert.do �α� : "+ bVO);
		
		MultipartFile uploadFile=bVO.getUploadFile();
		if(!uploadFile.isEmpty()) { // ���ε��� ���� ���翩�� Ȯ��
			String fileName=uploadFile.getOriginalFilename(); // ���ε��� ���ϸ�
			bVO.setBimg(fileName);
			try {
				uploadFile.transferTo(new File("C:\\0607JUNG\\workspace2\\test9\\src\\main\\webapp\\images\\"+fileName));
				// ���ε��� ������ ������ ��ο� ����
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		boardService.insertBoard(bVO);
		return "redirect:main.do";
	}
	@RequestMapping("/delete.do")
	public String delete(BoardVO bVO) {
		boardService.deleteBoard(bVO);
		return "redirect:main.do";
	}

	/*
	@Override
	public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
		BoardVO bVO=new BoardVO();
		bVO.setBid(Integer.parseInt(request.getParameter("bid")));
		
		BoardDAO bDAO=new BoardDAO();
		bVO=bDAO.selectOneBoard(bVO);
		
		HttpSession session=request.getSession();
		session.setAttribute("data", bVO);
	
		return "board";
	}
	 */
	
}
