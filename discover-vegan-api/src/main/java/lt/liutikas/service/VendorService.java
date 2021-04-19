package lt.liutikas.service;

import lt.liutikas.assembler.VendorAssembler;
import lt.liutikas.dto.CreateVendorDto;
import lt.liutikas.entity.Vendor;
import lt.liutikas.entity.VendorProduct;
import lt.liutikas.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class VendorService {

    private static final Logger LOG = LoggerFactory.getLogger(VendorService.class);

    private final VendorRepository vendorRepository;
    private final VendorAssembler vendorAssembler;

    public VendorService(VendorRepository vendorRepository, VendorAssembler vendorAssembler) {
        this.vendorRepository = vendorRepository;
        this.vendorAssembler = vendorAssembler;
    }

    public List<Vendor> getVendors() {

        LOG.info(String.format("Returned vendors for location {latitude: %s, longitude: %s}", "XXXX", "YYYYY"));

        return vendorRepository.findAll();
    }

    public List<VendorProduct> getProducts(Integer id) {

        LOG.info(String.format("Returned products for vendor {id: %d}", id));

        return Arrays.asList(
                new VendorProduct() {{
                    setId(1);
                    setName("Tofu A");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/991-home_default/organic-tofu-picknicker-50g-viana.jpg");
                    setPrice(4.19f);
                }},
                new VendorProduct() {{
                    setId(2);
                    setName("Tofu B");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/147-home_default/ekologiskas-keptas-tempeh.jpg");
                    setPrice(4.19f);
                }},
                new VendorProduct() {{
                    setId(3);
                    setName("Tofu C");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/839-home_default/ekologiskas-fermentuotas-tofu-su-laiskiniais-cesnakais-130g-lord-of-tofu.jpg");
                    setPrice(4.19f);
                }},
                new VendorProduct() {{
                    setId(4);
                    setName("Tofu D");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/549-home_default/ekologiskas-silkinis-tofu.jpg");
                    setPrice(4.19f);
                }},
                new VendorProduct() {{
                    setId(5);
                    setName("Tofu E");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/991-home_default/organic-tofu-picknicker-50g-viana.jpg");
                    setPrice(4.19f);
                }},
                new VendorProduct() {{
                    setId(6);
                    setName("Tofu F");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/147-home_default/ekologiskas-keptas-tempeh.jpg");
                    setPrice(4.19f);
                }}
        );
    }

    public Vendor createVendor(CreateVendorDto createVendorDto) {
        Vendor vendor = vendorAssembler.assemblerVendor(createVendorDto);

        vendor = vendorRepository.save(vendor);

        return vendor;
    }
}
