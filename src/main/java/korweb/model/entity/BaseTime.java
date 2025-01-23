package korweb.model.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {
        @CreatedDate
        private LocalDateTime cdate; // 생성 날짜
        @LastModifiedDate
        private LocalDateTime udate; // 수정 날짜
}
