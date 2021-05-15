package lt.liutikas.controller;

import lt.liutikas.dto.*;
import lt.liutikas.model.VendorType;
import lt.liutikas.service.VendorService;
import lt.liutikas.utility.IsAuthorized;
import lt.liutikas.utility.TokenUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private final VendorService vendorService;
    private final TokenUtil tokenUtil;

    public VendorController(VendorService vendorService, TokenUtil tokenUtil) {
        this.vendorService = vendorService;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping
    public ResponseEntity<List<VendorDto>> getVendors(@RequestParam Double latitude,
                                                      @RequestParam Double longitude,
                                                      @RequestParam(required = false) VendorType type) {
        Location location = new Location();
        location.setLat(latitude);
        location.setLng(longitude);
        GetVendorDto getVendorDto = new GetVendorDto();
        getVendorDto.setLocation(location);
        getVendorDto.setType(type);

        return ResponseEntity.ok(vendorService.getVendors(getVendorDto));
    }

    @GetMapping("/{vendorId}/product")
    public ResponseEntity<VendorProductPageDto> getProducts(@PathVariable Integer vendorId,
                                                            @Min(0) @RequestParam(value = "pageToken", defaultValue = "0") Integer pageToken,
                                                            @Min(1) @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageToken, pageSize);
        return ResponseEntity.ok(vendorService.getProducts(vendorId, pageRequest));
    }

    @PostMapping("/{vendorId}/product")
    @IsAuthorized
    public ResponseEntity<VendorProductDto> createProduct(@PathVariable Integer vendorId,
                                                          @RequestBody @Valid CreateVendorProductDto createVendorProductDto,
                                                          @RequestHeader("Authorization") String token) {

        String userId = tokenUtil.getValue(token, "userId");
        return ResponseEntity.ok(vendorService.createProduct(Integer.parseInt(userId), vendorId, createVendorProductDto));
    }

    @PatchMapping("/{vendorId}/product/{productId}")
    @IsAuthorized
    public ResponseEntity<VendorProductDto> patchProduct(@PathVariable Integer vendorId,
                                                         @PathVariable Integer productId,
                                                         @RequestBody @Valid PatchVendorProductDto patchVendorProductDto,
                                                         @RequestHeader("Authorization") String token
    ) {
        String userId = tokenUtil.getValue(token, "userId");
        return ResponseEntity.ok(vendorService.patchProduct(Integer.parseInt(userId), vendorId, productId, patchVendorProductDto));
    }

}
