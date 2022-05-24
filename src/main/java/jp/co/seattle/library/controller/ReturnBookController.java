package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalsService;

@Controller
public class ReturnBookController {
	final static Logger logger = LoggerFactory.getLogger(RentalsService.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private RentalsService rentalsService;

	/**
	 * 書籍を返却する
	 * 
	 * @param bookid 書籍ID
	 * @param model  モデル情報
	 */
	@RequestMapping(value = "/returnBook", method = RequestMethod.POST)
	public String returnbook(Locale locale, @RequestParam("bookId") int bookId, Model model) {
		logger.info("Welcome return! The client locale is {}.", locale);
		boolean checkout_date = rentalsService.getRentInfo(bookId);
		if (checkout_date) {
			model.addAttribute("errorMessage_rent_return", "貸出しされていません。");
		} 
		else {
			rentalsService.returnBook(bookId);
		}

		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
}