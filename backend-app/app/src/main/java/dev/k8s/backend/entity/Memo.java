package dev.k8s.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "memo") // DB 테이블명
@Getter
@Setter
@NoArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "메모 ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id; // 기본키

    private String title;
    private String memo;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
    @Schema(description = "생성일시", example = "2025-08-29T12:34:56", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_date", insertable = false)
    @Schema(description = "수정일시", example = "2025-08-30T12:34:56", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateDate;

    public Memo(String title, String memo) {
        this.title = title;
        this.memo = memo;
    }
}
