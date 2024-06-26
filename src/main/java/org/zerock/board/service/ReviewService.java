//package org.zerock.board.service;
//
//import org.zerock.board.dto.ReviewDTO;
//import org.zerock.board.entity.Member;
//import org.zerock.board.entity.Movie;
//import org.zerock.board.entity.Review;
//
//import java.util.List;
//
//public interface ReviewService {
//
//    //영화의 모든 영화리뷰를 가져옴
//    List<ReviewDTO> getListOfMovie(Long mno);
//
//    //영화 리뷰 추가
//    Long register(ReviewDTO movieReviewDTO);
//
//    //특정한 영화 리뷰 수정
//    void modify(ReviewDTO movieReviewDTO);
//
//    //영화 리뷰 삭제
//    void remove(Long reviewnum);
//
//    default Review dtoToEntity(ReviewDTO movieReviewDTO){ // dto 객체를 엔티티 객체로 변환
//
//        Review movieReview = Review.builder()
//                .reviewnum(movieReviewDTO.getReviewnum())
//                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
//                .member(Member.builder().mid(movieReviewDTO.getMid()).build())
//                .grade(movieReviewDTO.getGrade())
//                .text(movieReviewDTO.getText())
//                .build();
//
//        return movieReview;
//    }
//
//    default ReviewDTO entityToDto(Review movieReview){ // 엔티티 객체를 dto 객체로 뱐환
//
//        ReviewDTO movieReviewDTO = ReviewDTO.builder()
//                .reviewnum(movieReview.getReviewnum())
//                .mno(movieReview.getMovie().getMno())
//                .mid(movieReview.getMember().getMid())
//                .nickname(movieReview.getMember().getName())
//                .email(movieReview.getMember().getEmail())
//                .grade(movieReview.getGrade())
//                .text(movieReview.getText())
//                .regDate(movieReview.getRegDate())
//                .modDate(movieReview.getModDate())
//                .build();
//
//        return movieReviewDTO;
//    }
//}