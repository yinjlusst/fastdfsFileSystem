package com.yjl.filesystem;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
class FilesystemApplicationTests {

    @Test
    public void contextLoads() throws IOException, MyException {
        //获取配置文件路径
        String trackerConfig = FilesystemApplication.class.getResource("/tracker.conf").getPath();
        //ClientGlobal.init(conf_filename)
        //conf_filename:配置fastdfs连接地址,最大连接数,最长等待时间...的配置文件
        ClientGlobal.init(trackerConfig);

        TrackerClient trackerClient = new TrackerClient();

        //获取一个trackerServer的实例
        TrackerServer trackerServer=trackerClient.getTrackerServer();

        //通过tracker获得一个Storage连接客户端
        //第二个参数为是否指定一个storageServer
        StorageClient storageClient= new StorageClient(trackerServer,null);

        //第一个参数是文件的路径，第二个是文件的扩展名，第三个是元数据列表,为null是不传递元数据
        //返回值是上传的信息，上传的地址，组名，文件名...
        //访问文件的路径：192.168.142.128/group1/M00/00/00/wKiOgF-50wCAJMyZAABe9Dr3ixI226.jpg
        String[] uploadInfo = null;
        try {
            uploadInfo = storageClient.upload_file("C:\\Users\\yinjianliang\\Desktop\\111.jpg", "jpg", null);
        }catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < uploadInfo.length; i++) {
            System.out.println(uploadInfo[i]);
        }

    }

}



