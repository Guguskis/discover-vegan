package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.assembler.VendorProductAssembler;
import lt.liutikas.configuration.exception.NotFoundException;
import lt.liutikas.dto.CreateVendorDto;
import lt.liutikas.dto.CreateVendorProductDto;
import lt.liutikas.dto.VendorProductDto;
import lt.liutikas.entity.Product;
import lt.liutikas.entity.Vendor;
import lt.liutikas.entity.VendorProduct;
import lt.liutikas.repository.ProductRepository;
import lt.liutikas.repository.VendorProductRepository;
import lt.liutikas.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public VendorService(VendorRepository vendorRepository, VendorAssembler vendorAssembler, VendorProductAssembler vendorProductAssembler, VendorProductRepository vendorProductRepository, ProductRepository productRepository) {
        this.vendorRepository = vendorRepository;
        this.vendorAssembler = vendorAssembler;
        this.vendorProductAssembler = vendorProductAssembler;
        this.vendorProductRepository = vendorProductRepository;
        this.productRepository = productRepository;
    }

    public List<Vendor> getVendors() {

        LOG.info(String.format("Returned vendors for location {latitude: %s, longitude: %s}", "XXXX", "YYYYY"));

        return vendorRepository.findAll();
    }

    public List<VendorProductDto> getProducts(Integer vendorId) {

        Optional<Vendor> vendor = vendorRepository.findById(vendorId);

        if (vendor.isEmpty()) {
            String message = String.format("Vendor not found {vendorId: %d}", vendorId);
            LOG.error(message);
            throw new NotFoundException(message);
        }


        List<VendorProduct> vendorProducts = vendorProductRepository.findAllByVendor(vendor.get());

        LOG.info(String.format("Returned products for vendor {vendorId: %d}", vendorId));

        return vendorProducts.stream()
                .map(vendorProductAssembler::assembleVendorProductDto)
                .collect(Collectors.toList());
    }

    public Vendor createVendor(CreateVendorDto createVendorDto) {
        Vendor vendor = vendorAssembler.assemblerVendor(createVendorDto);

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
