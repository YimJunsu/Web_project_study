package korweb.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import korweb.model.dto.LoginDto;
import korweb.model.dto.MemberDto;
import korweb.model.dto.PointDto;
import korweb.model.entity.MemberEntity;
import korweb.model.entity.PointEntity;
import korweb.model.repository.MemberRepository;
import korweb.model.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemberService implements UserDetailsService , OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // [2] OAUTH2 메소드 재정의(커스텀) , OAuth2UserService 인터페이스 'loadUser' 메소드 재정의
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // (1) loadUser 메소드 란 : oauth2 로 각 소셜 페이지에서 로그인 성공시 실행되는 메소드 , 로그인 성공후 유저 정보 반환
        System.out.println( "userRequest : " + userRequest ); // 유저 정보 요청 객체 // userRequest : org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest@2030742e
        // (2) 로그인을 성공한 oauth2 사용자 정보(동의항목)정보 반환
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser( userRequest );
        System.out.println( "oauth2User : " + oAuth2User  ); //
        // (3) oauth2 회사명 반환 : kakao , naver , google
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println( "registrationId : " + registrationId );
        // (4) 각 회사별 유저의 정보 반환하는 방법 다르다.
        String nickname = null;
        String image = null;
        Map< String , Object> profile = null;
        if( registrationId.equals("kakao") ){
            // (5) 로그인 성공한 카카오 회원의 정보 가져오기
            Map< String , Object > kakao_account = ( Map<String,Object> ) oAuth2User.getAttributes().get("kakao_account");
            // (6) 세부 회원 정보 가져오기
            profile = (Map<String, Object>) kakao_account.get("profile");
            // (7) 각 정보 가져오기
            nickname = profile.get("nickname").toString();
            image = profile.get("profile_image_url").toString();
            // (8) 만약에 최초 로그인이면 회원 DB 에 저장
            if( memberRepository.findByMid( nickname ) == null ){ // 만약에 DB에 로그인한 카카오 닉네임이 없으면 DB저장
                // DB에 저장할 DB 구성
                MemberEntity memberEntity = MemberEntity.builder()
                        .mid( nickname ) // 실제 카카오 아이디 가져올수 없으므로 닉네임 대체한다. 비지니스(사업자등록) 있으면 가능
                        .mname( nickname ) // 로그인한 회원 닉네임
                        .mmail( nickname ) // 실제 카카오 이메일 가져올수 없으므로 닉네임 대체한다. 비지니스(사업자등록) 있으면 가능
                        .mimg( image )  // 로그인 회원 프로필 사진
                        // 실제 카카오회원의 비밀번호를 절대 가져올수 없다. 임의 비밀번호 넣는다. OAUTH 회원은 비밀번호를 사용하지 않는다.임의데이터
                        .mpwd( new BCryptPasswordEncoder().encode("1234") )
                        .build();
                memberRepository.save( memberEntity ); // DB 에 entity 저장
            }
        } else if (registrationId.equals("naver")) {}
        else if( registrationId.equals("google") ){}

        // (9) DefaultOauth2User 타입으로 리턴 해야한다. 매개변수 3가지 : (1) 권한(ROLE) 목록 (2) 사용자정보 (3) 식별키
