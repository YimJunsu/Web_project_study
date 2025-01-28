package korweb.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import korweb.model.dto.MemberDto;
import korweb.model.dto.PointDto;
import korweb.model.entity.MemberEntity;
import korweb.model.entity.PointEntity;
import korweb.model.repository.MemberRepository;
import korweb.model.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
    @Autowired private MemberRepository memberRepository;
    @Autowired private PointRepository pointRepository;

    // 헷갈리면 repository 쓰는 곳 대부분 @Transactional 쓰기
    // 회원가입
    @Transactional // 트랜잭션
    public boolean signup(MemberDto memberDto) {
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

    // 로그인
    public boolean login(MemberDto memberDto) {
        // [방법 1]
        /*// (1) 모든 회원 엔티티를 조회한다.
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        // (2) 모든 회원 엔티티를 하나씩 조회한다.
        for(int index = 0; index <= memberEntityList.size() - 1; index++){
            // (3) index 번째의 엔티티 꺼낸 후, 엔티티가 입력받은(dto) 아이디와 같으면 && 엔티티가 입력받은(dto) 비밀번호와 같으면
            if( memberEntityList.get(index).getMid().equals( memberDto.getMid()) && memberEntityList.get(index).getMpwd().equals(memberDto.getMpwd()) ){
                System.out.println("로그인 성공");
                return true; // 성공
            }
        }*/
        // [방법 2] JPA Repository 추상 메소드 활용.
        boolean result =
                memberRepository.existsByMidAndMpwd(memberDto.getMid(), memberDto.getMpwd());
        if (result == true) {
            System.out.println("로그인 성공");
            setSession(memberDto.getMid()); // 로그인 성공시 세션에 아이디 저장
            // 포인트 DTO 생성
            PointDto pointDto = PointDto.builder()
                    .pname("로그인 보상")
                    .pcount(+ 1).build();
            // - 현재 로그인된 엔티티 찾기 // .findById(pk번호) : 지정한 pk번호의 엔티티 조회
            MemberEntity memberEntity = memberRepository.findById(getMyInfo().getMno()).get();
            // - 포인트 지급 함수
            pointPayment(pointDto, memberEntity);
            return true;
        } else {
            System.out.println("로그인 실패");
            return false; // 실패
        }

    }
     // ===================== 세션 관련 함수 ===================== //
    // (1) 내장된 톰캣 서버의 요청 객체
    @Autowired private HttpServletRequest request;

    // [3] 세션객체 내 정보 추가 : 세션객체에 로그인된 회원아이디를 추가하는 함수. ( 로그인 성공시 )
        public boolean setSession(String mid){
            // (2) 요청 객체를 이용한 톰캣 내 세션 객체를 반환한다.
            HttpSession httpSession = request.getSession();
            // (3) 세션 객체에 속성(새로운 값) 추가한다.
            httpSession.setAttribute("loginId",mid);
            return true;
    }
    // [4] 세션객체 내 정보 반환 : 세션객체에 로그인 된 회원 아이디 반환하는 함수 ( 내정보 조회, 수정 등등)
    public String getSession(){
        // (2) 요청 객체를 이용한 톰캣 내 세션 객체를 반환한다.
        HttpSession httpSession = request.getSession();
        // (4) 세션 객체에 속성명의 값 반환한다. * 반환타입이 Object 이다.
        Object object = httpSession.getAttribute("loginId");
        // (5) 검사후 타입 변환
        if(object != null){
            //만약에 세션 정보가 존재하면
            String mid = (String) object; // Object타입 --> String타입
            return mid;
        }
        return null;
    }
    // [5] 세션객체내 정보 초기화 : 로그아웃
    public boolean deleteSession(){
        // (2) 요청 객체를 이용한 톰캣 내 세션 객체를 반환한다.
        HttpSession httpSession = request.getSession();
        // (5) 세션객체 안에 특정 정보를 지우기
        httpSession.removeAttribute("loginId");
        return true;
    }
    // [6] 현재 로그인된 회원의 정보 조회
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

