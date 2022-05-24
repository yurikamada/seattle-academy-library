package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentalsService;

/**
 * 貸出履歴コントローラー
 */
@Controller // APIの入り口
public class HistoryBookController {
	// TODO 自動生成されたメソッド・スタブ
	final static Logger logger = LoggerFactory.getLogger(HistoryBookController.class);

	@Autowired
	private RentalsService rentalsService;

	@RequestMapping(value = "/historyBook", method = RequestMethod.POST) // value＝actionで指定したパラメータ
	public String historyBook(Locale locale, Model model) {
		logger.info("Welcome history! The client locale is {}.", locale);
		model.addAttribute("historyList", rentalsService.historyList());
		// 履歴画面に遷移する
		return "historyBook";
	}
}