//        DefaultOAuth2User user = new DefaultOAuth2User( null , profile , "nickname"  );
//        return user;
        // (*) 권한 부여
        List<GrantedAuthority> 권한목록 = new ArrayList<>();
        권한목록.add(new SimpleGrantedAuthority("ROLE_USER"));
        권한목록.add(new SimpleGrantedAuthority("ROLE_OAUTH"));

        LoginDto loginDto = LoginDto.builder()
                .mid(nickname) // oauth2 회원은 패스워드가 없다.
                .mrolList(권한목록)
                .build();
        return loginDto;
        // loginDto 에서 "OAuth2User' 구현(implements) 했으므로 가능
    }

    @Autowired private MemberRepository memberRepository;
    @Autowired private PointRepository pointRepository;
    @Autowired private  FileService fileService;

    // 헷갈리면 repository 쓰는 곳 대부분 @Transactional 쓰기

    // (1) 회원가입 - (day70 회원가입시 프로필 사진 추가)
    @Transactional // 트랜잭션
    public boolean signup(MemberDto memberDto) {
        // - 프로필사진 첨부파일 존재하면 업로드 진행
            // (1) 만약에 업로드 파일이 비어 있으면 'default.jpg' 임시용 프로필 사진 등록한다.
        if(memberDto.getUploadfile().isEmpty()){memberDto.setMimg("default.jpg");}
        else { // (2) 아닌 업로드 파일이 존재하면, 파일 서비스 객체내 업로드 함수를 호출한다.
           String fileName = fileService.fileUpload(memberDto.getUploadfile()); // 업로드 함수에 multipart 객체를 대입 해준다
            // (3) 만약에 업로드 후 반환된 값이 null 이면 업로드 실패, null 아니면 업로드 성공
            if(fileName == null) {return false;} // 업로드 실패 했으면 회원가입 실패
        else {
            memberDto.setMimg(fileName); // 업로드 성공한 uuid+파일명을 dto 대입한다.
            }
        }
        // 1. DTO를 Entity로 변환한 toMEntity를 가져오기
        MemberEntity memberentity = memberDto.toMEntity();
        // 2. 변환된 엔티티를 Save하고 그 결과 entity를 반환 받는다.
        MemberEntity saveEntity = memberRepository.save(memberentity);
        // 3. 회원가입 해놓고, mno가 제대로 생성 되었는지 여부 확인해서 return값 반환
        if (saveEntity.getMno() > 0) {
            PointDto signdto = new PointDto();
            PointDto pointDto = PointDto.builder()
                    .pname("회원가입 보상")
                    .pcount(100)
                    .build();
            pointPayment(pointDto,memberentity);
            return true;
        } else {
            return false;
        }
    }

// [2]. 로그인 서비스 // 시큐리티 이후에 사용하지 않는다.
//    @Transactional // 트랜잭션
//    public boolean login( MemberDto memberDto ){
//        //[방법1]
//        /*
//        // (1) 모든 회원 엔티티를 조회한다.
//        List<MemberEntity> memberEntityList = memberRepository.findAll();
//        // (2) 모든 회원 엔티티를 하나씩 조회한다.
//        for( int index = 0 ; index <= memberEntityList.size()-1 ; index++ ){
//            // (3) index번째의 엔티티를 꺼내기
//            MemberEntity memberEntity = memberEntityList.get(index);
//            // (4) index번째의 엔티티 아이디가 입력받은(dto) 아이디와 같으면
//            if( memberEntity.getMid().equals( memberDto.getMid() ) ) {
//                // (5) index번째의 엔티티 비밀번호가 입력받은(dto)비밀번호와 같으면
//                if( memberEntity.getMpwd().equals( memberDto.getMpwd() ) ){
//                    System.out.println(" login OK ");
//                    return true; // 로그인 성공
//                } // if end
//            } // if end
//        } // for end
//        return false; // 로그인 실패
//        */
//        // [방법2] JPA Repository 추상메소드 활용.
//        boolean result
//        = memberRepository.existsByMidAndMpwd( memberDto.getMid() , memberDto.getMpwd() );
//
//        if( result == true ){
//            System.out.println("로그인성공");
//            setSession( memberDto.getMid() ); // 로그인 성공시 세션에 아이디 저장
//            //  포인트 DTO 생성
//            PointDto pointDto = PointDto.builder()
//                    .pcontent("로그인접속")
//                    .pcount( 1 ).build();
//            // - 현재 로그인된 엔티티 찾기  // .findById( pk번호 ) : 지정한 pk번호의 엔티티 조회
//            MemberEntity memberEntity = memberRepository.findById(  getMyInfo().getMno() ).get();
//            // 포인트 지급 함수
//            pointPayment( pointDto , memberEntity );
//
//            return true; // 로그인 성공
//        }else{
//            System.out.println("로그인실패");
//            return false; // 로그인 실패
//        }
//    } // f end

     // [  2  ] : 시큐리티에서의 로그인 함수
    // (1) 해당 서비스 클래스명 뒤에 implements UserDetailsService
    // (2) 'loadUserByUsername' 메소드 오버라이딩/재정의 한다

    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
        System.out.println("mid = " + mid);
        System.out.println("MemberService.loadUserByUsername");

        // (3) mid 이용하여 데이터베이스의 저장된 암호화 패스워드 가져오기.
        MemberEntity memberEntity = memberRepository.findByMid(mid); // 입력받은 아이디로 회원 엔티티 찾기
        if(memberEntity == null){
            throw new UsernameNotFoundException("없는 아이디 입니다.");
            // throw : 던지다 뜻 // new UsernameNotFoundException : 예외클래스 강제 실행
        }
        // 입력받은 아이디의 엔티티가 존재하면 암호화된 패스워드 확인
        String password = memberEntity.getMpwd();
        System.out.println("password = " + password);
        // (4) 입력받은 mid와 입력받은 mid의 암호화된 패스워드를 리턴 < USERDetails >
            // UserDetials : 인터페이스명, 시큐리티에서 사용하는 유저 정보 조작 인터페이스
            // User : 클래스, UserDetails 를 구현하는 구현(객)체
            // ---> 시큐리티는 UserDetails 반환 하면 자동으로 로그인 처리를 해준다.
            // 단] 입력받은 id 와 입력받은 id의 암호화된 password 넣어줘야한다.
