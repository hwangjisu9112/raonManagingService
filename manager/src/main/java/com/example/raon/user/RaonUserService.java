package com.example.raon.user;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;



//ユーザーのビジネスロジク

@Transactional
@Service
@RequiredArgsConstructor
public class RaonUserService {

    private final RaonUserRepository raonUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;
	private final ResourceLoader resourceLoader;

    //社員リスト
	public Page<RaonUser> getList(Integer page, String kw) {

		//pageableオブジェクトを作成、1ページあたりのアイテム数を20に設定
		Pageable pageable = PageRequest.of(page, 20);
		
		//検索キーワード(kw)を使ってSpecificationを生成
		Specification<RaonUser> spec = searchByRaonUser(kw);
		
		//RaonUserエンティティを検索し、ページングおよびソート情報を適用してページに返し
    	return this.raonUserRepository.findAll(spec, pageable);

    	}
      
  	//IDで社員メールを検索
  	public RaonUser getRaonUserID(Long id) {
  		
  		//与えられたIDでRaonUserを検索します。 検索結果はOptional<Raon User>形式で返却
  		Optional<RaonUser> raonUser = this.raonUserRepository.findById(id);

  		//Optionalオブジェクトから実際のエンティティをインポートして返却
  		return raonUser.get();

  	}


    public RaonUser create(RaonUser raonUser) {
    	
    	//ユーザーのパスワードを暗号化して設定
        raonUser.setPassword(passwordEncoder.encode(raonUser.getPassword()));

        //ユーザーのEmployee 情報を検索
        Employee e = employeeRepository.findById(raonUser.getEmployee().getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + raonUser.getEmployee().getEmployeeId()));

        //検索されたEmployeeオブジェクトをユーザのRaonUserオブジェクトに設定
        raonUser.setEmployee(e);
        
        //ユーザのattendCodeを設定
        raonUser.setAttendCode(raonUser.getAttendCode());

