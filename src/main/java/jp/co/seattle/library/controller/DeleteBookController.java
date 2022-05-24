package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalsService;

/**
 * 削除コントローラー
 */
@Controller // APIの入り口
public class DeleteBookController {
	final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private RentalsService rentalsService;

	/**
	 * 対象書籍を削除する
	 *
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model  モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/deleteBook", method = RequestMethod.POST)
	public String deleteBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		logger.info("Welcome delete! The client locale is {}.", locale);
		boolean checkout_date = rentalsService.getRentInfo(bookId);
		// 対象書籍が貸出されていない時
		if (checkout_date) {
			booksService.deleteBook(bookId);
			booksService.deleteBook_rental(bookId);
			// 本の情報を取得して画面側に渡す
			model.addAttribute("bookList", booksService.getBookList());
			return "home";
			// 対象書籍が貸出されている時
		} else {
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
			model.addAttribute("errorMessage_rent_return", "貸出し中のため書籍を削除できません。");
			return "details";
		}

	}

}
