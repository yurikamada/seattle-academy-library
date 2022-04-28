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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * 編集コントローラー
 */
@Controller // APIの入り口
public class EditBookController {
	final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 対象書籍を編集する
	 *
	 * @param locale ロケール情報
	 * @param bookId 書籍ID
	 * @param model  モデル情報
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/editBook", method = RequestMethod.POST)
	public String editBook(Locale locale, @RequestParam("bookId") int bookId, Model model) {
		logger.info("Welcome edit! The client locale is {}.", locale);
		booksService.getBookInfo(bookId);
		// 書籍の詳細情報を表示する
		model.addAttribute("bookInfo", booksService.getBookInfo(bookId));
		// 編集画面に遷移する
		return "editBook";
	}

	@Transactional
	@RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String insertBook(Locale locale, @RequestParam("bookId") int bookId,@RequestParam("title") String title, @RequestParam("author") String author,
			@RequestParam("publisher") String publisher, @RequestParam("publishDate") String publishDate,
			@RequestParam("isbn") String isbn, @RequestParam("explanatory_text") String explanatory_text,
			@RequestParam("thumbnail") MultipartFile file, Model model) {
		logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

		// パラメータで受け取った書籍情報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		bookInfo.setBookId(bookId);
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setPublishDate(publishDate);
		bookInfo.setIsbn(isbn);
		bookInfo.setExplanatory_text(explanatory_text);

		// バリデーションチェック
		boolean brank = title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty();
		boolean date = publishDate.length() != 8 && !(publishDate.matches("^[0-9]$"));
		boolean isbn_check = !isbn.isEmpty() && !isbn.matches("^[0-9]{10}|[0-9]{13}$");

		// 必須項目が入力されているかどうか
		if (brank) {
			model.addAttribute("errorMessage_brank", "必須項目が未入力です。");
		}

		// 出版日の形式チェック
		if (date) {
			model.addAttribute("errorMessage_date", "出版日は半角数字のYYYYMMDD形式で入力してください。");
		}

		// isbnが入力されているか
		if (isbn_check) {
			model.addAttribute("errorMessage_isbn", "ISBNの桁数または半角数字が正しくありません。");
		}

		if (brank || date || isbn_check) {
			model.addAttribute("bookInfo", bookInfo);
			return "editBook";
		}

		// 書籍情報を新規登録する
		booksService.updateBook(bookInfo);

		model.addAttribute("resultMessage", "登録完了");
		// TODO 登録した書籍の詳細情報を表示するように実装
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		// 詳細画面に遷移する
		return "details";

	}

}
