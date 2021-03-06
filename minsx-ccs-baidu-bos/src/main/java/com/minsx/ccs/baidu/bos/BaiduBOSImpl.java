package com.minsx.ccs.baidu.bos;

import java.io.File;
import java.util.List;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.minsx.ccs.core.able.ListObjectsRequestable;
import com.minsx.ccs.core.able.ObjectRequestable;
import com.minsx.ccs.core.able.PageRequestable;
import com.minsx.ccs.core.able.PutObjectRequestable;
import com.minsx.ccs.core.config.BaiduBOSConfig;
import com.minsx.ccs.core.exception.NativeClientCastException;
import com.minsx.ccs.core.model.CCSBucket;
import com.minsx.ccs.core.model.CCSObject;
import com.minsx.ccs.core.model.CCSObjectList;
import com.minsx.ccs.core.model.CCSObjectMetadata;
import com.minsx.ccs.core.service.CCSClient;

public class BaiduBOSImpl implements CCSClient {

	private BaiduBOSConfig baiduBOSConfig;
	private BosClient bosClient;

	public BaiduBOSImpl(BaiduBOSConfig baiduBOSConfig) {
		this.baiduBOSConfig = baiduBOSConfig;
		BosClientConfiguration config = new BosClientConfiguration();
		config.setCredentials(
				new DefaultBceCredentials(baiduBOSConfig.getAccessKeyId(), baiduBOSConfig.getAccessKeySecret()));
		config.setEndpoint(baiduBOSConfig.getEndPoint());
		bosClient = new BosClient(config);
	}

	@Override
	public void createBucket(String bucketName) {
		bosClient.createBucket(bucketName);
	}

	@Override
	public void deleteBucket(String bucketName) {
		bosClient.deleteBucket(bucketName);
	}

	@Override
	public Boolean doesBucketExist(String bucketName) {
		return bosClient.doesBucketExist(bucketName);
	}

	@Override
	public Boolean doesObjectExist(String bucketName, String ccsObjectPath) {
		Boolean isExist = true;
		try {
			bosClient.getObjectMetadata(bucketName, ccsObjectPath);
		} catch (Exception e) {
			isExist = false;
		}
		return isExist;
	}

	@Override
	public CCSObjectList listObjects(String bucketName, String ccsFolderPath) {
		return BaiduBOSParseUtil.parseToCCSObjectList(bosClient.listObjects(bucketName, ccsFolderPath));
	}

	@Override
	public CCSObject getObject(String bucketName, String ccsObjectPath) {
		return BaiduBOSParseUtil.parseToCCSObject(bosClient.getObject(bucketName, ccsObjectPath));
	}

	@Override
	public CCSObjectMetadata getObjectMetadata(String bucketName, String ccsObjectPath) {
		return BaiduBOSParseUtil.parseToCCSObjectMetadata(bosClient.getObjectMetadata(bucketName, ccsObjectPath));
	}

	@Override
	public void copyObject(String sourceBucketName, String sourceObjectPath, String destinationBucketName,
			String destinationObjectPath) {
		bosClient.copyObject(sourceBucketName, sourceObjectPath, destinationBucketName, destinationObjectPath);
	}

	@Override
	public void putObject(String sourceFilePath, String bucketName, String ccsObjectPath) {
		bosClient.putObject(bucketName, ccsObjectPath, new File(sourceFilePath));
	}

	@Override
	public void deleteObject(String bucketName, String ccsObjectPath) {
		bosClient.deleteObject(bucketName, ccsObjectPath);
	}

	@Override
	public void shutdown() {
		bosClient.shutdown();
	}
 
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getNativeClient(Class<T> nativeClientClass) {
		if (!nativeClientClass.isInstance(BosClient.class)) {
			throw new NativeClientCastException(bosClient.getClass(), nativeClientClass);
		}
		return (T) bosClient;
	}

	public BaiduBOSConfig getBaiduBOSConfig() {
		return baiduBOSConfig;
	}

	public void setBaiduBOSConfig(BaiduBOSConfig baiduBOSConfig) {
		this.baiduBOSConfig = baiduBOSConfig;
	}

	@Override
	public CCSObjectList listObjects(PageRequestable pageable) {
		return null;
	}

	@Override
	public CCSObjectList listObjects(ListObjectsRequestable listObjectsRequestable) {
		return null;
	}

	@Override
	public CCSObject getObject(ObjectRequestable objectRequestable) {
		return null;
	}

	@Override
	public List<CCSBucket> listCCSBuckets() {
		return null;
	}

	@Override
	public void putObject(PutObjectRequestable putObjectRequestable) {
	}

}
