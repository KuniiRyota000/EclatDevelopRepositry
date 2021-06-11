package jp.co.sss.shop.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.repository.ItemRepository;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ItemShowCustomerController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @param pageable ページング情報
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model, Pageable pageable) {

		return "index";
	}

	@RequestMapping(path = "/item/list/category/1", method = RequestMethod.GET)
	public String itemListCategory1(String categoryId) {
		return "item_search_result";
	}

	@RequestMapping(path = "/item/list/category/{sortType}", method = RequestMethod.GET)
	public String itemListCategory(String categoryId) {
		return "item_search_result";
	}

	@RequestMapping(path = "/item/list/price", method = RequestMethod.GET)
	public String itemListPrice(Integer minPrice, Integer maxPrice) {
		return "item_search_result";
	}

	@RequestMapping(path = "/item/list/price/{sortType}", method = RequestMethod.GET)
	public String itemListPriceSortType(Integer minPrice, Integer maxPrice) {
		return "item_search_result";
	}

	@RequestMapping(path = "/item/detail")
	public String itemDetail() {
		return "item_detail";
	}

}
