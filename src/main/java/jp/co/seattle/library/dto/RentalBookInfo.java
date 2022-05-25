package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class RentalBookInfo {

	private int bookId;

	private String title;

	private String checkoutDate;

	private String returnDate;

	public RentalBookInfo() {

	}

}
