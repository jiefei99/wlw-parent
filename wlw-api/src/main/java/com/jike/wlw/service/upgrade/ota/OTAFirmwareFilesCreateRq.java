/**
 * 版权所有©，Rs自主完成，所有权利保留。
 * <p>
 * 修改历史：
 * 2023年03月21日 0:50 - ASUS - 创建。
 */
package com.jike.wlw.service.upgrade.ota;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * @author ASUS
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTAFirmwareFilesCreateRq implements Serializable {
    private List<MultipartFile> files;
}
