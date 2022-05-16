package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.BookDetailsInfo;

@Configuration
public class BookDetailsInfoRowMapper implements RowMapper<BookDetailsInfo> {

    @Override
    public BookDetailsInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        BookDetailsInfo bookDetailsInfo = new BookDetailsInfo();

        bookDetailsInfo.setBookId(rs.getInt("id"));
        bookDetailsInfo.setTitle(rs.getString("title"));
        bookDetailsInfo.setAuthor(rs.getString("author"));
        bookDetailsInfo.setPublisher(rs.getString("publisher"));
        bookDetailsInfo.setThumbnailName(rs.getString("thumbnail_name"));
        bookDetailsInfo.setThumbnailUrl(rs.getString("thumbnail_url"));
        bookDetailsInfo.setPublishDate(rs.getString("publish_date"));
        bookDetailsInfo.setIsbn(rs.getString("isbn"));
        bookDetailsInfo.setExplanatory_text(rs.getString("explanatory_text"));
        bookDetailsInfo.setRentId(rs.getInt("book_id"));
        bookDetailsInfo.setStatus(rs.getString("status"));
        return bookDetailsInfo;
    }

}