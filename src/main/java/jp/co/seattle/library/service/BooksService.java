package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"select id,title,thumbnail_url,author,publisher,publish_date,isbn,explanatory_text from books order by title",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "select books.id ,books.title ,books.author ,books.publisher ,books.publish_date ,"
				+ " books.thumbnail_url ,books.thumbnail_name ,books.reg_date ,books.upd_date ,"
				+ " books.isbn ,books.explanatory_text ,rentals.book_id," + " case when checkout_date is not null then '貸出し中'"
				+ " else '貸出し可'" + " end as status"
				+ " from books left join rentals on books.id = rentals.book_id WHERE books.id = " + bookId + ";";

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,publish_date,isbn,explanatory_text,thumbnail_name,thumbnail_url,reg_date,upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getPublishDate() + "','" + bookInfo.getIsbn() + "','" + bookInfo.getExplanatory_text()
				+ "','" + bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl() + "'," + "'now()',"
				+ "'now()');";

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を削除する
	 * 
	 * @param bookId 書籍ID
	 */

	public void deleteBook(Integer bookId) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "DELETE FROM books WHERE id = " + bookId + ";";
		jdbcTemplate.update(sql);
	}
	
	public void deleteBook_rental(Integer bookId) {
		String sql = "DELETE FROM rentals WHERE book_id =" + bookId +";";
		jdbcTemplate.update(sql);
	}
	

	/**
	 * 最新の書籍IDを取得する
	 * 
	 * @return MaxId 最新の書籍ID
	 */
	public int MaxId() {
		String sql = "SELECT MAX(id) FROM books";
		int MaxId = jdbcTemplate.queryForObject(sql, int.class);
		return MaxId;
	}

	/**
	 * 書籍を編集する
	 * 
	 * @parm bookInfo 書籍情報
	 */
	public void updateBook(BookDetailsInfo bookInfo) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "UPDATE books SET title = '" + bookInfo.getTitle() + "'," + "author = '" + bookInfo.getAuthor()
				+ "'," + "publisher = '" + bookInfo.getPublisher() + "'," + "publish_date = '"
				+ bookInfo.getPublishDate() + "'," + "isbn = '" + bookInfo.getIsbn() + "'," + "explanatory_text = '"
				+ bookInfo.getExplanatory_text() + "'," + "upd_date = now() WHERE id ='" + bookInfo.getBookId() + "';";

		jdbcTemplate.update(sql);

	}

	/**
	 * 書籍を部分一致で検索する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> searchBookList(String search) {
		List<BookInfo> searchBookList = jdbcTemplate
				.query("select id ,title ,author ,publisher ,publish_date ,thumbnail_url from books where title like '%"
						+ search + "%'", new BookInfoRowMapper());

		return searchBookList;
	}

	/**
	 * 書籍を完全一致で検索する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> perfectsearchList(String search) {
		List<BookInfo> perfectsearchList = jdbcTemplate.query(
				"select id ,title ,author ,publisher ,publish_date ,thumbnail_url from books where title ='"+ search +"';",
				new BookInfoRowMapper());
		
		return perfectsearchList;
		
	}

}