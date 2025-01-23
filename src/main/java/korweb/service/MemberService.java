package korweb.service;

import jakarta.transaction.Transactional;
import korweb.model.dto.MemberDto;
import korweb.model.entity.MemberEntity;
import korweb.model.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    // 회원가입
    @Transactional // 트랜잭션
    public boolean signup(MemberDto memberDto) {
        // 1. DTO를 Entity로 변환한 toMEntity를 가져오기
        MemberEntity memberEntity = memberDto.toMEntity();
        // 2. 변환된 엔티티를 Save하고 그 결과 entity를 반환 받는다.
        MemberEntity saveEntity = memberRepository.save(memberEntity);
        // 3. 회원가입 해놓고, mno가 제대로 생성 되었는지 여부 확인해서 return값 반환
        if (saveEntity.getMno() > 0) {
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
            return true;
        } else {
            System.out.println("로그인 실패");
            return false; // 실패
        }
    }
}
