package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.RentalBookInfo;

public class RentalBookRowMapper implements RowMapper<RentalBookInfo> {

	public RentalBookInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// Query結果（ResultSet rs）を、オブジェクトに格納する実装
		RentalBookInfo rentalbookInfo = new RentalBookInfo();

		rentalbookInfo.setBookId(rs.getInt("book_id"));
		rentalbookInfo.setTitle(rs.getString("title"));
		rentalbookInfo.setCheckoutDate(rs.getString("checkout_date"));
		rentalbookInfo.setReturnDate(rs.getString("return_date"));
		return rentalbookInfo;
	}

}
