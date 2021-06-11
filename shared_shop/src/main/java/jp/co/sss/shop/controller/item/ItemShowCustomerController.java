package jp.co.sss.shop.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String index(Model model, Pageable pageable, Integer salesFigures) {
		model.addAttribute("items1", itemRepository.findBySalesFiguresOrderBySalesFiguresAsc(salesFigures, pageable));
		return "index";
	}

	/**新着一覧表示*/
	@RequestMapping(path = "/item/list/{sortType}")
	public String itemlist(int deleteFlag, Pageable pageable, Model model) {
		model.addAttribute("items", itemRepository.findByDeleteFlagOrderByInsertDateDesc(deleteFlag, pageable));
		return "item_list";
	}

	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @param pageable ページング情報
	 * @return "/" トップ画面へ
	 */
	//	@RequestMapping(path = "/item/list/category", method = RequestMethod.GET)
	//			public String itemListCategory1(String categoryId, Model mode, Pageable pageable) {
	//				Page<Item> pageList = itemRepository.
	//				List<Item> ItemList = pageList.getContent();
	//				model.addAttribute("pages", pageList);
	//				model.addAttribute("items", ItemList);
	//				return "";
	//			}
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
	//}

	//public String itemListCategory(String categoryId) {
	//	return "/item/item_search_result/item_search_result";
	//}
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
}
