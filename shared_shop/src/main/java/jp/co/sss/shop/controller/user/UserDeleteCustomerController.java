package jp.co.sss.shop.controller.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

/**
 * 会員削除機能(一般ユーザー)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class UserDeleteCustomerController {
	/**
	 * 会員情報
	 */
	@Autowired
	UserRepository userRepository;

	/**
	 * 会員情報削除確認処理
	 *
	 * @param model Viewとの値受渡し
	 * @param form  会員情報フォーム
	 * @return "ここも書く" 会員情報 削除確認画面へ
	 */
	@RequestMapping(path = "", method = RequestMethod.POST)
	public String deleteCheck(Model model, @ModelAttribute UserForm form) {

		// 削除対象の会員情報を取得
		User user = userRepository.findById(form.getId()).orElse(null);

		UserBean userBean = new UserBean();

		// Userエンティティの各フィールドの値をUserBeanにコピー
		BeanUtils.copyProperties(user, userBean);

		// 会員情報をViewに渡す
		model.addAttribute("user", userBean);

		return "";
	}

	/**
	 * 会員情報削除完了処理
	 *
	 * @param form 会員情報フォーム
	 * @return "" 会員情報 削除完了画面へ
	 */
	@RequestMapping(path = "", method = RequestMethod.POST)
	public String deleteComplete(@ModelAttribute UserForm form) {

		// 削除対象の会員情報を取得
		User user = userRepository.findById(form.getId()).orElse(null);

		// 削除フラグを立てる
		user.setDeleteFlag(Constant.DELETED);

		// 会員情報を保存
		userRepository.save(user);

		return "";
	}

	/**
	 * 会員情報削除完了処理
	 *
	 * @param form 会員情報フォーム
	 * @return "" 会員情報 削除完了画面へ
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String deleteCompleteRedirect() {

		return "";
	}

	/**
	 * 会員情報 削除確認処理から会員詳細画面に戻る処理
	 *
	 * @param model Viewとの値受渡し
	 * @param form  会員情報フォーム
	 * @return "" 会員情報 詳細画面へ
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String deleteBack(Model model, @ModelAttribute UserForm form) {

		// 削除対象の会員情報を取得
		User user = userRepository.findById(form.getId()).orElse(null);

		UserBean userBean = new UserBean();

		// Userエンティティの各フィールドの値をUserBeanにコピー
		BeanUtils.copyProperties(user, userBean);

		// 会員情報をViewに渡す
		model.addAttribute("user", userBean);

		return "";
	}
}
