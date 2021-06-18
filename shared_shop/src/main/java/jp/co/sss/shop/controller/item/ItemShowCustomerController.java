package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.util.BeanCopy;
import jp.co.sss.shop.util.Constant;

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
		Page<Item> pageList = itemRepository.findAllByOrderBySalesFiguresDesc(pageable);
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(pageList.getContent());
		model.addAttribute("pages", pageList);
		model.addAttribute("items", itemBeanList);
		return "index";
	}

	/**新着一覧表示*/
	@RequestMapping(path = "/item/list/1")
	public String itemlist(Pageable pageable, Model model) {
		Page<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDesc(Constant.NOT_DELETED, pageable);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList.getContent());

		// 商品情報をViewへ渡す
		model.addAttribute("pages", itemList);
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list");

		return "item/list/item_list";
	}

	@RequestMapping(path = "/item/list/2")
	public String itemByList(Pageable pageable, Model model) {
		Page<Item> itemList = itemRepository.findByDeleteFlagOrderBySalesFiguresDesc(Constant.NOT_DELETED, pageable);

		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList.getContent());

		// 商品情報をViewへ渡す
		model.addAttribute("pages", itemList);
		model.addAttribute("items", itemBeanList);
		model.addAttribute("url", "/item/list");
		return "item/list/item_list_2";
	}
}
/**
 * トップ画面 表示処理
 *
 * @param model    Viewとの値受渡し
 * @param pageable ページング情報
 * @return "/" トップ画面へ
 */
//		@RequestMapping(path = "/item/list/category", method = RequestMethod.GET)
//				public String itemListCategory1(String categoryId, Model model, Pageable pageable) {
//					Page<Item> pageList = itemRepository.
//					List<Item> ItemList = pageList.getContent();
//					model.addAttribute("pages", pageList);
//					model.addAttribute("items", ItemList);
//					return "";
//				}
//
//	@RequestMapping("/item/list/category/1")
//	public String itemListCategory(Integer categoryId, Model model) {
//		Category category = new Category();
//		category.setId(categoryId);
//		List<Item> items = itemRepository.findById(category);
//		// エンティティ内の検索結果をJavaBeansにコピー
//		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(items);
//		model.addAttribute("category", items);
//		model.addAttribute("items", itemBeanList);
//		return "item/item_search_result/item_list_1";
//	}
//
//	@RequestMapping(path = "/item/list/category/{sortType}", method = RequestMethod.GET)
//	public String itemListCategory(Integer categoryId, Model model) {
//		Category category = new Category();
//		category.setId(categoryId);
//		List<Item> item = itemRepository.findById(category);
//		// エンティティ内の検索結果をJavaBeansにコピー
//		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(item);
//		model.addAttribute("item", itemBeanList);
//		return "item/item_search_result/item_list_1";
//	}
//
//	public String itemListCategory(String categoryId) {
//		return "/item/item_search_result/item_search_result";
//	}
//
//	@RequestMapping(path = "/item/list/price", method = RequestMethod.GET)
//	public String itemListPrice(Integer minPrice, Integer maxPrice, Integer price) {
//		return "/item/item_search_result/item_search_result";
//	}
//
//	@RequestMapping(path = "/item/list/price/{sortType}", method = RequestMethod.GET)
//	public String itemListPriceSortType(Integer minPrice, Integer maxPrice, Integer price) {
//		return "/item/item_search_result/item_search_result";
//	}
//
//	@RequestMapping(path = "/item/detail")
//	public String itemDetail() {
//		return "/item/item_search_result/item_search_result";
//	}
//
//}
