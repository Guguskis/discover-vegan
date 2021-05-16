package lt.liutikas.assembler;

import lt.liutikas.dto.ReviewDto;
import lt.liutikas.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewAssembler {

    public ReviewDto assembleReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setReviewId(review.getId());
        reviewDto.setCreatedAt(review.getCreatedAt());
        reviewDto.setReviewType(review.getReviewType());

        return reviewDto;
    }

}
