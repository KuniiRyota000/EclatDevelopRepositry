package jp.co.sss.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	// 商品情報を新着順で検索
	public Page<Item> findByDeleteFlagOrderByInsertDateDesc(int deleteFlag, Pageable pageable);

	//価格帯曖昧検索
	@Query("SELECT i FROM Item i WHERE price BETWEEN :minPrice AND :maxPrice")
	public Page<Item> findByPriceBetween(Integer minPrice, Integer maxPrice, Pageable pageable);

	//商品情報を売れ筋順で検索
	public Page<Item> findByDeleteFlagOrderBySalesFiguresDesc(int deleteFlag, Pageable pageable);

	//該当するカテゴリ情報を新着順で検索
	@Query("SELECT i FROM Item i WHERE i.category.id=:categoryId ORDER BY insertDate DESC")
	public Page<Item> findByCategoryIdOrderByInsertDateDesc(@Param("categoryId") Integer categoryId, Pageable pageable);
}