        //生成されたRaonUserオブジェクトを保存
        //生成されたRaonUserオブジェクトを返します
        raonUserRepository.save(raonUser);
        return raonUser;
    }
    
    //メソッド内で実行されるすべてのデータベース操作が 1 つのトランザクションに結びつく
    //ユーザーロールを割り当て
    @Transactional
    public RaonUser createUserWithRole(RaonUser raonUser, RaonUserRole role) {
        
    	//ユーザーのパスワードを暗号化して設定
    	raonUser.setPassword(passwordEncoder.encode(raonUser.getPassword()));

    	//ユーザーのEmployee情報を検索します。 Employeeエンティティは、raonUserオブジェクト内のemployeeIdで識別
        Employee e = employeeRepository.findById(raonUser.getEmployee().getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("社員番号（EmployeeID）を確認してください　： " + raonUser.getEmployee().getEmployeeId()));

        //検索されたEmployeeオブジェクトをユーザのRaonUserオブジェクトに設定
        raonUser.setEmployee(e);
        
        //ユーザーに役割を割り当て
        raonUser.setRole(role);
        
        //生成されたRaonUserオブジェクトを保存
        return raonUserRepository.save(raonUser);
    }
    
    
    //社員メールを削除
  	public void delete(RaonUser raonUser) {

  		//社員情報をデータベースから削除
  		this.raonUserRepository.delete(raonUser);
  		
  	}
 
    
	//AuthCodeを生成
	private String generateRandomAuthCode() {
		
		//UUIDクラスを使用してランダムなUUID(Universally Unique Identifier)を生成し、これを文字列に返す
		return UUID.randomUUID().toString();
	}

    
    //メソッド内で実行されるすべてのデータベース操作が 1 つのトランザクションに結びつく
	//
    @Transactional
    public ResponseEntity<String> sendMailAndGenerateAuthCode(String username, String email) {
        
    	//raonUserRepository を使用して、指定されたUsernameを持つRaonUserをデータベースから検索。戻り値は Optional<RaonUser> 型
    	Optional<RaonUser> oru = raonUserRepository.findByUsername(username);
        
    	//Optionalを使用してユーザーが存在するかどうかを確認
    	if (oru.isPresent()) {
    		
    		//generateRandomAuth Codeメソッドを呼び出して無作為認証コード(auth Code)を生成
            String authCode = generateRandomAuthCode();
            
            //OptionalからRaonUserエンターテインメントを抽出
            RaonUser raonUser = oru.get();
            
            //ユーザエンティティのauthCodeフィールドに生成された認証コードを設定
            raonUser.setAuthCode(authCode);
            
            //変更されたユーザー エンティティをデータベースに保存
            raonUserRepository.save(raonUser); 
            
            //電子メールを送信するメソッドを呼び出す
            sendResetPasswordEmail(email, authCode);
            
            //パスワードリセットメールが正常に送信されたことを示すHTTP応答を返し
            return ResponseEntity.ok("認証メールが正常に送信されました。 メールを確認してください。");
        
    	//ユーザーが存在しないため、リクエストを処理できないことを示すHTTP応答を返し
    	} else {
            return ResponseEntity.badRequest().body("存在しない会員です");
        }
    }
    
    
    //送信するメールの形を定義
    public void sendResetPasswordEmail(String toEmail, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("RaonManagerパスワードリセットのご案内");

        String emailContent = loadEmailTemplate(authCode); 
        message.setText(emailContent);

        javaMailSender.send(message);
    }
    
    
    //送信するメールの形を定義
    private String loadEmailTemplate(String authCode) {
    	
        try {
        	
        	//resourceLoaderを使用してクラスパス(classpath)からメールテンプレートファイルをロード
            Resource resource = resourceLoader.getResource("classpath:templates/email-templates/send_authcode_mail.html");
            
            //resourceから取得した入力ストリームを使用してテンプレートファイルの内容をバイト配列
            byte[] templateBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            
            //読み込んだバイト配列をUTF-8文字列にデコードしてテンプレート文字列に変換
            String template = new String(templateBytes, StandardCharsets.UTF_8);
           
            //テンプレート文字列で${authCode}を実際のauthCode値に置き換え、完成した電子メールテンプレートを返します
            return template.replace("${authCode}", authCode);
            
            //
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
    
    //入力するAuthCodeが正しいにかを判別
    public boolean isAuthCodeValid(String authCode) {
    	
    	//与えられたauthCodeでRaonUserを検索します。 検索結果はOptional<Raon User>形式で返却

        Optional<RaonUser> oru = raonUserRepository.findByAuthCode(authCode);
        
        //Optionalが空でなければtrueを返し、そうでなければfalseを返す
        return oru.isPresent();
    }
    
    //パスワード修正するメソッド
    public boolean resetPassword(String authCode, String newPassword) {
        
    	//与えられたauthCodeでRaonUserを検索します。 検索結果はOptional<Raon User>形式で返却
    	Optional<RaonUser> oru = raonUserRepository.findByAuthCode(authCode);
        
    	//検索結果が存在する場合、以下のロジックを実行
    	if (oru.isPresent()) {
    		
    		//検索されたRaonUserオブジェクト
            RaonUser raonUser = oru.get();
            //新しいパスワードもBCryptPasswordEncoderで登録するべき
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            
            //新しいパスワードをハッシュ化
            String encodedPassword = passwordEncoder.encode(newPassword);
            
            //ユーザーのパスワードを新しいハッシュ化されたパスワードに更新
            raonUserRepository.updatePasswordByUsername(raonUser.getUsername(), encodedPassword);
            
            return true;
        }
        	return false;
    }
    

	// 検索
	public Specification<RaonUser> searchByRaonUser(String kw) {
	    
		//無名クラスを使用して、Specification インターフェースの匿名実装を作成
		return new Specification<>() {
	        
	    	//オブジェクトシリアル化
	    	private static final long serialVersionUID = 1L;
	        
			//検索条件を生成するためのメソッド
	        @Override
	        public Predicate toPredicate(Root<RaonUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	            
				//クエリの結果から重複を排除
	        	query.distinct(true);

	        	// "attendCode","nameEmployee","username"がキーワードに部分一致する条件
	            Predicate idPredicate = cb.like(cb.lower(root.get("attendCode").as(String.class)), "%" + kw.toLowerCase() + "%");
	            Predicate emplooyeeNamePredicate = cb.like(cb.lower(root.get("nameEmployee")), "%" + kw.toLowerCase() + "%");
	            Predicate usernamePredicate = cb.like(cb.lower(root.get("username")), "%" + kw.toLowerCase() + "%");
	            
	 	        //"idPredicate", "emplooyeeNamePredicate",  "emplooyeeNamePredicate",  "usernamePredicate" のいずれかが一致する場合を返す
	            return cb.or(idPredicate, emplooyeeNamePredicate, usernamePredicate);
	        }
	    };
	}
	
}
