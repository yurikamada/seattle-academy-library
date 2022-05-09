package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class BulkBooksController {
	final static Logger logger = LoggerFactory.getLogger(BulkBooksController.class);

	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/bulkBook", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	public String login(Model model) {

		return "bulkBook";
	}

	// RequestParamでname属性を取得
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(Locale locale, @RequestParam("upload_file") MultipartFile uploadFile, Model model) {
		String Line = null;
		int count = 0;
		List<String[]> booksList = new ArrayList<String[]>();
		List<String> errorLine = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(uploadFile.getInputStream(), StandardCharsets.UTF_8))) {

			// CSVファイルに書籍情報があるか
			if (!br.ready()) {
				model.addAttribute("errorMessage_csv", "csvに書籍情報がありません。");
				return "bulkBook";
			} else {
				while ((Line = br.readLine()) != null) {
					// 行数のカウント
					count++;
					final String[] split = Line.split(",", -1);			
					// バリデーションチェック
					// 必須項目が入力されているかどうか

					boolean brank = split[0].isEmpty() || split[1].isEmpty() || split[2].isEmpty()
							|| split[3].isEmpty();
					
					// 出版日の形式チェック
					boolean date = split[3].length() != 8 && !(split[3].matches("^[0-9]$"));

					// isbnが入力されているか
					boolean isbn_check = !split[4].isEmpty() && !split[4].matches("^[0-9]{10}|[0-9]{13}$");					

					if (brank || date || isbn_check) {
						errorLine.add("<p>"+count + "行目の書籍登録時にエラーが発生しました。"+"</p>");
					}

					if (!brank || !date || !isbn_check) {
						booksList.add(split);
					}
				}
			}

		} catch (IOException e) {
			model.addAttribute("errorMessage_csv", "例外が発生しました。");
			return "bulkBook";
		}

		if (errorLine.size() > 0) {
			model.addAttribute("errorMessage_csv", errorLine);
			return "bulkBook";
		} else {
			String[] bookList;
			BookDetailsInfo bookInfo = new BookDetailsInfo();

			// 書籍の一括登録
			for (int i = 0; i < booksList.size(); i++) {
				bookList = booksList.get(i);

				for (int a = 0; a < bookList.length; a++) {

					bookInfo.setTitle(bookList[0]);
					bookInfo.setAuthor(bookList[1]);
					bookInfo.setPublisher(bookList[2]);
					bookInfo.setPublishDate(bookList[3]);
					bookInfo.setIsbn(bookList[4]);
					bookInfo.setExplanatory_text(bookList[5]);
				}
				booksService.registBook(bookInfo);

			}
			model.addAttribute("bookList", booksService.getBookList());
			return "home";
		}

	}
}
