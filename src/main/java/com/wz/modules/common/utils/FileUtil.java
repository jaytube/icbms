package com.wz.modules.common.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {

	private static Logger log = Logger.getLogger(FileUtil.class);

	/**
	 * NIO way
	 * 
	 * @throws Exception
	 */
	public static byte[] toByteArray(String filename) throws Exception {

		File f = new File(filename);
		if (!f.exists()) {
			log.error("文件未找到！" + filename);
			throw new Exception();
		}
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			throw new Exception();
		} finally {
			try {
				channel.close();
			} catch (IOException e) {

			}
			try {
				fs.close();
			} catch (IOException e) {

			}
		}
	}
}