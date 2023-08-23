package com.example.freesale.cotrollers;

import com.example.freesale.entities.ProductEntity;
import com.example.freesale.entities.UserEntity;
import com.example.freesale.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String getAllProducts(@RequestParam(name = "searchWord", required = false) String title, Principal principal,
                                 Model model) {
        model.addAttribute("products", productService.getProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("searchWord", title);

        return "/product/all_products";
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") Long id, Model model, Principal principal) {
        ProductEntity product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("photos", product.getPhotos());
        model.addAttribute("authorProduct", product.getUser());


        return "/product/product";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3,
                                ProductEntity product, Principal principal) throws IOException {
        productService.save(principal, product, file1, file2, file3);

        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        productService.delete(productService.getUserByPrincipal(principal), id);

        return "redirect:/my/products";
    }

    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        UserEntity user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "product/my_product";
    }
}
