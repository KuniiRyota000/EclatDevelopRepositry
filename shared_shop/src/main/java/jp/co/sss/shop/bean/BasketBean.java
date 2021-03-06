package jp.co.sss.shop.bean;

/**
 * 買い物かご内の商品情報クラス
 *
 * @author SystemShared
 */

public class BasketBean {

	/**
	 * 商品ID
	 */
	private Integer	id;

	/**
	 * 商品名
	 */
	private String	name;

	/**
	 * 商品在庫数
	 */
	private Integer	 stock;

	/**
	 * 商品注文個数 初期値 1
	 */
	private Integer orderNum	= 1;

	/**
	 * 商品画像
	 */
	private String image;

	/**
	 * 単価
	 */
	private Integer	 price;

	/**
	 * 小計
	 */
	private Integer	 subtotal;

	/**
	 * 使用ポイント
	 */
	private Integer usePoint;

	/**
	 * コンストラクタ
	 */
	public BasketBean() {
	}

	/**
	 * コンストラクタ
	 *
	 * @param id  商品ID
	 * @param name  商品名
	 * @param stock 商品在庫数
	 */
	public BasketBean(Integer id, String name, Integer stock) {
		this.id = id;
		this.name = name;
		this.stock = stock;
	}

	/**
	 * コンストラクタ
	 *
	 * @param id  商品ID
	 * @param name  商品名
	 * @param stock  商品在庫数
	 * @param orderNum  注文個数
	 */
	public BasketBean(Integer id, String name, Integer stock, Integer orderNum) {
		this.id = id;
		this.name = name;
		this.stock = stock;
		this.orderNum = orderNum;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Integer subtotal) {
		this.subtotal = subtotal;
	}

	public Integer getUsePoint() {
		return usePoint;
	}

	public void setUsePoint(Integer usePoint) {
		this.usePoint = usePoint;
	}

}
