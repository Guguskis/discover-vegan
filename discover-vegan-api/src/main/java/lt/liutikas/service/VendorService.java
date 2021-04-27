package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.*;
import lt.liutikas.model.Product;
import lt.liutikas.model.Vendor;
import lt.liutikas.model.VendorProduct;
import lt.liutikas.model.VendorType;
import lt.liutikas.repository.PlaceRepository;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorService {

    private static final Logger LOG = LoggerFactory.getLogger(VendorService.class);

    private final VendorAssembler vendorAssembler;
    private final VendorProductAssembler vendorProductAssembler;

    private final VendorRepository vendorRepository;
    private final VendorProductRepository vendorProductRepository;
    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;

    public VendorService(VendorAssembler vendorAssembler, VendorProductAssembler vendorProductAssembler, VendorRepository vendorRepository, VendorProductRepository vendorProductRepository, ProductRepository productRepository, PlaceRepository placeRepository) {
        this.vendorAssembler = vendorAssembler;
        this.vendorProductAssembler = vendorProductAssembler;
        this.vendorRepository = vendorRepository;
        this.vendorProductRepository = vendorProductRepository;
        this.productRepository = productRepository;
        this.placeRepository = placeRepository;
    }

    public List<VendorDto> getVendors(GetVendorDto getVendorDto) {
        Location location = getVendorDto.getLocation();
        VendorType vendorType = getVendorDto.getType();

        List<Place> places = new ArrayList<>();

        if (vendorType == null) {
            places.addAll(placeRepository.getFoodPlaces(location, VendorType.STORE));
            places.addAll(placeRepository.getFoodPlaces(location, VendorType.RESTAURANT));
        } else {
            if (vendorType == VendorType.STORE) {
                places.addAll(placeRepository.getFoodPlaces(location, VendorType.STORE));
            }
            if (vendorType == VendorType.RESTAURANT) {
                places.addAll(placeRepository.getFoodPlaces(location, VendorType.RESTAURANT));
            }
        }

        List<String> placesIds = places.stream()
                .map(Place::getPlace_id)
                .collect(Collectors.toList());

        List<Vendor> vendors = vendorRepository.findByExternalPlaceIdIn(placesIds);
        List<Vendor> newVendors = createVendorsForNewPlaces(vendors, places);

        vendors.addAll(newVendors);

        List<VendorDto> vendorDtos = vendors.stream()
                .map(vendorAssembler::assembleVendor)
                .collect(Collectors.toList());

        LOG.info(String.format("Returned vendors for location {latitude: %f, longitude: %f}",
                location.getLat(), location.getLng()));

        return vendorDtos;
    }

    private List<Vendor> createVendorsForNewPlaces(List<Vendor> vendors, List<Place> places) {
        List<String> vendorsIds = vendors.stream()
                .map(Vendor::getExternalPlaceId)
                .collect(Collectors.toList());

        List<Place> newPlaces = places.stream()
                .filter(place -> !vendorsIds.contains(place.getPlace_id()))
                .collect(Collectors.toList());

        List<Vendor> newVendors = newPlaces.stream()
                .map(vendorAssembler::assembleVendor)
                .collect(Collectors.toList());

        return vendorRepository.saveAll(newVendors);
    }

    public VendorProductPageDto getProducts(Integer vendorId, PageRequest pageRequest) {

        Vendor vendor = assertVendorFound(vendorId);

        Page<VendorProduct> vendorProductPage = vendorProductRepository.findAllByVendor(vendor, pageRequest);
        Pageable nextVendorProductPage = vendorProductPage.nextPageable();
        List<VendorProductDto> products = vendorProductPage.get()
                .map(vendorProductAssembler::assembleVendorProductDto)
                .collect(Collectors.toList());

        VendorProductPageDto vendorProductPageDto = new VendorProductPageDto();
        vendorProductPageDto.setProducts(products);

        if (nextVendorProductPage.isPaged()) {
            vendorProductPageDto.setNextPageToken(nextVendorProductPage.getPageNumber());
        }

        LOG.info(String.format("Returned products for vendor {vendorId: %d}", vendorId));

        return vendorProductPageDto;
    }

    public VendorProductDto createProduct(Integer vendorId, CreateVendorProductDto createVendorProductDto) {

        Vendor vendor = assertVendorFound(vendorId);
        Product product = assertProductFound(createVendorProductDto.getProductId());

        VendorProduct vendorProduct = vendorProductAssembler.assembleVendorProduct(createVendorProductDto, vendor, product);

        vendorProduct = vendorProductRepository.save(vendorProduct);

        return vendorProductAssembler.assembleVendorProductDto(vendorProduct);
    }

    private Product assertProductFound(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %d}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }
        return product.get();
    }

    public VendorProductDto patchProduct(Integer vendorId, Integer productId, PatchVendorProductDto patchVendorProductDto) {

        Vendor vendor = assertVendorFound(vendorId);
        assertProductFound(productId);

        List<VendorProduct> vendorProducts = vendorProductRepository.findAllByVendor(vendor);

        Optional<VendorProduct> vendorProductOptional = vendorProducts.stream().filter(vendorProductT -> vendorProductT
                .getProduct()
                .getProductId()
                .equals(productId))
                .findFirst();

        if (vendorProductOptional.isEmpty()) {
            String message = String.format("Vendor product not found {vendorId: %d, productId: %d}", vendorId, productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        VendorProduct vendorProduct = vendorProductOptional.get();

        if (patchVendorProductDto.getPrice() != null) {
            vendorProduct.setPrice(patchVendorProductDto.getPrice());
        }

        vendorProduct = vendorProductRepository.save(vendorProduct);

        LOG.info(String.format("Patched vendor product {vendorId: %d, productId: %d}", vendorId, productId));

        return vendorProductAssembler.assembleVendorProductDto(vendorProduct);
    }

    private Vendor assertVendorFound(Integer vendorId) {
        Optional<Vendor> vendor = vendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %d}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        return vendor.get();
    }
}
