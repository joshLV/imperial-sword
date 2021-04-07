package cn.lovike.tool.basic.framework.common.util;

import cn.lovike.tool.basic.framework.exception.BusException;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lovike
 * @since 2021/4/7
 */
@Component
public class SFTPUtil {
    private transient Logger log = LoggerFactory.getLogger(this.getClass());

    private ChannelSftp sftp;

    private Session session;

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private String port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.directory}")
    private String saveDirectory;

    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            log.info("sftp connect by host:{} username:{}", host, username);
            session = jsch.getSession(username, host, Integer.parseInt(port));
            log.info("Session is build");
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            log.info("Session is connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");

            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
            return;
        }
    }

    // 连接 sftp
    public void login(String host, String userName, String password, String port) {
        try {
            JSch jsch = new JSch();
            log.info("sftp connect by host:{} username:{}", host, userName);
            session = jsch.getSession(userName, host, Integer.parseInt(port));
            log.info("Session is build");
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect(30000);
            log.info("Session is connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");

            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (Exception e) {
            log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
            throw new BusException("服务无法连接");
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     * @throws SftpException
     * @throws Exception
     */
    public String upload(String directory, String sftpFileName, InputStream input) throws SftpException {
//        login();
        // 判断目录是否存在
        SftpATTRS attrs = null;
        try {
            attrs = sftp.stat(directory);
        } catch (Exception e) {
            log.info("directory=[{}] not exists", directory);
        }
        // 创建目录.最多只能创建一层目录。
        if (null == attrs) {
            try {
                log.info("create directory: {}", directory);
                sftp.mkdir(directory);
            } catch (SftpException e) {
                log.error("sftp mkdir error. please contact admin");
            }
        }
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        log.info("file:{} is upload successful", sftpFileName);
        return directory;
    }

    public String upload(String sourceDirectory, Boolean typeFlag, String sftpFileName, InputStream input) throws SftpException {
        // 判断目录是否存在
        SftpATTRS attrs = null;
        try {
            attrs = sftp.stat(sourceDirectory);
        } catch (Exception e) {
            log.info("directory=[{}] not exists", sourceDirectory);
        }
        // 创建目录.最多只能创建一层目录。
        if (null == attrs) {
            try {
                log.info("create directory: {}", sourceDirectory);
                sftp.mkdir(sourceDirectory);
            } catch (SftpException e) {
                log.error("sftp mkdir error. please contact admin");
            }
        }
        try {
            sftp.cd(sourceDirectory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            sftp.mkdir(sourceDirectory);
            sftp.cd(sourceDirectory);
        }

        if (typeFlag) {
            sftp.put(input, sftpFileName);
            log.info("file:{} is upload successful", sftpFileName);
        }

        return sourceDirectory;
    }

    /**
     * 上传单个文件
     *
     * @param directory  上传到sftp目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param byteArr      要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param dataStr      待上传的数据
     * @param charsetName  sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
        log.info("file:{} is download successful", downloadFile);
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        login();
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is       = sftp.get(downloadFile, new MyProgressMonitor());
        byte[]      fileData = IOUtils.toByteArray(is);
        log.info("file:{} is download successful", downloadFile);
        return fileData;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws SftpException {
//        login();
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    public void deleteDir(String directory) throws SftpException {
        sftp.rmdir(directory);
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @param directory
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    class MyProgressMonitor implements SftpProgressMonitor {
        private Logger logger = LoggerFactory.getLogger(getClass());

        private AtomicLong transferred = new AtomicLong(0);

        private AtomicInteger atomicInteger = new AtomicInteger(0);

        private Long totalSize;


        /**
         * 每4k 进来一次
         *
         * @param count
         * @return
         */
        @Override
        public boolean count(long count) {
            long countSize = transferred.addAndGet(count);
            int  step      = atomicInteger.incrementAndGet();
            if (step % 1000 == 0) {
                logger.info("Currently transferred total size: {} kB, Speed of progress : {} % ", countSize / 1024, ArithUtil.mul(ArithUtil.divRounding(countSize, totalSize, 10), 100));
            }
            //// 后面可以实现 限制下载速度， 暂停下载
            return true;
        }


        @Override
        public void end() {
            logger.info("Transferring done.");
        }

        @Override
        public void init(int op, String src, String dest, long max) {
            totalSize = max;
            logger.info("Transferring begin.");
        }
    }
}
