package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.RentalBookInfo;
import jp.co.seattle.library.rowMapper.RentalBookRowMapper;

@Service
public class RentalsService {
	final static Logger logger = LoggerFactory.getLogger(RentalsService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 貸出情報を登録する
	 * 
	 * @param bookID 書籍ID
	 */
	public void rentalsbook(int bookId) {

		// SQL生成
		String sql = "insert into rentals (book_Id) values (" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を借りられるかチェックする
	 * 
	 * @param bookID 書籍ID
	 * @return rentID 貸出書籍の書籍ID
	 */
	public boolean getRentInfo(int bookId) {
		// SQL生成
		String sql = "select case when checkout_date is null then true else false "
				+ "end from rentals right join books on rentals.book_id = books.id where books.id=" + bookId;
		boolean checkout_date = jdbcTemplate.queryForObject(sql, boolean.class);
		return checkout_date;
	}

	/**
	 * 返却情報を更新する
	 * 
	 * @param bookID 書籍ID
	 */
	public void returnBook(int bookId) {
		// SQL生成
		String sql = "insert into rentals(book_id, return_date) values(" + bookId + ",now())"
				+ "on conflict (book_id) do update set checkout_date = null, return_date = now() ;";
		jdbcTemplate.update(sql);
	}

	/**
	 * 貸出情報を更新する
	 * 
	 * @parm bookId 書籍ID
	 */
	public void rentalsAgainBook(int bookId) {
		// SQL生成
		String sql = "insert into rentals(book_id, checkout_date) values(" + bookId + ",now())"
				+ "on conflict (book_id) do update set checkout_date = now(), return_date = null ;";
		jdbcTemplate.update(sql);
	}

	/**
	 * 貸出書籍情報を取得する
	 */
	public List<RentalBookInfo> historyList() {
		List<RentalBookInfo> historyList = jdbcTemplate.query(
				"select rentals.book_id ,books.title ,rentals.checkout_date ,rentals.return_date from "
				+ "rentals left join books on rentals.book_id = books.id",
				new RentalBookRowMapper());

		return historyList;
	}
}
