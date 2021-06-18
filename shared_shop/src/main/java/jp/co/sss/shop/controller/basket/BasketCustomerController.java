package jp.co.sss.shop.controller.basket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
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
	public static OrderBean orderInfo=new OrderBean();

	/**
	 * 買い物かご表示
	 */
	@RequestMapping("/basket")
	public String showBasket() {
		session.setAttribute("basketList", basketList);
		return "basket/shopping_basket";
	}

	/**
	 * 買い物かごに商品を追加
	 */
	@RequestMapping("/basket/input/{itemId}")
	public String inputItem(@PathVariable int itemId) {

		BasketBean checkBasket=new BasketBean();
		int subtotal=0;

		/*
		 * 同一商品チェック
		 *
		 * 同一商品がある場合：注文数 +1
		 */
		if(!basketList.isEmpty()) {
		for(int i = 0; i < basketList.size(); i++) {
			checkBasket = basketList.get(i);

			if(checkBasket.getId() == itemId) {
				checkBasket.setOrderNum(checkBasket.getOrderNum()+1);
				subtotal = checkBasket.getPrice()*checkBasket.getOrderNum();
				checkBasket.setSubtotal(subtotal);
				basketList.set(i, checkBasket);
				session.setAttribute("basketList", basketList);

				return "redirect:/basket";
			}
		}
		}

		//買い物かごに商品追加
		Item item=itemRepository.getOne(itemId);
		BasketBean basket= new BasketBean();
		basket.setId(item.getId());
		basket.setName(item.getName());
		basket.setStock(item.getStock());
		basket.setOrderNum(basket.getOrderNum());
		basket.setImage(item.getImage());
		basket.setPrice(item.getPrice());
		subtotal = item.getPrice()*basket.getOrderNum();
		basket.setSubtotal(subtotal);

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
		int subtotal=copyBasket.getPrice()*copyBasket.getOrderNum();
		copyBasket.setSubtotal(subtotal);
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
		int subtotal=copyBasket.getPrice()*copyBasket.getOrderNum();
		copyBasket.setSubtotal(subtotal);
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

	/**
	 * お届け先入力画面へ遷移
	 */
	@RequestMapping("/order/regist/addressInput")
	public String orderAddressInput() {
		return "order/regist/order_address_input";
	}

	/**
	 * 注文情報セッションにお届け先入力情報登録 --> 支払方法選択画面にリダイレクト
	 */
	@RequestMapping(path="/regist/addressInputComplete", method = RequestMethod.POST)
	public String addressInputComplete(OrderForm form) {
		orderInfo.setPostalCode(form.getPostalCode());
		orderInfo.setAddress(form.getAddress());
		orderInfo.setName(form.getName());
		orderInfo.setPhoneNumber(form.getPhoneNumber());

		session.setAttribute("orderInfo", orderInfo);

		return "redirect:/order/regist/paymentInput";
	}

	/**
	 * 支払方法選択画面へ遷移
	 */
	@RequestMapping("/order/regist/paymentInput")
	public String orderPaymentInput() {
		return "order/regist/order_payment_input";
	}

	/**
	 * 注文情報セッションに支払方法入力情報登録 --> 注文確認選択画面にリダイレクト
	 */
	@RequestMapping(path="/regist/paymentInputComplete", method = RequestMethod.POST)
	public String paymentInputComplete(OrderForm form) {
		orderInfo.setPayMethod(form.getPayMethod());

		int total=0;
		for(int i = 0; i < basketList.size(); i++) {
			total += basketList.get(i).getSubtotal();
		}

		session.setAttribute("total", total);
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
	private String orderComplete() throws ParseException {
		Order order = new Order();
		OrderItem orderItem = new OrderItem();
		List<OrderItem> orderList = new ArrayList<OrderItem>();

		User user = (User) session.getAttribute("userInfo");

		order.setPostalCode(orderInfo.getPostalCode());
		order.setAddress(orderInfo.getAddress());
		order.setName(orderInfo.getName());
		order.setPhoneNumber(orderInfo.getPhoneNumber());
		order.setPayMethod(orderInfo.getPayMethod());

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = sdf.format(now);
		now = sdf.parse(nowDate);
		java.sql.Date sqlNowDate = new java.sql.Date(now.getTime());
		order.setInsertDate(sqlNowDate);

		order.setUser(user);

		for(int i = 0; i < basketList.size(); i++) {
			int itemId = basketList.get(i).getId();
			Item item = itemRepository.getOne(itemId);
			orderItem.setQuantity(basketList.get(i).getOrderNum());
			orderItem.setItem(item);
			orderItem.setPrice(basketList.get(i).getPrice());
			orderList.add(orderItem);
		}

		orderRepository.save(order);

		return "order/regist/order_complete";
	}
}
