package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.util.BeanCopy;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class IwataController {
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
	//カテゴリ別検索
	@RequestMapping(path = "/item/list/category/1", method = RequestMethod.GET)
	public String itemListCategory1(Integer categoryId, Model model, Pageable pageable) {
		Page<Item> itemList = itemRepository.findByDeleteFlagCategoryIdOrderByInsertDateDesc(categoryId, pageable);
		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList.getContent());
		model.addAttribute("pages", itemList);
		model.addAttribute("items", itemBeanList);
		model.addAttribute("selectedCategory", categoryId);
		return "item/list/item_list";
	}

	//価格帯
	@RequestMapping(path = "/item/list/price/1", method = RequestMethod.GET) //delet
	public String itemListPrice(Item item, String min, String max, Model model, Pageable pageable) {
		if (max.isEmpty()) {
			max = min;
			return "item/list/item_list";
		} else if (min.isEmpty()) {
			min = "0";
		}
		Integer minPrice = Integer.valueOf(min);
		Integer maxPrice = Integer.valueOf(max);

		Page<Item> itemList = itemRepository.findByPriceBetween(minPrice, maxPrice, pageable);
		// エンティティ内の検索結果をJavaBeansにコピー
		List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList.getContent());
		model.addAttribute("pages", itemList);
		model.addAttribute("items", itemBeanList);
		return "item/list/item_list";
	}

	//商品詳細
	@RequestMapping(path = "/item/detail/{id}")
	public String showItem(@PathVariable int id, Model model) {

		// 商品IDに該当する商品情報を取得
		Item item = itemRepository.findById(id).orElse(null);

		ItemBean itemBean = new ItemBean();

		// Itemエンティティの各フィールドの値をItemBeanにコピー
		BeanUtils.copyProperties(item, itemBean);

		// 商品情報にカテゴリ名を設定
		itemBean.setCategoryName(item.getCategory().getName());

		// 商品情報をViewへ渡す
		model.addAttribute("item", itemBean);

		return "item/detail/item_detail";
	}
}
