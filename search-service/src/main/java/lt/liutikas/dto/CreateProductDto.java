package lt.liutikas.dto;

import javax.validation.constraints.NotBlank;

public class CreateProductDto {

    @NotBlank
    private String name;
    @NotBlank
    private String imageUrl;
    @NotBlank
    private String producer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }
}
