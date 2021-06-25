package jp.co.sss.shop.controller.basket;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;

@Controller
public class BasketCustomerController extends Basket{
	/**
	 * 買い物かご表示
	 */
	@RequestMapping("/basket")
	public String showBasket() {
		if(upperLimitFlag == 1) {
			System.out.println(staticItem.getName());
			session.setAttribute("itemName", staticItem.getName());
		}else {
			session.removeAttribute("itemName");
		}

		session.setAttribute("basketList", basketList);
		upperLimitFlag = 0;
		return "basket/shopping_basket";
	}

	/**
	 * 買い物かごに商品を追加
	 */
	@RequestMapping("/basket/input/{itemId}")
	public String inputItem(@PathVariable int itemId) {

		BasketBean basketItem=new BasketBean();

		//買い物かごに同じ商品がある場合：注文数 +1
		if(!basketList.isEmpty()) {
		for(int i = 0; i < basketList.size(); i++) {
			basketItem = basketList.get(i);

			if(basketItem.getId() == itemId && basketItem.getOrderNum() < basketItem.getStock()) {
				addOrderNum(basketItem, i);

				return "redirect:/basket";
			}
		}
		}

		Item item=itemRepository.getOne(itemId);

		//在庫なしなら追加しない
		if(item.getStock() <= 0) {
			staticItem = itemRepository.getOne(itemId);
			upperLimitFlag = 1;
			return "redirect:/basket";
		}

		//買い物かごのに商品を追加
		addBasket(item);

		return "redirect:/basket";
	}

	/**
	 * 商品の購入数 +1
	 */
	@RequestMapping("/basket/addOrderNum/{index}")
	public String addOrderNum(@PathVariable int index, Model model) {
		BasketBean basketItem = basketList.get(index);

		if(basketItem.getOrderNum() < basketItem.getStock()) {
			addOrderNum(basketItem, index);
		}else {
			upperLimitFlag = 1;
			staticItem = itemRepository.getOne(basketItem.getId());
			return "redirect:/basket";
		}

		return "redirect:/basket";
	}

	/**
	 * 商品の購入数 -1
	 */
	@RequestMapping("/basket/subOrderNum/{index}")
	public String subOrderNum(@PathVariable int index) {
		BasketBean basketItem=basketList.get(index);

		if(basketItem.getOrderNum() >= 2) {
			subOrderNum(basketItem, index);
		}else {
			deleteItem(index);
		}

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

	/**
	 * お届け先入力画面へ遷移
	 */
	@RequestMapping("/order/regist/addressInput")
	public String orderAddressInput(@ModelAttribute OrderForm form) {
		if(session.getAttribute("orderFormParam") ==  null) {
			setOrderForm(form);

			return "order/regist/order_address_input";
		}else {
			setOrderForm(form, orderInfo);

			return "order/regist/order_address_input";
		}
	}

	/**
	 * お届け先入力画面へ戻る
	 */
	@RequestMapping(path="/order/regist/addressInput/back", method = RequestMethod.POST)
	public String backrderAddressInput(OrderForm form) {
		return orderAddressInput(form);
	}

	/**
	 * 注文情報セッションにお届け先入力情報登録 --> 支払方法選択画面にリダイレクト
	 */
	@RequestMapping(path="/regist/addressInputComplete", method = RequestMethod.POST)
	public String addressInputComplete(@Valid @ModelAttribute OrderForm form, BindingResult result) {
		if(result.hasErrors()) {
			return orderAddressInput(form);
		}

		setOrderInfo(form, orderInfo);

		return "redirect:/order/regist/paymentInput";
	}

	/**
	 * 支払方法選択画面へ遷移
	 */
	@RequestMapping("/order/regist/paymentInput")
	public String orderPaymentInput(@ModelAttribute OrderForm form) {
		setOrderForm(form, orderInfo);

		return "order/regist/order_payment_input";
	}

	/**
	 * 注文情報セッションに支払方法入力情報登録 --> 注文確認画面にリダイレクト
	 */
	@RequestMapping(path="/regist/paymentInputComplete", method = RequestMethod.POST)
	public String paymentInputComplete(@Valid @ModelAttribute OrderForm form, BindingResult result, Model model) {
		User userInfo = (User) session.getAttribute("userInfo");

		if(form.getUsePoint() == null) {
			form.setUsePoint(0);
		}else if(form.getUsePoint() > userInfo.getPoint()) {
			model.addAttribute("overUsePoint", "保持しているポイントを超えて指定することはできません。");
			return orderPaymentInput(form);
		}

		if(result.hasErrors()) {
			return orderPaymentInput(form);
		}


		orderInfo.setPayMethod(form.getPayMethod());
		orderInfo.setUsePoint(form.getUsePoint());

		calcTotal();

		session.setAttribute("orderInfo", orderInfo);
		return "redirect:/order/regist/check";
	}


	/**
	 * 注文確認画面へ遷移
	 */
	@RequestMapping("/order/regist/check")
	public String orderCheck() {
		return "order/regist/order_check";
	}

	/**
	 * 注文完了
	 * @throws ParseException
	 */
	@RequestMapping("/order/regist/complete")
	private String orderComplete(Model model) throws ParseException {
		Order order = new Order();
		List<OrderItem> orderList = new ArrayList<OrderItem>();
		User user = (User) session.getAttribute("userInfo");

		//合計金額の取得
		int total = (int) session.getAttribute("total");

		//ポイント計算
		int getPoint = (total - orderInfo.getUsePoint()) / 100;
		user.setPoint(user.getPoint() - orderInfo.getUsePoint());
		int totalPoint = getPoint + user.getPoint();

		//ポイントのセット
		user.setPoint(totalPoint);
		order.setGotPoint(getPoint);
		order.setUsedPoint(orderInfo.getUsePoint());

		//userをusersテーブルに保存
		int userId = user.getId();
		userRepository.save(user);

		//保存したuserを取得
		User userInfo = userRepository.getOne(userId);

		//orderをordersテーブルに保存
		order = setOrder(order, userInfo);
		orderRepository.save(order);

		//取得したいレコードのorderId = orderIdListのサイズ
		List<Integer> orderIdList = orderRepository.findIdByAll();
		int orderId = orderIdList.size();
		Order orderUpdate = orderRepository.getOne(orderId);

		orderList = setOrderList(orderUpdate);
		orderUpdate.setOrderItemsList(orderList);
		orderRepository.save(orderUpdate);

		model.addAttribute("gotPoint", orderUpdate.getGotPoint());

		basketList.clear();
		session.removeAttribute("basketList");
		session.removeAttribute("orderInfo");
		session.removeAttribute("total");

		return "order/regist/order_complete";
	}
}
