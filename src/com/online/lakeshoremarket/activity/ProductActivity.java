package com.online.lakeshoremarket.activity;

import java.util.ArrayList;

import com.online.lakeshoremarket.domain.ProductDomain;
import com.online.lakeshoremarket.model.product.ProdImpl;
import com.online.lakeshoremarket.model.product.Product;
import com.online.lakeshoremarket.representation.generic.GenericResponse;
import com.online.lakeshoremarket.representation.generic.Link;
import com.online.lakeshoremarket.representation.product.ProductRepresentation;
import com.online.lakeshoremarket.representation.product.ProductRequest;
import com.online.lakeshoremarket.util.Constant;

public class ProductActivity {

	public ArrayList<ProductRepresentation> getProducts(String prodName){
		
		ArrayList<Product> prodList = new ArrayList<Product>();
		ArrayList<ProductRepresentation> prodRepresentationList = new ArrayList<ProductRepresentation>();
		ProductDomain prodDomain = new ProductDomain();
		prodList = prodDomain.searchProductByLikeName(prodName);
		for(int i=0; i< prodList.size() ; i++){
			ProductRepresentation productRepresentation = new ProductRepresentation();
			productRepresentation.setActive(prodList.get(i).isActive());
			productRepresentation.setDescription(prodList.get(i).getDescription());
			productRepresentation.setPartnerID(prodList.get(i).getPartnerID());
			productRepresentation.setPrice(prodList.get(i).getPrice());
			productRepresentation.setProductID(prodList.get(i).getProductID());
			productRepresentation.setProductName(prodList.get(i).getProductName());
			productRepresentation.setTaxonomyID(prodList.get(i).getTaxonomyID());
			Link get = new Link("Get Product Details", Constant.LSM_COMMON_URL + "/product-by-id/" + prodList.get(i).getProductID(), "application/xml");
			productRepresentation.setLinks(get);
			prodRepresentationList.add(productRepresentation);
		}
		if(prodRepresentationList != null && prodRepresentationList.size() != 0){
			return prodRepresentationList;
		}else{
			return null;
		}
	}
	
	public GenericResponse createProduct(ProductRequest prodRequest){
		GenericResponse genericResponse = new GenericResponse();
		Product prodNew  = new ProdImpl();
		prodNew.setPartnerID(prodRequest.getPartnerID());
		prodNew.setTaxonomyID(prodRequest.getTaxonomyID());
		prodNew.setCost(prodRequest.getCost());
		prodNew.setPrice(prodRequest.getPrice());
		prodNew.setProductName(prodRequest.getProductName());
		prodNew.setDescription(prodRequest.getDescription());
		prodNew.setQoh(prodRequest.getQoh());
		prodNew.setActive(true);
		
		ProductDomain prodDomain = new ProductDomain();
		
		int productID = 0;
		productID = prodDomain.addProduct(prodNew);
		
		if(0 != productID){
			genericResponse.setMessage("Product is created");
			genericResponse.setSuccess(true);
			Link get = new Link("Get Product Details", Constant.LSM_COMMON_URL + "/product-by-id/" + productID, "application/xml");
			genericResponse.setLinks(get);
		}else{
			genericResponse.setMessage("Product is not created");
			genericResponse.setSuccess(false);
		}
		
		return genericResponse;
	}
	
	public ProductRepresentation getProduct(String prodName){
		ProductDomain prodDomain = new ProductDomain();
		Product product = new ProdImpl();
		product = prodDomain.searchProductByName(prodName);
		if(product != null && product.getProductID() != 0){
			ProductRepresentation productRepresentation = new ProductRepresentation();
			productRepresentation.setProductName(product.getProductName());
			productRepresentation.setDescription(product.getDescription());
			productRepresentation.setActive(product.isActive());
			productRepresentation.setPartnerID(product.getPartnerID());
			productRepresentation.setPrice(product.getPrice());
			productRepresentation.setProductID(product.getProductID());
			productRepresentation.setTaxonomyID(product.getTaxonomyID());
			Link check = new Link("Check Product Availability", Constant.LSM_COMMON_URL + "/available/"+product.getProductID(), "application/xml");
			Link review = new Link("Create Product Review", Constant.LSM_COMMON_URL + "/review/product", "application/xml");
			Link getReview = new Link("Get Product Reviews", Constant.LSM_COMMON_URL + "/review/product/"+product.getProductID(), "application/xml");
			Link buy = new Link("Buy Product", Constant.LSM_COMMON_URL + "/order", "application/xml");
			productRepresentation.setLinks(check,review,getReview,buy);
			return productRepresentation;
		}else{
			return null;
		}
		
	}
	
	public GenericResponse checkProductAvailability(String productIDString) { 
		boolean isProductAvailable = false;
		ProductDomain prodDomain = new ProductDomain();
		isProductAvailable = prodDomain.checkProductAvailabilityByID(Integer.parseInt(productIDString));
		
		GenericResponse genericResponse = new GenericResponse();
		if(isProductAvailable){
			genericResponse.setMessage("Product is available");
			genericResponse.setSuccess(true);
			Link buy = new Link("Buy Product", Constant.LSM_COMMON_URL + "/order", "application/xml");
			genericResponse.setLinks(buy);
		}else{
			genericResponse.setMessage("Product is not available");
			genericResponse.setSuccess(false);
		}		
		return genericResponse;
	}
	
	public ProductRepresentation getProductByID(String ProductIDString){
		ProductDomain prodDomain = new ProductDomain();
		Product product = new ProdImpl();
		product = prodDomain.getProductByID(Integer.parseInt(ProductIDString));
		if(product == null){
			return null;
		}else{
			ProductRepresentation productRepresentation = new ProductRepresentation();
			productRepresentation.setProductName(product.getProductName());
			productRepresentation.setDescription(product.getDescription());
			productRepresentation.setActive(product.isActive());
			productRepresentation.setPartnerID(product.getPartnerID());
			productRepresentation.setPrice(product.getPrice());
			productRepresentation.setProductID(product.getProductID());
			productRepresentation.setTaxonomyID(product.getTaxonomyID());
			Link check = new Link("Check Product Availability", Constant.LSM_COMMON_URL + "/available/"+product.getProductID(), "application/xml");
			Link review = new Link("Create Product Review", Constant.LSM_COMMON_URL + "/review/product", "application/xml");
			Link getReview = new Link("Get Product Reviews", Constant.LSM_COMMON_URL + "/review/product/"+product.getProductID(), "application/xml");
			Link buy = new Link("Buy Product", Constant.LSM_COMMON_URL + "/order", "application/xml");
			productRepresentation.setLinks(check,review,getReview,buy);
			
			return productRepresentation;
		}
	}
}
