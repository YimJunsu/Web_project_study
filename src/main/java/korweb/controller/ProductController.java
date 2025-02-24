package korweb.controller;

import korweb.model.dto.ProductDto;
import korweb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired private ProductService productService;

    @PostMapping("/product/save")
    // [POST]
    // [Body] { "name" : "콜라" , "price" : "1200" }
    public int save(@RequestBody ProductDto productDto){
        return productService.save(productDto);
    }
    // [2] 제품 전체 조회
    @GetMapping("/product/findall")
    public List<ProductDto> findAll(){
        return productService.findAll();
    }
    // [3] 제품 개별 조회
    @GetMapping("/product/find")
    public ProductDto find(@RequestParam int id){
        return productService.find(id);
    }
    // [4] 제품 수정
    @PutMapping("/product/update")
    public boolean update(@RequestBody ProductDto productDto){
        return productService.update(productDto);
    }
    @DeleteMapping("/product/delete")
    public boolean delete(@RequestParam int id){
        return productService.delete(id);
    }
}
