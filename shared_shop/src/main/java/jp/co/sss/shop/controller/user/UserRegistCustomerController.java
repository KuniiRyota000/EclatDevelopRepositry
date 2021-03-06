package jp.co.sss.shop.controller.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 会員管理 登録機能(一般ユーザー)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class UserRegistCustomerController {
	/**
	 * 会員情報
	 */
	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * 会員情報入力画面表示処理
	 *
	 * @param Model Viewとの値受渡し
	 * @return "user/regist/user_regist_input" 会員情報 登録入力画面へ
	 */
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.GET)
	public String registInput(Model model) {
		UserForm userForm = new UserForm();

		model.addAttribute("userForm", userForm);
		return "user/regist/user_regist_input";

	}

	/**
	 * POSTメソッドを利用して会員情報入力画面に戻る処理
	 *
	 * @param form 会員情報
	 * @return "user/regist/user_regist_input" 会員情報 登録入力画面へ
	 */
	@RequestMapping(path = "/user/regist/input", method = RequestMethod.POST)
	public String registInput(UserForm form) {

		return "user/regist/user_regist_input";
	}

	/**
	 * 会員情報 登録確認処理
	 *
	 * @param form   会員情報フォーム
	 * @param result 入力チェック結果
	 * @return 入力値エラーあり："user/regist/user_regist_input" 会員情報登録画面へ
	 *         入力値エラーなし："user/regist/user_regist_check" 会員情報 登録確認画面へ
	 */
	@RequestMapping(path = "/user/regist/check", method = RequestMethod.POST)
	public String registCheck(@Valid @ModelAttribute UserForm form, BindingResult result) {

		// 入力値にエラーがあった場合、会員情報 入力画面表示処理に戻る
		if (result.hasErrors()) {
			return "user/regist/user_regist_input";
		}

		return "user/regist/user_regist_check";
	}

	/**
	 * 会員情報登録完了処理
	 *
	 * @param form 会員情報
	 * @return "redirect:/user/regist/complete" 会員情報 登録完了画面へ
	 * @throws ParseException
	 */
	@RequestMapping(path = "/user/regist/complete", method = RequestMethod.POST)
	public String registComplete(@ModelAttribute UserForm form) throws ParseException {
		// 会員情報の生成
		User user = new User();
		//権限に2を追加
		form.setAuthority(2);
		//新規会員のためポイントは0
		form.setPoint(0);
		form.setDeleteFlag(0);

		java.util.Date now = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = sdf.format(now);
		now = sdf.parse(nowDate);
		java.sql.Date sqlNowDate = new java.sql.Date(now.getTime());
		nowDate = sdf.format(sqlNowDate);

		form.setInsertDate(nowDate);

		// 入力値を会員情報にコピー
		BeanUtils.copyProperties(form, user);

		// 会員情報を保存
		userRepository.save(user);
		session.setAttribute("userInfo", user);
		//userBeanを宣言
		UserBean userBean = new UserBean();

		userBean.setId(user.getId());
		userBean.setName(user.getName());
		userBean.setAuthority(user.getAuthority());

		// セッションスコープにログインしたユーザの情報を登録
		session.setAttribute("user", userBean);

		return "redirect:/user/regist/complete_regist";
	}

	/**
	 * 会員情報登録完了画面表示
	 *
	 * @param form 会員情報
	 * @return "user/regist/user_regist_complete" 会員情報 登録完了画面へ
	 */
	@RequestMapping(path = "/user/regist/complete_regist", method = RequestMethod.GET)
	public String registCompleteRedirect() {
		return "user/regist/user_regist_complete";

	}
}
