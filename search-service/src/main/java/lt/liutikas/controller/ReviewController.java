package lt.liutikas.controller;

import lt.liutikas.dto.CreateReviewDto;
import lt.liutikas.dto.ReviewDto;
import lt.liutikas.service.ReviewService;
import lt.liutikas.utility.IsAuthorized;
import lt.liutikas.utility.TokenUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final TokenUtil tokenUtil;

    public ReviewController(ReviewService reviewService, TokenUtil tokenUtil) {
        this.reviewService = reviewService;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping
    @IsAuthorized
    public ReviewDto createReview(@RequestBody CreateReviewDto createReviewDto,
                                  @RequestHeader("Authorization") String token) {
        String userId = tokenUtil.getValue(token, "userId");
        return reviewService.createReview(createReviewDto, userId);
    }
}
