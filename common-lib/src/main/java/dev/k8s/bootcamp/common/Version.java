package dev.k8s.bootcamp.common;

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
public class Version extends AbstractEntity {
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
}