//        UserDetails user = User.builder().username(mid).password(password).build();
//        return user;

        // (*) 권한/등급 부여하기. GrantedAuthority : 시큐리티 사용자의 권한 조작하는 인터페이스
        // SimpleGrantedAuthority : 시큐리티 사용자의 권한 클래스 ( 구현체 )
        List<GrantedAuthority> 권한목록 = new ArrayList<>();
        권한목록.add(new SimpleGrantedAuthority("ROLE_USER")); // 권한명 규칙 : ROLE_권한명
        권한목록.add(new SimpleGrantedAuthority("ROLE_GENERAL")); // 권한은 여러개 넣을 수 있다.
        // DB에 존재하는 권한으로 부여할 수 있다."ROLE_+"변수명

        LoginDto loginDto = LoginDto.builder()
                .mid(mid)
                .mpwd(password)
                .mrolList(권한목록) // LoginDto 에 권한목록 넣어주지.
                .build();
        return loginDto;
        // loginDto 에서 "UserDetails' 구현(implements) 했으므로 가능
    }

    // ===================== 세션 관련 함수 ===================== //
    // (1) 내장된 톰캣 서버의 요청 객체
    @Autowired private HttpServletRequest request;

    /*// [3] 세션객체내 정보 추가 : 세션객체에 로그인된 회원아이디를 추가하는 함수. ( 로그인 ) - 시큐리티 이후에는 사용하지 않는다, // 시큐리티가 자동으로 세션 추가해준다.
    public boolean setSession( String mid ){
        // (2) 요청 객체를 이용한 톰캣내 세션 객체를 반환한다.
        HttpSession httpSession = request.getSession();
        // (3) 세션 객체에 속성(새로운 값) 추가한다.
        httpSession.setAttribute( "loginId" , mid );
        return true;
    } // f end*/

    // [4] 세션객체내 정보 반환 : 세션객체에 로그인된 회원아이디 반환하는 함수 ( 내정보 조회 , 수정 등등 ) // 시큐리티 이후에 코드 수정.
    public String getSession( ){
        // (1) 시큐리티 에서 자동으로 생성한 (로그인) 세션 꺼내기 , 기존 세션 사용법과 다르다.
        // SecurityContextHolder : 시큐리티에 관련된 정보 저장소 // .getContext() : 저장소 반환
        // .getAuthentication() : 저장소에서 인증서 반환 // .getPrincipal(); : 중요한 인증 정보
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // (2) 만약에 비로그인[ anonymousUser ] 이면
        if( object.equals("anonymousUser") ){  return null; } // 비로그인 상태이면 null 반환
        // (3) 로그인 상태이면 로그인 구현할때 'loadUserByUsername' 메소드에서 반환한 userDetails 로 타입변환
        // * userDetails : 일반회원 타입  vs OAuth2User : oauth회원 타입 ===> 타입별 구분 해야한다 , 통합이 필요하다.
        // (4) 로그인된 정보에서 mid 꺼낸다.
        String loginMid = "";
        // -> (클래스 하나 더 만들어서 처리 해도 됨 가독성 증가)
        /*if( object instanceof UserDetails ){ // 객체 instanceof 타입 : 객체가 지정한 타입인지 확인하는 키워드 , 객체가 해당 타입이면 true 아니면 false
            // 현재로그인세션이 UserDetails(일반회원) 타입 이면
            UserDetails userDetails = (UserDetails)object;
            loginMid = userDetails.getUsername();
        }else if( object instanceof DefaultOAuth2User ){
            // 현재로그인세션이 DefaultOAuth2User(oauth2회원) 타입 이면
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) object;
            loginMid = oAuth2User.getAttributes().get("nickname").toString();
        }*/
        LoginDto loginDto = (LoginDto)object;
        loginMid = loginDto.getMid();
//        UserDetails userDetails = (UserDetails) object;
//        // (4) 로그인된 정보에서 mid 꺼낸다.
//        String loginMid = userDetails.getUsername();
        // (5) 로그인된 mid를 반환한다.
        return loginMid;
    }
