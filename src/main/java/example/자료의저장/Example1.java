package example.자료의저장;

import java.util.*;

public class Example1 {
    public static void main(String[] args) {

        // [생각] 이름 : 유재석, 나이 : 40, 이름 : 신동엽 , 나이 : 50
        // 조건1 : 위 4가지 자료를 자바에서 저장/대입 하는 방법
        // * 배열 <--> 클래스 : 배열은 동일한 타입 / 클래스는 서로 다른 타입
        // 1) 변수 : 하나의 자료를 저장하는 메모리 공간
        // [1] 자료가 4개 이니까 변수 4개 만들자 특징 : 자료 갯수에 따라 변수가 많아지므로 관리가 힘들다.
        String name1 = "유재석";
        String old1 = "40살";
        String name2 = "신동엽";
        String old2 = "50살";
        System.out.println(name1 + old1 + name2 + old2);

        // 2) 배열 : *동일* 타입의 여러 자료들을 하나의 변수에 저장하는 메모리 공간
        // 특징 : 파이썬/JS 와 다르게 자바의 배열은 동일한 타입의 여러 자료 들, 각 자료들 마다의 속성 정보를 알 수 없다.
        // [2] 여러 자료들을 하나의 변수에 순서대로 저장하자
        String [] array = new String[4];
        array[0] = "유재석";
        array[1] = "40";
        array[2] = "신동엽";
        array[3] = "50";
        System.out.println(array);
        // 3) 클래스 타입 : *서로 다른 타입*의 여러 자료들을 하나의 자료로 저장하는 메모리 공간
        // 특징 : 자료들을 저장할때 자료들의 상징적인 이름을 붙일 수 있다(멤버변수)
        Value value = new Value();
        value.name1 = "유재석";
        value.age1=40;
        value.name2="신동엽";
        value.age2=50;
        // 4) 컬렉션 프레임 워크 : 여러 자료들을 미리 만들어진 자료구조 클래스에 따라 자료를 저장하는 메모리 공간
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("name1","유재석");
        valueMap.put("old1","40");
        valueMap.put("name2","신동엽");
        valueMap.put("old1","50");
        
        // [생각해보기2]
        // Remote 클래스에 Tv와 Audio 인터페이스 타입의 자료를 모두 저장할 수 있도록 코드 수정
        // Remo re = new Remote();
        // implements : (여러개) 구현하다
        // extends : (한개) 상속하다.

        // [생각해보기3]
        // ArrayList 타입의 자료와 LinkedList 타입의 자료를 하나의 타입으로 저장하는 방법
        // 1) Object - 최상위 클래스로, 2) List - List 형식으로
       List<?> list = new ArrayList<>();
        list = new LinkedList<>();
    }
}

interface Tv{}
interface Audio{}
//interface Remo implements Tv, Audio{ }
class Remote implements Tv, Audio{} // implements 여러개를 구현하다.

class Value{
    String name1; int age1; String name2; int age2;
}
