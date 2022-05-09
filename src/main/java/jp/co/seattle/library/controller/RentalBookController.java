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
public class RentalBookController {
	final static Logger logger = LoggerFactory.getLogger(RentalsService.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private RentalsService rentalsService;

	/**
	 * 書籍を借りる
	 * 
	 * @param bookid 書籍ID
	 * @param model  モデル情報
	 */
	@RequestMapping(value = "/rentalBook", method = RequestMethod.POST)
	public String rentalsbook(Locale locale, @RequestParam("bookId") int bookId, Model model) {
		logger.info("Welcome rental! The client locale is {}.", locale);

		// 書籍IDチェック
		if (rentalsService.getRentInfo(bookId) == 0) {
			rentalsService.rentalsbook(bookId);
		} else {
			model.addAttribute("errorMessage_rent", "貸出し済みです。");
		}

		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}

}
