package jp.co.sss.shop.controller.basket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;

@Controller
public class BasketCustomerController {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository oderItemRepository;

	/**
	 * 買い物かごに商品を追加
	 *
	 */
	@RequestMapping("/basket/input/{itemId}")
	public String inputItem() {

		return "redirect:/basket";
	}

	/**
	 * 買い物かご表示
	 */
	@RequestMapping("/basket")
	public String showBasket() {

		return "basket/shopping_basket";
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
