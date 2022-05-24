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

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class SearchBookController {
	final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 書籍情報を検索する
	 * 
	 * @param search   検索キーワード
	 * @param radiobtn ラジオボタン
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String searchBook(Locale locale, @RequestParam("search") String search,
			@RequestParam("radiobtn") int radiobtn, Model model) {
		logger.info("Welcome searchBook.java! The client locale is {}.", locale);
		// 完全一致「と一致する」を選択した時
		if (radiobtn == 1) {
			// 検索結果がない時
			if (booksService.perfectsearchList(search).isEmpty()) {
				model.addAttribute("errorMessage_search", "「" + search + "」と一致する検索結果がありません。");
				// 検索結果がある時
			} else {
				model.addAttribute("bookList", booksService.perfectsearchList(search));
			}
			// 部分一致「を含む」を選択した時
		} else {
			// 検索結果がない時
			if (booksService.searchBookList(search).isEmpty()) {
				model.addAttribute("errorMessage_search", "「" + search + "」を含む検索結果がありません。");
				// 検索結果がある時
			} else {
				model.addAttribute("bookList", booksService.searchBookList(search));
			}
		}
		return "home";
	}
}