//    public String getSession( ){
//        // (2)
//        HttpSession httpSession = request.getSession();
//        // (4) 세션 객체에 속성명의 값 반환한다. * 반환타입이 Object 이다.
//        Object object = httpSession.getAttribute( "loginId");
//        // (5) 검사후 타입변환
//        if( object != null ){// 만약에 세션 정보가 존재하면
//            String mid = (String)object; // Object타입 --> String타입
//            return mid;
//        }
//        return null;
//    } // f end

    // [5] 세션객체내 정보 초기화 : 로그아웃
    public boolean deleteSession(){
        HttpSession httpSession = request.getSession(); // (2)
        // (3) 세션객체 안에 특정한 속성명 제거
        httpSession.removeAttribute( "loginId");
        return true;
    }
    // [6] 현재 로그인된 회원의 정보 조회 - [스프링 시큐리티로 대체]
    public MemberDto getMyInfo(){
        // 1. 현재 세션에 저장된 회원 아이디 조회
        String mid = getSession();
        // 2. 만약에 로그인 상태이면
        if(mid != null){
            // 3. 회원아이디로 엔티티 조회
            MemberEntity memberEntity = memberRepository.findByMid(mid);
            // 4. entity --> dto 변환
            MemberDto memberDto = memberEntity.toMDto();
            // 5. 반환
            return memberDto;
        }
        return null;
    }
    // [7] 현재 로그인된 회원 탈퇴
    public boolean myDelete(){
        // 1. 현재 세션에 저장된 회원 아이디 조회
        String mid = getSession();
        // 2. 만약에 로그인 상태이면
        if(mid != null){
            // 3. 현재 로그인된 아이디로 로그인 조회
            MemberEntity memberEntity = memberRepository.findByMid(mid);
            // 4. 엔티티 탈퇴/삭제하기
            memberRepository.delete(memberEntity);
            deleteSession(); // 로그인 정보 지우기 : 로그아웃
            return true;
        }
        return false;
    }
    // [8] 현재 로그인된 정보 수정, mname 닉네임, mmail 이메일
    @Transactional // 수정시 Transactional 필수
    public boolean myUpdate(MemberDto memberDto){
            // 1. (로그인된) 내정보 가져오기
        String mid = getSession();
        if(mid != null){
            MemberEntity memberEntity = memberRepository.findByMid(mid);
            memberEntity.setMname(memberDto.getMname());
            memberEntity.setMmail(memberDto.getMmail());
            return true;
        }
        return false;
    }
    @Transactional
    // [9] (부가서비스)포인트 지급 함수. 지급내용 pname / 지급수량 pcount / 지급받는 회원 엔티티
    public boolean pointPayment(PointDto pointDto, MemberEntity memberEntity){

            PointEntity pointEntity = pointDto.toPEntity();
            pointEntity.setMemberEntity(memberEntity); // 지급받는 회원 엔티티 대입

            PointEntity saveEntity = pointRepository.save(pointEntity);
            if(pointEntity.getPno() > 0){return true;}
            else {return false;}
    }

    // [10] 내 포인트 내역 전체 출력
    public List<PointDto> pointList(){
        // 1. (로그인된) 내정보 가져오기
        String mid = getSession();
        MemberEntity memberEntity = memberRepository.findByMid(mid);
        // 2. 내 포인트 조회하기
        List<PointEntity> pointEntityList = pointRepository.findByMemberEntity(memberEntity);
        // 3, 조회된 포인트 엔티티를 dto 반환
        List<PointDto> pointDtoList = new ArrayList<>();
        pointEntityList.forEach(pointEntity -> {pointDtoList.add(pointEntity.toPDto());
        });
        return pointDtoList;
    }
    // [11] 내 현재 포인트 출력
    public int pointInfo(){
            String mid = getSession();
            MemberEntity memberEntity = memberRepository.findByMid(mid);
            List<PointEntity> pointEntityList = pointRepository.findByMemberEntity(memberEntity);
            int mypoint = 0;
            for(int index = 0; index<=pointEntityList.size()-1; index++) {
                mypoint += pointList().get(index).getPcount();
            }
            return mypoint;
            }
    }


