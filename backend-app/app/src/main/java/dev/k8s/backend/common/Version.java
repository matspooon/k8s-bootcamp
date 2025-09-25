package dev.k8s.backend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * application version info
 * resource 폴더의 versoin.json 파일의 내용을 매핑하기 위한 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class Version {
  /**
   * application image name
   */
  String image;
  /**
   * application version : yyyymmdd.buildnumber(jenkins build number)
   */
  String appVersion;
  /**
   * git commit sha
   */
  String commit;

  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return String.format("image: %s, appVersion: %s, commit: %s", image, appVersion, commit);
    }
  }
}
