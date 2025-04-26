package com.BallaDream.BallaDream.service;

import com.BallaDream.BallaDream.domain.enums.DiagnoseType;
import com.BallaDream.BallaDream.domain.enums.Level;
import com.BallaDream.BallaDream.domain.product.Element;
import com.BallaDream.BallaDream.domain.product.Guide;
import com.BallaDream.BallaDream.domain.product.Product;
import com.BallaDream.BallaDream.domain.product.ProductGuide;
import com.BallaDream.BallaDream.exception.data.DataIntegrityException;
import com.BallaDream.BallaDream.repository.product.ElementRepository;
import com.BallaDream.BallaDream.repository.product.GuideRepository;
import com.BallaDream.BallaDream.repository.product.ProductGuideRepository;
import com.BallaDream.BallaDream.repository.product.ProductRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitService {

    private final ElementRepository elementRepository;
    private final GuideRepository guideRepository;
    private final ProductGuideRepository productGuideRepository;
    private final ProductRepository productRepository;

    public <T> void readCSV(String classpath, Function<String[], T> mapper, JpaRepository<T, ?> repository) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(classpath);
             CSVReader csvReader = new CSVReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            if (is == null) {
                throw new FileNotFoundException("리소스를 찾을 수 없습니다: " + classpath);
            }

            String[] line;
            int idx = 0;
            List<T> result = new ArrayList<>();
            while ((line = csvReader.readNext()) != null) {
                if (idx != 0) {
                    result.add(mapper.apply(line));
                }
                idx++;
            }
            repository.saveAll(result);
            log.info("Saved {} rows from {}", result.size(), classpath);
        } catch (IOException | CsvValidationException e) {
            log.error("CSV 읽기 중 오류 발생: {}", e.getMessage());
        }
    }

//    @PostConstruct
    public void csvToDb() {
        readCSV("data/guide.csv", this::makeGuideWithLine, guideRepository);
        readCSV("data/product.csv", this::makeProductWithLine, productRepository);
        readCSV("data/element.csv", this::makeElementWithLine, elementRepository);
        readCSV("data/product_guide.csv", this::makeProductGuideWithLine, productGuideRepository);
    }

    //csv 파일의 행 순서대로 가져와서 객체를 생성해주는 메서드들
    private Guide makeGuideWithLine(String[] line) {
        return new Guide(Long.parseLong(line[0]), line[1], DiagnoseType.valueOf(line[2]),
                Level.valueOf(line[3]));
    }

    private Product makeProductWithLine(String[] line) {
        return new Product(Long.parseLong(line[0]), line[1], Integer.parseInt(line[2]), line[3], line[4], line[5]);
    }

    private ProductGuide makeProductGuideWithLine(String[] line) {
        Product product = productRepository.findById(Long.parseLong(line[1])).orElseThrow(
                () -> new DataIntegrityException("product_guide 데이터베이스 삽입중에 product_id 외래키를 찾을 수 없음"));

        Guide guide = guideRepository.findById(Long.parseLong(line[2])).orElseThrow(
                () -> new DataIntegrityException("product_guide 데이터베이스 삽입중에 guide_id 외래키를 찾을 수 없음"));

        return new ProductGuide(Long.parseLong(line[0]), product, guide);
    }

    private Element makeElementWithLine(String[] line) {
        Product product = productRepository.findById(Long.parseLong(line[2])).orElseThrow(
                () -> new DataIntegrityException("element 데이터베이스 삽입중에 product_id 외래키를 찾을 수 없음"));

        return new Element(Long.parseLong(line[0]), line[1], product);
    }
}
