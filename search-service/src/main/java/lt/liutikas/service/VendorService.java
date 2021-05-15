package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.*;
import lt.liutikas.model.*;
import lt.liutikas.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final MongoVendorRepository mongoVendorRepository;
    private final VendorProductRepository vendorProductRepository;
    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;
    private final MongoProductRepository mongoProductRepository;
    private final MongoVendorProductRepository mongoVendorProductRepository;

    public VendorService(VendorAssembler vendorAssembler, VendorProductAssembler vendorProductAssembler, VendorRepository vendorRepository, MongoVendorRepository mongoVendorRepository, VendorProductRepository vendorProductRepository, ProductRepository productRepository, PlaceRepository placeRepository, MongoProductRepository mongoProductRepository, MongoVendorProductRepository mongoVendorProductRepository) {
        this.vendorAssembler = vendorAssembler;
        this.vendorProductAssembler = vendorProductAssembler;
        this.vendorRepository = vendorRepository;
        this.mongoVendorRepository = mongoVendorRepository;
        this.vendorProductRepository = vendorProductRepository;
        this.productRepository = productRepository;
        this.placeRepository = placeRepository;
        this.mongoProductRepository = mongoProductRepository;
        this.mongoVendorProductRepository = mongoVendorProductRepository;
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

        List<MongoVendor> vendors = mongoVendorRepository.findByExternalPlaceIdIn(placesIds);
        List<MongoVendor> newVendors = createVendorsForNewPlaces(vendors, places);

        vendors.addAll(newVendors);

        List<VendorDto> vendorDtos = vendors.stream()
                .map(vendorAssembler::assembleVendor)
                .collect(Collectors.toList());

        LOG.info(String.format("Returned vendors for location {latitude: %f, longitude: %f}",
                location.getLat(), location.getLng()));

        return vendorDtos;
    }

    private List<MongoVendor> createVendorsForNewPlaces(List<MongoVendor> vendors, List<Place> places) {
        List<String> externalIds = vendors.stream()
                .map(MongoVendor::getExternalPlaceId)
                .collect(Collectors.toList());

        List<Place> newPlaces = places.stream()
                .filter(place -> !externalIds.contains(place.getPlace_id()))
                .collect(Collectors.toList());

        List<MongoVendor> newVendors = newPlaces.stream()
                .map(vendorAssembler::assembleVendor)
                .collect(Collectors.toList());

        return mongoVendorRepository.saveAll(newVendors);
    }

    public VendorProductPageDto getProducts(String vendorId, PageRequest pageRequest) {

        MongoVendor vendor = assertVendorFound(vendorId);

        Page<MongoVendorProduct> vendorProductPage = mongoVendorProductRepository.findAllByVendor(vendor, pageRequest);
        Pageable nextVendorProductPage = vendorProductPage.nextPageable();
        List<VendorProductDto> products = vendorProductPage.get()
                .map(vendorProductAssembler::assembleVendorProductDto)
                .collect(Collectors.toList());

        VendorProductPageDto vendorProductPageDto = new VendorProductPageDto();
        vendorProductPageDto.setProducts(products);

        if (nextVendorProductPage.isPaged()) {
            vendorProductPageDto.setNextPageToken(nextVendorProductPage.getPageNumber());
        }

        LOG.info(String.format("Returned products for vendor {vendorId: %s}", vendorId));

        return vendorProductPageDto;
    }

    public VendorProductDto createProduct(String userId, String vendorId, CreateVendorProductDto createVendorProductDto) {

        MongoVendor vendor = assertVendorFound(vendorId);
        MongoProduct product = assertProductFound(createVendorProductDto.getProductId());

        MongoVendorProduct vendorProduct = vendorProductAssembler.assembleVendorProduct(userId, createVendorProductDto, vendor, product);

        vendorProduct = mongoVendorProductRepository.save(vendorProduct);

        return vendorProductAssembler.assembleVendorProductDto(vendorProduct);
    }

    private MongoProduct assertProductFound(String productId) {
        Optional<MongoProduct> product = mongoProductRepository.findById(productId);

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %s}", productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }
        return product.get();
    }

    public VendorProductDto patchProduct(String userId, String vendorId, String productId, PatchVendorProductDto patchVendorProductDto) {

        MongoVendor vendor = assertVendorFound(vendorId);
        MongoProduct product = assertProductFound(productId);

        Optional<MongoVendorProduct> vendorProductOptional = mongoVendorProductRepository.findByProductAndVendor(product, vendor);

        if (vendorProductOptional.isEmpty()) {
            String message = String.format("Vendor product not found {vendorId: %s, productId: %s}", vendorId, productId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        MongoVendorProduct vendorProduct = vendorProductOptional.get();

        if (patchVendorProductDto.getPrice() != null) {
            MongoVendorProductChange vendorProductChange = new MongoVendorProductChange();
            vendorProductChange.setPrice(patchVendorProductDto.getPrice());
            vendorProductChange.setUserId(userId);
            vendorProductChange.setCreatedAt(LocalDateTime.now());
            vendorProduct.getVendorProductChanges().add(vendorProductChange);
        }

        vendorProduct = mongoVendorProductRepository.save(vendorProduct);

        LOG.info(String.format("Patched vendor product {vendorId: %s, productId: %s}", vendorId, productId));

        return vendorProductAssembler.assembleVendorProductDto(vendorProduct);
    }

    private MongoVendor assertVendorFound(String vendorId) {
        Optional<MongoVendor> vendor = mongoVendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %d}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        return vendor.get();
    }
}
