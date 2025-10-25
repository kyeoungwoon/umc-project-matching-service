package kr.kyeoungwoon.upms.domain.infra.aws.s3.repository;

import java.util.List;
import java.util.Optional;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.entity.S3File;
import kr.kyeoungwoon.upms.domain.infra.aws.s3.enums.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FileRepository extends JpaRepository<S3File, Long> {

  Optional<S3File> findByS3Key(String s3Key);

  Optional<S3File> findByStoredFileName(String storedFileName);

  List<S3File> findByStatus(FileStatus status);
}