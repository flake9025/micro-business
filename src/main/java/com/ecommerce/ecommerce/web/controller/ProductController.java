package com.ecommerce.ecommerce.web.controller;

import com.ecommerce.ecommerce.dao.ProductDao;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.web.exceptions.ProduitIntrouvableException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api( description="API pour es opérations CRUD sur les produits.")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    //Récupérer la liste des produits
    @RequestMapping(value="/Produits", method=RequestMethod.GET)
    public List<Product>listeProduits() {
        return productDao.findAll();
    }

    //Récupérer la liste des produits avec marges
    @RequestMapping(value="/AdminProduits", method=RequestMethod.GET)
    public Map<String, Integer> calculerMargeProduit () {
        return productDao.findAll().stream().collect(Collectors.toMap(Product::toString, p -> p.getPrix() - p.getPrixAchat()));
        //Map<String, Integer> productsPrices = new HashMap<>();
        //productDao.findAll().forEach(
        //        p->productsPrices.put(p.getNom(), p.getPrix() - p.getPrixAchat())
        //);
        //return productsPrices;
    }

    //Récupérer un produit par son Id
    @GetMapping(value="/Produits/{id}")
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    public Product afficherUnProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);

        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        return produit;
    }

    //ajouter un produit
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@RequestBody Product product) {

        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "test/produits/prix/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(400);
    }

    @GetMapping(value = "test/produits/nom/{recherche}")
    public List<Product> testeDeRequetes(@PathVariable String recherche) {
        return productDao.findByNomLike("%"+recherche+"%");
    }

    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {
        productDao.save(product);
    }

    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }
}
