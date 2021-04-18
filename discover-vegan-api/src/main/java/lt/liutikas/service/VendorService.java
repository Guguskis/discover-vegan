package lt.liutikas.service;

import lt.liutikas.entity.Product;
import lt.liutikas.entity.Vendor;
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

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> getVendors() {

        LOG.info(String.format("Returned vendors for location {latitude: %s, longitude: %s}", "XXXX", "YYYYY"));

        return Arrays.asList(
                new Vendor() {{
                    setId(1);
                    setName("IKI - PLYTINĖS");
                    setImageUrl("https://lh5.googleusercontent.com/p/AF1QipMwXdfDl8SJsX2yUyqVVU3TcYKgGNyxn3YyqThD=w408-h725-k-no");
                    setLatitude(54.725215315954124);
                    setLongitude(25.342470671792796);
                }},
                new Vendor() {{
                    setId(2);
                    setName("Maxima XXX");
                    setImageUrl("https://lh5.googleusercontent.com/p/AF1QipMwXdfDl8SJsX2yUyqVVU3TcYKgGNyxn3YyqThD=w408-h725-k-no");
                    setLatitude(54.72744555070343);
                    setLongitude(25.341746138622348);
                }},
                new Vendor() {{
                    setId(3);
                    setName("Rimi");
                    setImageUrl("https://lh5.googleusercontent.com/p/AF1QipMwXdfDl8SJsX2yUyqVVU3TcYKgGNyxn3YyqThD=w408-h725-k-no");
                    setLatitude(54.72298451304856);
                    setLongitude(25.348183440258097);
                }},
                new Vendor() {{
                    setId(4);
                    setName("Norfa");
                    setImageUrl("https://lh5.googleusercontent.com/p/AF1QipMwXdfDl8SJsX2yUyqVVU3TcYKgGNyxn3YyqThD=w408-h725-k-no");
                    setLatitude(54.716093723400704);
                    setLongitude(25.33977203278708);
                }}
        );
    }

    public List<Product> getProducts(Integer id) {

        LOG.info(String.format("Returned products for vendor {id: %d}", id));

        return Arrays.asList(
                new Product() {{
                    setId(1);
                    setName("Tofu A");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/991-home_default/organic-tofu-picknicker-50g-viana.jpg");
                    setPrice(4.19f);
                }},
                new Product() {{
                    setId(2);
                    setName("Tofu B");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/147-home_default/ekologiskas-keptas-tempeh.jpg");
                    setPrice(4.19f);
                }},
                new Product() {{
                    setId(3);
                    setName("Tofu C");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/839-home_default/ekologiskas-fermentuotas-tofu-su-laiskiniais-cesnakais-130g-lord-of-tofu.jpg");
                    setPrice(4.19f);
                }},
                new Product() {{
                    setId(4);
                    setName("Tofu D");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/549-home_default/ekologiskas-silkinis-tofu.jpg");
                    setPrice(4.19f);
                }},
                new Product() {{
                    setId(5);
                    setName("Tofu E");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/991-home_default/organic-tofu-picknicker-50g-viana.jpg");
                    setPrice(4.19f);
                }},
                new Product() {{
                    setId(6);
                    setName("Tofu F");
                    setDescription("a lot of protein, low sugar, healthy");
                    setImageUrl("https://www.veggo.lt/147-home_default/ekologiskas-keptas-tempeh.jpg");
                    setPrice(4.19f);
                }}
        );
    }
}
