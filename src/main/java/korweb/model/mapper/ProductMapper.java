package korweb.model.mapper;

import korweb.model.dto.ProductDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper // [2] 해당 인터페이스가 myBatis mapper 임을 명시
public interface ProductMapper { // [1] 인터페이스타입 선언
    // 추상메소드
    // [3] @SQL어노테이션("SQL작성")
    // [4] SQL 에서 매개변수 표현 : #{매개변수}
    //@Insert("insert into products(name, price)values(#{name},#{price})")
    int save(ProductDto productDto);

    // (2) 제품 전체 조회 SQL 매핑
    //@Select("select * from products")
    List<ProductDto> findAll();
    // * mybatis @Select 에서는 반환타입을 특별하게 명시 하지 않아도,
    // 즉) 자동으로 추상메소드의 타입으로 결과를 변환해준다.
    // 조회결과를 자동으로 List<ProductDto> 로 변환 해준다.

    // (3) 제품 개별 조회 SQL 매핑
    //@Select("select * from products where id = #{id}")
    ProductDto find(int id);
    // * mybatis @Select 에서는 #{매개변수}을 추상메소드 매개변수와 같다.
    // 즉) 추상메소드 매개변수에 변수를 #{} 이용하여 SQL 문에 대입할 수 있다.
    // console 프로젝트, Dao 에서 ps.setInt(1, id);
    // 조회결과를 자동으로 ProductDto 로 변환 해준다.
    // console 프로젝트 Dao 에서 rs.next(); rs.getInt("id);

    // (4) 제품 수정
    // Dao : update products set name =?, price = ?, where id = ?
    // myBatis 방법 : update products set name = #{name}, price=#{price}, where id={#id}
    //@Update("update products set name = #{name} , price = #{price} where id=#{id}")
    boolean update(ProductDto productDto);
    // * 반환타입이 boolean 이면 *자동*으로 성공=true, 실패=false 반환

    //@Delete("delete from products where id = #{id}")
    boolean delete(int id);
}
