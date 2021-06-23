package jp.co.sss.shop.controller.basket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

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
import jp.co.sss.shop.repository.UserRepository;

public class Basket {
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository oderItemRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	protected static List<BasketBean> basketList = new ArrayList<BasketBean>();
	protected static OrderBean orderInfo=new OrderBean();
	protected static Item staticItem = new Item();
	protected static int upperLimitFlag = 0;

	/**
	 * 注文情報をセット
	 */
	public Order setOrder(Order order, User userInfo) throws ParseException {
		order.setPostalCode(orderInfo.getPostalCode());
		order.setAddress(orderInfo.getAddress());
		order.setName(orderInfo.getName());
		order.setPhoneNumber(orderInfo.getPhoneNumber());
		order.setPayMethod(orderInfo.getPayMethod());
		java.sql.Date now = getSqlNowDate();
		order.setInsertDate(now);
		order.setUser(userInfo);

		return order;
	}

	/**
	 * sqlDateで現時刻を取得
	 */
	public java.sql.Date getSqlNowDate() throws ParseException{
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = sdf.format(now);
		now = sdf.parse(nowDate);
		java.sql.Date sqlNowDate = new java.sql.Date(now.getTime());

		return sqlNowDate;
	}

	/**
	 * basketList内の商品分orderListにorderItemを追加
	 */
	public List<OrderItem> setOrderList(Order orderUpdate) {
		List<OrderItem> orderList = new ArrayList<OrderItem>();

		for(int i = 0; i < basketList.size(); i++) {
			OrderItem orderItem = new OrderItem();
			BasketBean basketItem = basketList.get(i);
			int itemId = basketItem.getId();
			Item item = itemRepository.getOne(itemId);
			item.setSalesFigures(item.getSalesFigures() + basketItem.getOrderNum());
			item.setStock(item.getStock() - basketItem.getOrderNum());
			orderItem.setQuantity(basketItem.getOrderNum());
			orderItem.setOrder(orderUpdate);
			orderItem.setItem(item);
			orderItem.setPrice(basketItem.getPrice());
			orderList.add(orderItem);
			itemRepository.save(item);
			oderItemRepository.save(orderItem);
		}

		return orderList;
	}

	/**
	 * 買い物かごに商品を追加
	 */
	public void addBasket(Item item) {
		BasketBean basket= new BasketBean();
		basket.setId(item.getId());
		basket.setName(item.getName());
		basket.setStock(item.getStock());
		basket.setOrderNum(basket.getOrderNum());
		basket.setImage(item.getImage());
		basket.setPrice(item.getPrice());
		int subtotal = item.getPrice()*basket.getOrderNum();
		basket.setSubtotal(subtotal);

		basketList.add(basket);

		session.setAttribute("basketList", basketList);
	}

	/**
	 * 商品の購入数 +1
	 */
	public void addOrderNum(BasketBean basketItem, int index) {
		basketItem.setOrderNum(basketItem.getOrderNum() +1);
		int subtotal=basketItem.getPrice()*basketItem.getOrderNum();
		basketItem.setSubtotal(subtotal);
		basketList.set(index, basketItem);
		session.setAttribute("basketList", basketList);
		upperLimitFlag = 0;
	}

	/**
	 * orderFormにuserInfoのセッションを初期値としてセット
	 */
	public void setOrderForm(OrderForm form) {
		User userInfo = (User) session.getAttribute("userInfo");

		form.setPostalCode(userInfo.getPostalCode());
		form.setAddress(userInfo.getAddress());
		form.setName(userInfo.getName());
		form.setPhoneNumber(userInfo.getPhoneNumber());
		form.setUsePoint(0);

		session.setAttribute("orderFormParam", form);
	}

	/**
	 * orderFormにOrderFormの値を初期値としてセット
	 */
	public void setOrderForm(OrderForm form, OrderBean orderInfo) {
		form.setPostalCode(orderInfo.getPostalCode());
		form.setAddress(orderInfo.getAddress());
		form.setName(orderInfo.getName());
		form.setPhoneNumber(orderInfo.getPhoneNumber());
		form.setUsePoint(0);

		session.setAttribute("orderFormParam", form);
	}

	/**
	 * setOrderInfoにOrderFormの値を初期値としてセット
	 */
	public void setOrderInfo(OrderForm form, OrderBean orderInfo) {
		orderInfo.setPostalCode(form.getPostalCode());
		orderInfo.setAddress(form.getAddress());
		orderInfo.setName(form.getName());
		orderInfo.setPhoneNumber(form.getPhoneNumber());

		session.setAttribute("orderInfo", orderInfo);
	}

	/**
	 * 合計金額を計算
	 * その後セッションにセット
	 */
	public void calcTotal() {
		int total=0;
		for(int i = 0; i < basketList.size(); i++) {
			total += basketList.get(i).getSubtotal();
		}

		total -= orderInfo.getUsePoint();

		session.setAttribute("total", total);
	}
}
