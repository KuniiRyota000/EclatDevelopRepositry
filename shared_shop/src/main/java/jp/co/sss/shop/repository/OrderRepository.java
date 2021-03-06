package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.util.JPQLConstant;

/**
 * ordersテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository

public interface OrderRepository extends JpaRepository<Order, Integer> {

	Order findTop1ByOrderByInsertDateDesc();

	// 会員IDに該当する注文情報を注文日付順で検索
	//Page<Order> findByUserIdOrderByInsertDateDesc(int userId, Pageable pageable);

	// 会員IDに該当する注文情報を注文日付順で検索
	@Query("SELECT o FROM Order o WHERE o.user.id=:userId ORDER BY insertDate DESC")
	public Page<Order> findByUserIdOrderByInsertDateDesc(@Param("userId") Integer userId, Pageable pageable);

	// 注文日付順で注文情報すべてを検索
	@Query(JPQLConstant.FIND_ALL_ORDERS_ORDER_BY_INSERT_DATE)
	Page<Order> findAllOrderByInsertDateDesc(Pageable pageable);

	@Query("SELECT id FROM Order o")
	List<Integer> findIdByAll();

}
