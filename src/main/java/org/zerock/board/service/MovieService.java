package org.zerock.board.service;

import org.springframework.data.domain.PageRequest;
import org.zerock.board.dto.MovieDTO;
import org.zerock.board.dto.MovieImageDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Movie;
import org.zerock.board.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    Long register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO); // 목록 처리

    MovieDTO getMovie(Long mno); // 조회

    // Movie 엔티티와 그와 관련된 정보를 MovieDTO로 변환하는 메서드
    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCnt) {
        // Movie 엔티티를 기반으로 MovieDTO 객체를 생성
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno()) // 영화 번호
                .title(movie.getTitle()) // 영화 제목
                .regDate(movie.getRegDate()) // 등록 일자
                .modDate(movie.getModDate()) // 수정 일자
                .build();

        // movieImages 리스트에서 각각의 MovieImage 엔티티를 기반으로 MovieImageDTO 객체로 변환
        List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage -> {
            // 이 과정에서 밑에 세 개를 가져와 MovieImageDTO를 생성하고 이를 모아 리스트로 만듦
            return MovieImageDTO.builder().imgName(movieImage.getImgName()) // 이미지 이름
                    .path(movieImage.getPath()) // 이미지 경로
                    .uuid(movieImage.getUuid()) // uuid
                    .build();
        }).collect(Collectors.toList());

        movieDTO.setImageDTOList(movieImageDTOList); // 앞서 생성한 이미지 리스트 설정
        movieDTO.setAvg(avg); // 평점 설정
        movieDTO.setReviewCnt(reviewCnt.intValue()); // 리뷰 수 설정(reviewCnt Long 형태 -> 정수형으로 변환)

        return movieDTO; // 설정한 movieDTO 객체를 반환함
    }

    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) { // Map 타입으로 반환

        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        entityMap.put("movie", movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        // MovieImageDTO 처리
        if(imageDTOList != null && imageDTOList.size() > 0) {
            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
                MovieImage movieImage = MovieImage.builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie)
                        .build();
                return movieImage;
            }).collect(Collectors.toList());
            entityMap.put("imgList", movieImageList);
            }
        return entityMap;
        }

}
