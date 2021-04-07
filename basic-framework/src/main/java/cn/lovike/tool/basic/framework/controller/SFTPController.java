package cn.lovike.tool.basic.framework.controller;

import cn.lovike.tool.basic.framework.common.util.SFTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lovike
 * @since 2021/4/7
 */
@RestController
public class SFTPController {

    private transient Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SFTPUtil sftpUtil;

    @Value("${sftp.directory}")
    private String saveDirectory;

    public static final String document = "path";

    private void upload(MultipartFile file) {
        if (file != null) {
            // 上传附件到sftp服务器
            sftpUtil.login();
            try {
                String fakeUniqueStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_"));
                sftpUtil.upload(saveDirectory + File.separator + document, fakeUniqueStr + file.getOriginalFilename(), file.getInputStream());
            } catch (Exception e) {
                log.warn("upload error, fileName: {}", file.getOriginalFilename(), e);
            } finally {
                sftpUtil.logout();
            }
        }
    }
}
