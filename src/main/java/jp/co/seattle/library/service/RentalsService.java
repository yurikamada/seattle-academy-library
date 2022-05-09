package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
		String sql = "INSERT INTO rentals (book_id) VALUES (" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	/**
	 * 貸出書籍の情報取得
	 * 
	 * @param bookID 書籍ID
	 */
	public int getRentInfo(int bookId) {
		// SQL生成
		String sql = "select book_id from rentals where book_id =" + bookId;

		try {
			int rentid = jdbcTemplate.queryForObject(sql, Integer.class);
			return rentid;
			// データがない時の例外処理
		} catch (Exception e) {
			return 0;
		}

	}

}
