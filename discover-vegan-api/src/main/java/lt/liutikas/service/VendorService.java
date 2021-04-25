package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.*;
import lt.liutikas.entity.Product;
import lt.liutikas.entity.Vendor;
import lt.liutikas.entity.VendorProduct;
import lt.liutikas.entity.VendorType;
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

    public List<Vendor> getVendors(GetVendorDto getVendorDto) {
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

        LOG.info(String.format("Returned vendors for location {latitude: %f, longitude: %f}",
                location.getLat(), location.getLng()));

        return vendors;
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

        Optional<Vendor> vendor = vendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %d}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }


        Page<VendorProduct> vendorProductPage = vendorProductRepository.findAllByVendor(vendor.get(), pageRequest);
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

    public Vendor createVendor(CreateVendorDto createVendorDto) {
        Vendor vendor = vendorAssembler.assembleVendor(createVendorDto);

        vendor = vendorRepository.save(vendor);

        return vendor;
    }

    public VendorProductDto createProduct(Integer vendorId, CreateVendorProductDto createVendorProductDto) {

        Optional<Vendor> vendor = vendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %d}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }

        Optional<Product> product = productRepository.findById(createVendorProductDto.getProductId());

        if (product.isEmpty()) {
            String message = String.format("Product not found {productId: %d}", createVendorProductDto.getProductId());
            LOG.error(message);
            throw new NotFoundException(message);
        }

        VendorProduct vendorProduct = vendorProductAssembler.assembleVendorProduct(createVendorProductDto, vendor.get(), product.get());

        vendorProduct = vendorProductRepository.save(vendorProduct);

        return vendorProductAssembler.assembleVendorProductDto(vendorProduct);
    }

}
