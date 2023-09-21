package com.example.raon.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/raonuser")
@RequiredArgsConstructor

//社内ユーザーメールのコントローラー
public class RaonUserController {

	// 生成子
	private final RaonUserService raonUserService;
	private final RaonUserRepository raonUserRepository;

	// ページに移動, ADMIN等級ユーザーのみ入場
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String userList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {

		//ページングが0以下にならないように制限
		if (page < 0) {
			page = 0;
		}

		//raonUserServiceを使用して、指定されたページ番号と検索キーワードに基づいてユーザーリストのページを取得
		Page<RaonUser> paging = this.raonUserService.getList(page, kw);
		
		//paging, kwオブジェクトをmodelに追加
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		
		//raon_list.htmlでレンダリング
		return "raon_list";
	}

	// 社内メールを登録するページに移動
	@GetMapping("/signup")
	public String signUp(Model model) {
		
		//new RaonUserオブジェクトをmodelに追加
		model.addAttribute("raonUser", new RaonUser());
		
		//raon_signupでレンダリング
		return "raon_signup";
	}

	
	//社内メールを登録
	@PostMapping("/signup")
	public String signUp(@Valid RaonUser raonUser, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			
			//raon_signupでレンダリング
			return "raon_signup";
		}

		//パスワードとパスワードの再確認フィールドの値が一致しない場合、エラーメッセージを追加し、"raon_signup"ビューを再度表示
		if (!raonUser.getPassword().equals(raonUser.getPasswordRe())) {
			bindingResult.rejectValue("passwordRe", "passwordIncorrect", "パスワードが違います");
			
			//raon_signupでレンダリング
			return "raon_signup";
		}

		//
		try {
			
			//ユーザー情報を登録するために、raonUserServiceのcreateメソッドを呼び出す
			raonUserService.create(raonUser);
			
			//データベースに重複するユーザーが既に存在する場合、DataIntegrityViolationExceptionが発生
		} catch (DataIntegrityViolationException e) {
			
			e.printStackTrace();
			bindingResult.reject("signupFailed", "もう登録されているユーザーです。");
			
			return "raon_signup";
		
		//それ以外の例外が発生した場合、エラーメッセージを追加し、"raon_signup"ビューを再度表示
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			
			//raon_signupでレンダリング
			return "raon_signup";
		}

		// リダイレクト -> Main
		return "redirect:/";
	}

	// ログイン、 ビジネスロジクはSPRING securityで実装
	@GetMapping("/login")
	public String login() {
		
		//raon_loginでレンダリング
		return "raon_login";
	}

	// 社員メール削除, ADMIN等級ユーザーのみ入場
	@GetMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_RAON_ADMIN')")
	public String DeleteEmployee(Principal principal, @PathVariable("id") Long id) {
		
		//raonUserServiceを使用して、指定されたIDに対応する社員メール情報を取得
		RaonUser raonUser = this.raonUserService.getRaonUserID(id);

		//deleteメソッドを実行
		this.raonUserService.delete(raonUser);
		
		// リダイレクト -> /raonuser/list
		return "redirect:/raonuser/list";
	}

	// パスワード修正するページに移動
	@GetMapping("/reset-sendmail")
	public String sendMailpage() {

		//raon_reset_sendmailでレンダリング
		return "raon_reset_sendmail";
	}

	// パスワード修正する為にAuthCodeをメールで発送
	@PostMapping("/reset-sendmail")
	public ResponseEntity<String> sendMailpage(@RequestParam("username") String username,
			@RequestParam("email") String email) {

		//sendMailAndGenerateAuthCodeメソッドを呼び出す
		return raonUserService.sendMailAndGenerateAuthCode(username, email);
	}

	// AuthCodeを確認するページ
	@GetMapping("/reset-authcode")
	public String checkAuthCodePage() {

		//raon_reset_authcodeでレンダリング
		return "raon_reset_authcode";
	}

	// AuthCodeを確認する
	@PostMapping("/reset-authcode")
	public String checkAuthCodePage(@RequestParam("authCode") String authCode) {
		
		//authCodeに基づいてRaonUserエンターテインメントを検索
		Optional<RaonUser> oru = raonUserRepository.findByAuthCode(authCode);
		
		//oru.isPresent()を使ってoruが空いているかどうかを確認
		if (oru.isPresent()) {
			
			//有効な場合は、ユーザーをパスワードリセットページにリダイレクトするようURLを作成して返却
			//そうでない場合、error=invalidクエリパラメータを追加して無効なauthCodeエラーを示し、ユーザーをリダイレクト
			return "redirect:/raonuser/reset-password?authCode=" + authCode;
		} else {
			
			
			return "redirect:/raonuser/reset-authcode?error=invalid";
		}
	}

	//新しいパスワード更新ページに移動
	@GetMapping("/reset-password")
	public String resetPasswordPage(@RequestParam("authCode") String authCode, Model model) {
		
		//authCodeオブジェクトをmodelに追加
		model.addAttribute("authCode", authCode);
		
		//raon_reset_passwordでレンダリング
		return "raon_reset_password";
	}

	//新しいパスワード更新
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam("authCode") String authCode,
			@RequestParam("newPassword") String newPassword) {
		
		//raon Uservice.reset Password(auth Code, new Password)を呼び出してパスワードの再設定
		if (raonUserService.resetPassword(authCode, newPassword)) {
			return ResponseEntity.ok("パスワードのリセットが完了しました。");
		} else {
			return ResponseEntity.badRequest().body("AutoCodeが違います。");
		}
	}

}