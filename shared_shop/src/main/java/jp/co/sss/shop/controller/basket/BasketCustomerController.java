package jp.co.sss.shop.controller.basket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;

@Controller
public class BasketCustomerController {
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository oderItemRepository;

	@Autowired
	HttpSession session;

	public static List<BasketBean> basketList = new ArrayList<BasketBean>();

	/**
	 * 買い物かご表示
	 */
	@RequestMapping("/basket")
	public String showBasket() {
		return "basket/shopping_basket";
	}

	/**
	 * 買い物かごに商品を追加
	 */
	@RequestMapping("/basket/input/{itemId}")
	public String inputItem(@PathVariable int itemId) {

		//同一商品チェック用
		BasketBean checkBasket=new BasketBean();

		/*
		 * 同一商品チェック
		 *
		 * 同一商品がある場合：注文数 +1
		 */
		for(int i = 0; i <= basketList.size(); i++) {
			if(checkBasket.getId() == itemId) {
				checkBasket.setOrderNum(checkBasket.getOrderNum()+1);
				basketList.set(i, checkBasket);
				session.setAttribute("basketList", basketList);

				return "redirect:/basket";
			}
		}

		//買い物かごに商品追加
		Item item=itemRepository.getOne(itemId);
		BasketBean basket= new BasketBean();
		basket.setId(item.getId());
		basket.setName(item.getName());
		basket.setStock(item.getStock());
		basket.setOrderNum(basket.getOrderNum());

		basketList.add(basket);

		session.setAttribute("basketList", basketList);

		return "redirect:/basket";
	}

	/**
	 * 商品の購入数 +1
	 */
	@RequestMapping("/basket/addOrderNum/{index}")
	public String addOrderNum(@PathVariable int index) {
		BasketBean copyBasket=basketList.get(index);

		copyBasket.setOrderNum(copyBasket.getOrderNum() +1);
		basketList.set(index, copyBasket);

		session.setAttribute("basketList", basketList);
		return "redirect:/basket";
	}

	/**
	 * 商品の購入数 -1
	 */
	@RequestMapping("/basket/subOrderNum/{index}")
	public String subOrderNum(@PathVariable int index) {
		BasketBean copyBasket=basketList.get(index);

		copyBasket.setOrderNum(copyBasket.getOrderNum() -1);
		basketList.set(index, copyBasket);

		session.setAttribute("basketList", basketList);
		return "redirect:/basket";
	}

	/**
	 * 商品単体の削除
	 */
	@RequestMapping("/basket/deleteItem/{index}")
	public String deleteItem(@PathVariable int index) {
		basketList.remove(index);

		session.setAttribute("basketList", basketList);
		return "redirect:/basket";
	}

	/**
	 * 全商品の削除
	 */
	@RequestMapping("/basket/deleteAllItem")
	public String deleteAllItem() {
		basketList.clear();

		session.setAttribute("basketList", basketList);
		return "redirect:/basket";
	}

	@RequestMapping("/order/regist/addressInput")
	public String orderAddressInput() {
		return "order/regist/order_address_input";
	}

	@RequestMapping("/order/regist/paymentInput")
	public String orderPaymentInput() {
		return "order/regist/order_payment_input";
	}

	@RequestMapping("/order/regist/check")
	public String orderCheck() {
		return "order/regist/order_check";
	}

	@RequestMapping("/order/regist/complete")
	private String orderComplete() {
		return "order/regist/order_complete";
	}
}
