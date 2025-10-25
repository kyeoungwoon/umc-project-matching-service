package kr.kyeoungwoon.upms.domain.challenger.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.kyeoungwoon.upms.domain.challenger.dto.SchoolDto;
import kr.kyeoungwoon.upms.domain.challenger.entity.School;
import kr.kyeoungwoon.upms.domain.challenger.repository.SchoolRepository;
import kr.kyeoungwoon.upms.global.apiPayload.code.status.ErrorStatus;
import kr.kyeoungwoon.upms.global.apiPayload.enums.DomainType;
import kr.kyeoungwoon.upms.global.apiPayload.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolService {

  private final SchoolRepository schoolRepository;

  @Transactional
  public SchoolDto.Response create(SchoolDto.CreateRequest request) {
    School school = School.builder()
        .name(request.name())
        .logoImageUrl(request.logoImageUrl())
        .build();

    School saved = schoolRepository.save(school);
    return toResponse(saved);
  }

  @Transactional
  public List<SchoolDto.Response> bulkCreate(SchoolDto.BulkCreateRequest request) {
    List<School> schools = request.schools().stream()
        .map(req -> School.builder()
            .name(req.name())
            .logoImageUrl(req.logoImageUrl())
            .build())
        .collect(Collectors.toList());

    List<School> savedSchools = schoolRepository.saveAll(schools);

    return savedSchools.stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  public SchoolDto.Response findById(Long id) {
    School school = schoolRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.SCHOOL, ErrorStatus.SCHOOL_NOT_FOUND));
    return toResponse(school);
  }

  // 학교 목록 조회, 전체 조회
  public List<SchoolDto.Response> findAll() {
    return schoolRepository.findAll()
        .stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public SchoolDto.Response update(Long id, SchoolDto.UpdateRequest request) {
    School school = schoolRepository.findById(id)
        .orElseThrow(() -> new DomainException(DomainType.SCHOOL, ErrorStatus.SCHOOL_NOT_FOUND));

    school.updateInfo(request.name(), request.logoImageUrl());

    return toResponse(school);
  }

  @Transactional
  public void delete(Long id) {
    if (!schoolRepository.existsById(id)) {
      throw new DomainException(DomainType.SCHOOL, ErrorStatus.SCHOOL_NOT_FOUND);
    }
    schoolRepository.deleteById(id);
  }

  private SchoolDto.Response toResponse(School school) {
    return SchoolDto.Response.builder()
        .id(school.getId())
        .name(school.getName())
        .logoImageUrl(school.getLogoImageUrl())
        .createdAt(school.getCreatedAt())
        .updatedAt(school.getUpdatedAt())
        .build();
  }
}
