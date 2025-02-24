package korweb.service;

import korweb.model.dto.ProductDto;
import korweb.model.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired private ProductMapper productMapper;

    public int save(ProductDto productDto){
        return productMapper.save(productDto);
    }

    public List<ProductDto> findAll(){
        return productMapper.findAll();
    }
    public ProductDto find(int id){
        return productMapper.find(id);
    }
    public boolean update(ProductDto productDto){
        return productMapper.update(productDto);
    }
    public boolean delete(int id){
        return productMapper.delete(id);
    }
}
